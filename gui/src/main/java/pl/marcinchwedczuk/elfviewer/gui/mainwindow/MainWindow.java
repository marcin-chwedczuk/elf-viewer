package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfReader;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.LongNativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.gui.UiService;
import pl.marcinchwedczuk.elfviewer.gui.aboutdialog.AboutDialog;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ContentsHexRenderer;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.NothingRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    public static MainWindow showOn(Stage window) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml"));

            Scene scene = new Scene(loader.load());
            MainWindow controller = (MainWindow) loader.getController();

            window.setTitle("Main Window");
            window.setScene(scene);
            window.setResizable(true);

            window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    controller.guiExit();
                }
            });

            controller.thisWindow = window;

            window.show();

            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Window thisWindow;

    @FXML
    private TextField filterText;

    @FXML
    private TreeView<RenderDataAction<?>> treeView;
    private TreeItem<RenderDataAction<?>> rootItem;

    @FXML
    private TableView<Object> tableView;

    private FileChooser openFileChooser;
    private FileChooser saveBinaryDataFileChooser;

    private File currentElfPath;
    private ElfFile<?> currentElfFile;

    @FXML
    private Menu recentlyOpen;
    private RecentlyOpenFiles recentlyOpenFiles;

    // Tree view menu
    private final ContextMenu contentsNodeMenu = new ContextMenuBuilder()
            .addItem("Save as...", e -> {
                TreeItem<RenderDataAction<?>> selected = treeView.getSelectionModel().getSelectedItem();
                ContentsHexRenderer<?> renderer = (ContentsHexRenderer<?>) selected.getValue().renderer();
                FileView fileView = renderer.fileView();

                saveContentsAsFile(fileView);
            })
            .mkContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openFileChooser = newOpenFileChooser();
        saveBinaryDataFileChooser = newSaveBinaryDataFileChooser();

        resetTreeView();

        // Selection mode
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldTreeItem, newTreeItem) -> {
                    if (oldTreeItem != null) {
                        oldTreeItem.getValue().unmountView();
                    }

                    // Clear filter while changing views
                    filterText.clear();

                    if (newTreeItem != null) {
                        newTreeItem.getValue()
                                .mountView(tableView, filterText.textProperty());
                    } else {
                        clearTable();
                    }
                });

        treeView.setOnContextMenuRequested(event -> {
            TreeItem<RenderDataAction<?>> selected = treeView.getSelectionModel().getSelectedItem();

            // Hide everything that we may have
            contentsNodeMenu.hide();

            if (selected != null) {
                if ("(Contents)".equals(selected.getValue().toString())) {
                    contentsNodeMenu.show(thisWindow, event.getScreenX(), event.getScreenY());
                }
            }

            event.consume(); // Do NOT remove, without this menu will be very sluggish/unstable
        });

        // Allow selecting cells
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setPlaceholder(new Label("Select data to display"));

        recentlyOpenFiles = new RecentlyOpenFiles(recentlyOpen, this::loadElfFile);
        recentlyOpenFiles.initialize();
    }

    private void clearTable() {
        tableView.setItems(FXCollections.emptyObservableList());
        tableView.getColumns().clear();
    }

    private void loadElfFile(File f) {
        if (currentElfFile != null) {
            try {
                ((FileSystemFile) currentElfFile.storage()).close();
            } catch (Exception e) { }

            currentElfFile = null;
            currentElfPath = null;

            resetTreeView();
        }

        try {
            currentElfFile = ElfReader.readElf(new FileSystemFile(f));
            currentElfPath = f;
        } catch (ElfReaderException e) {
            UiService.errorDialog("Cannot open ELF file.", e.getMessage());
            return;
        }

        recreateTreeView();
        recentlyOpenFiles.onFileOpen(f);
    }

    private void resetTreeView() {
        rootItem = new TreeItem<>(new RenderDataAction<>(
                "No ELF file loaded",
                new NothingRenderer<>(new LongNativeWord())));
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
    }

    @SuppressWarnings("unchecked")
    private void recreateTreeView() {
        rootItem = (TreeItem<RenderDataAction<?>>)(TreeItem<?>) new ElfExplorerTreeViewBuilder<>(
                currentElfPath,
                currentElfFile,
                tableView,
                filterText.textProperty())
                    .build();
        treeView.setRoot(rootItem);
        rootItem.setExpanded(true);
    }

    @FXML
    private void tableViewKeyPressed(KeyEvent event) {
        KeyCodeCombination copyShortcut = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
        if (copyShortcut.match(event)) {
            event.consume();

            ClipboardDataBuilder cdb = new ClipboardDataBuilder();

            // Cells are in by row order, here we depend on this JavaFX behaviour
            ObservableList<TablePosition> cells = tableView.getSelectionModel().getSelectedCells();
            for (int i = 0; i < cells.size(); i++) {
                TablePosition position = cells.get(i);
                Object data = position.getTableColumn().getCellData(position.getRow());
                cdb.addCellData(position.getRow(), position.getColumn(), data);
            }

            cdb.copyToClipboard();
        }
    }

    private void saveContentsAsFile(FileView fileView) {
        File f = saveBinaryDataFileChooser.showSaveDialog(thisWindow);
        if (f != null) {
            try {
                byte[] buffer = new byte[1024 * 4];

                // TODO: Move to method on FileView
                try (FileOutputStream outputStream = new FileOutputStream(f)) {
                    long offset = 0;
                    int nbytes;
                    while ((nbytes = fileView.readBuffer(offset, buffer)) > 0) {
                        outputStream.write(buffer, 0, nbytes);
                        offset += nbytes;
                    }
                }

            } catch (IOException e) {
                UiService.errorDialog("Cannot save contents to file.", e.getMessage());
            }
        }
    }

    @FXML
    private void guiOpen() {
        File f = openFileChooser.showOpenDialog(thisWindow);
        if (f != null) {
            loadElfFile(f);
        }
    }

    @FXML
    private void guiExit() {
        Platform.exit();
    }

    @FXML
    private void guiAbout() {
        AboutDialog.show(thisWindow);
    }

    @FXML
    private void clearFilterText() {
        filterText.clear();
    }

    public static FileChooser newOpenFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select ELF file...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        return fileChooser;
    }

    public static FileChooser newSaveBinaryDataFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save data...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Binary data", "*.bin"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        return fileChooser;
    }
}

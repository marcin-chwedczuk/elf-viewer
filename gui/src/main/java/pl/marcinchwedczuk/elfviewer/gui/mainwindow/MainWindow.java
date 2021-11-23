package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfReader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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

            controller.window = window;

            window.show();

            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Window window;

    @FXML
    private TreeView<DisplayAction> treeView;
    private TreeItem<DisplayAction> rootItem;

    @FXML
    private TableView<Object> tableView;

    private FileChooser openFileChooser;

    private File currentElfPath;
    private Elf32File currentElfFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openFileChooser = newOpenFileChooser();

        rootItem = new TreeItem<>(new DisplayAction("No ELF file loaded"));
        rootItem.setExpanded(true);

        treeView.setRoot(rootItem);

        // Selection mode
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldTreeItem, newTreeItem) -> {
                    if (newTreeItem != null) {
                        newTreeItem.getValue().runAction();
                    } else {
                        clearTable();
                    }
                });

        tableView.setPlaceholder(new Label("Select data to display"));
    }

    private void loadElfFile(File f) {
        currentElfFile = ElfReader.readElf32(new FileSystemFile(f));
        currentElfPath = f;

        recreateTreeView();
    }

    private void recreateTreeView() {
        rootItem.getChildren().clear();

        String name = currentElfPath.getName();
        rootItem.setValue(new DisplayAction(name, this::clearTable));

        TreeItem<DisplayAction> elfHeader = new TreeItem<>(
                new DisplayAction("ELF Header", () -> {
                    displayInTable(currentElfFile.header);
                }));
        rootItem.getChildren().add(elfHeader);

        TreeItem<DisplayAction> identificationBytes = new TreeItem<>(
                new DisplayAction("Identification Bytes", () -> {
                    displayInTable(currentElfFile.header.identification());
                }));
        elfHeader.getChildren().add(identificationBytes);
    }

    private void clearTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    private TableColumn<Object, String> mkColumn(String caption, String propertyName) {
        TableColumn<Object, String> column = new TableColumn<>(caption);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setSortable(false);
        column.setEditable(false);
        column.setResizable(true);
        return column;
    }

    private void setupTableGenericNumericItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> hexValueColumn = mkColumn("Value Hex", "hexValue");
        TableColumn<Object, String> intValueColumn = mkColumn("Value Int", "intValue");
        TableColumn<Object, String> descriptionColumn = mkColumn("Description", "description");

        tableView.getColumns().addAll(
                fieldNameColumn, hexValueColumn, intValueColumn, descriptionColumn
        );
    }

    private void setupTableGenericStringItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> valueColumn = mkColumn("Value", "value");
        TableColumn<Object, String> descriptionColumn = mkColumn("Description", "description");

        tableView.getColumns().addAll(
                fieldNameColumn, valueColumn, descriptionColumn
        );
    }

    private void displayInTable(Elf32Header header) {
        setupTableGenericNumericItem();
    }

    private void displayInTable(ElfIdentification identification) {
        setupTableGenericStringItem();

        tableView.getItems().addAll(
                new GenericStringItem("Magic String", identification.printableMagicString()),
                new GenericStringItem("Class", identification.elfClass()),
                new GenericStringItem("Data", identification.elfData()),
                new GenericStringItem("Version", identification.elfVersion()),
                new GenericStringItem("OS ABI", identification.osAbi()),
                new GenericStringItem("OS ABI Version", identification.osAbiVersion()),
                new GenericStringItem("Padding bytes", Arrays.toString(identification.paddingBytes()))
        );
    }

    @FXML
    private void guiOpen() {
        File f = openFileChooser.showOpenDialog(window);
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

    }

    public static FileChooser newOpenFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select ELF file...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*")
        );

        return fileChooser;
    }
}

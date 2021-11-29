package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    @FXML
    private Menu recentlyOpen;
    private RecentlyOpenFiles recentlyOpenFiles;

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
                        newTreeItem.getValue().displayOn(tableView);
                    } else {
                        clearTable();
                    }
                });

        tableView.setPlaceholder(new Label("Select data to display"));

        recentlyOpenFiles = new RecentlyOpenFiles(recentlyOpen, this::loadElfFile);
        recentlyOpenFiles.initialize();
    }

    private void clearTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    private void loadElfFile(File f) {
        currentElfFile = ElfReader.readElf32(new FileSystemFile(f));
        currentElfPath = f;

        recreateTreeView();

        recentlyOpenFiles.onFileOpen(f);
    }

    private void recreateTreeView() {
        rootItem = new TreeViewMenuBuilder(
                    currentElfPath,
                    currentElfFile,
                    tableView)
                .build();
        treeView.setRoot(rootItem);
        rootItem.setExpanded(true);
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
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        return fileChooser;
    }
}

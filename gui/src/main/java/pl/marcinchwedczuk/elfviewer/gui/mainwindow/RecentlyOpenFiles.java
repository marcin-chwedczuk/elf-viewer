package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.marcinchwedczuk.elfviewer.gui.AppSettings;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class RecentlyOpenFiles {
    private final Menu menu;
    private final Consumer<File> action;

    public RecentlyOpenFiles(Menu menu, Consumer<File> action) {
        this.menu = menu;
        this.action = action;
    }

    public void initialize() {
        populateMenu();
    }

    private void populateMenu() {
        menu.getItems().clear();

        List<File> files = new AppSettings().recentFiles();
        for (File f: files) {
            MenuItem tmp = new MenuItem("Open " + f.getName());
            tmp.setOnAction(event -> {
                action.accept(f);
            });
            menu.getItems().add(tmp);
        }
    }

    public void onFileOpen(File f) {
        // Update recent files
        AppSettings appSettings = new AppSettings();
        appSettings.addRecentFile(f);
        appSettings.save();

        // Update menu
        // TODO: Make it more optimal
        populateMenu();
    }
}

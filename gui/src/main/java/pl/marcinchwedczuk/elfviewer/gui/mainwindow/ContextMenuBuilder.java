package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuBuilder {
    private final List<MenuItem> items = new ArrayList<>();

    public ContextMenuBuilder addItem(String title, EventHandler<ActionEvent> handler) {
        MenuItem m = new MenuItem(title);
        m.setOnAction(handler);

        items.add(m);
        return this;
    }

    public ContextMenu mkContextMenu() {
        ContextMenu cm = new ContextMenu(items.toArray(MenuItem[]::new));
        cm.setAutoHide(true);
        return cm;
    }
}

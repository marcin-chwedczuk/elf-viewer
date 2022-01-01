package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuBuilder {
    private final List<MenuItem> items = new ArrayList<>();

    public ContextMenuBuilder addItem(String title, EventHandler<ActionEvent> handler) {
        return addItem(null, title, handler);
    }

    public ContextMenuBuilder addItem(Image icon, String title, EventHandler<ActionEvent> handler) {
        ImageView graphics = (icon != null) ? new ImageView(icon) : null;
        if (graphics != null) {
            graphics.setPreserveRatio(true);
            graphics.setFitWidth(24.0);
        }

        MenuItem m = new MenuItem(title, graphics);
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

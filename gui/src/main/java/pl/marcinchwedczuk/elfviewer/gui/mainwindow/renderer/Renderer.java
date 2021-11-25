package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableView;

public interface Renderer {
    void renderDataOn(TableView<Object> tableView);
}

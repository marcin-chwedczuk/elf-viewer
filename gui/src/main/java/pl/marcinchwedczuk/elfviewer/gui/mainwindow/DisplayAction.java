package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.control.TableView;

import java.util.function.Consumer;

public class DisplayAction {
    private final String displayName;
    private final Consumer<? super TableView<Object>> action;

    public DisplayAction(String displayName,
                         Consumer<? super TableView<Object>> action) {
        this.displayName = displayName;
        this.action = action;
    }

    public DisplayAction(String displayName) {
        this(displayName, tv -> { });
    }

    @Override
    public String toString() {
        return displayName;
    }

    public void displayOn(TableView<Object> tableView) {
        action.accept(tableView);
    }
}

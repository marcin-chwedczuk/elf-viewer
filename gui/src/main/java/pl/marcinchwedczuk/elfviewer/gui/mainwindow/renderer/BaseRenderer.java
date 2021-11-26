package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.LambdaValueFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class BaseRenderer<R> implements Renderer {
    @Override
    public void renderDataOn(TableView<Object> tableView) {
        resetTable(tableView);

        ((TableView<R>) tableView).getColumns()
                .addAll(defineColumns());

        tableView.getItems()
                .addAll(defineRows());
    }

    protected abstract List<TableColumn<R, String>> defineColumns();
    protected abstract List<? extends R> defineRows();

    private void resetTable(TableView<?> tableView) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    private static void setColumnDefaults(TableColumn<?,?> column) {
        column.setSortable(false);
        column.setEditable(false);
        column.setResizable(true);
    }

    protected TableColumn<R, String> mkColumn(String title,
                                              Function<R, Object> mapper,
                                              ColumnAttributes... attributes) {
        TableColumn<R, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new LambdaValueFactory<>(
                rowData -> Objects.toString(mapper.apply(rowData))));

        setColumnDefaults(column);
        for (ColumnAttributes attribute : attributes) {
            attribute.apply(column);
        }

        return column;
    }

    protected static String hex(int value) {
        return String.format("0x%08x", value);
    }

    protected static String dec(int value) {
        return String.format("%010d", value);
    }

    protected static String hex(long value) {
        return String.format("0x%16x", value);
    }

    protected static String dec(long value) {
        return String.format("%020d", value);
    }

    protected static String placeholder(String placeholder, String s) {
        if (s == null || s.length() == 0) return placeholder;
        return s;
    }
}

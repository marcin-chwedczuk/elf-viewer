package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.LambdaValueFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class BaseRenderer<R, NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        implements Renderer
{
    private final NativeWord<NATIVE_WORD> nativeWordMetadata;

    protected BaseRenderer(NativeWord<NATIVE_WORD> nativeWordMetadata) {
        this.nativeWordMetadata = nativeWordMetadata;
    }

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
                                              Function<? super R, Object> mapper,
                                              ColumnAttributes... attributes) {
        return mkColumn(title, "", mapper, attributes);
    }

    protected TableColumn<R, String> mkColumn(String title,
                                              String comment,
                                              Function<? super R, Object> mapper,
                                              ColumnAttributes... attributes) {
        TableColumn<R, String> column = new TableColumn<>(title);

        if (comment != null && !comment.isBlank()) {
            Label label = new Label(title);

            Tooltip commentTooltip = new Tooltip(comment);
            commentTooltip.setMaxWidth(200);
            commentTooltip.setWrapText(true);
            label.setTooltip(commentTooltip);

            column.setGraphic(label);
        }

        column.setCellValueFactory(new LambdaValueFactory<>(
                rowData -> Objects.toString(mapper.apply(rowData))));

        setColumnDefaults(column);
        for (ColumnAttributes attribute : attributes) {
            attribute.apply(column);
        }

        return column;
    }

    protected static String hex(byte b) {
        return String.format("0x%02x", (int)b & 0xff);
    }

    protected static String hex(short b) {
        return String.format("0x%04x", (int)b & 0xffff);
    }

    protected String hex(NATIVE_WORD value) {
        if (value == null) return "";
        return nativeWordMetadata.toHexString(value);
    }

    protected String dec(NATIVE_WORD value) {
        if (value == null) return "";
        return nativeWordMetadata.toDecString(value);
    }

    protected static String hex(int value) {
        return String.format("0x%08x", value);
    }

    protected static String dec(int value) {
        return String.format("%d", value);
    }

    protected static String hex(long value) {
        return String.format("0x%16x", value);
    }

    protected static String dec(long value) {
        return String.format("%d", value);
    }

    protected static String placeholder(String placeholder, String s) {
        if (s == null || s.length() == 0) return placeholder;
        return s;
    }
}

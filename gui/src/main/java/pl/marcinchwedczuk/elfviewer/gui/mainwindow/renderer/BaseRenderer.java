package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.LambdaValueFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class BaseRenderer<R, NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        implements Renderer {
    private final NativeWord<NATIVE_WORD> nativeWordMetadata;
    private final StringProperty filter;

    protected BaseRenderer(NativeWord<NATIVE_WORD> nativeWordMetadata,
                           StringProperty searchPhrase) {
        this.nativeWordMetadata = nativeWordMetadata;
        this.filter = searchPhrase;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void renderDataOn(TableView<Object> tableView) {
        resetTable(tableView);

        ((TableView<R>) tableView).getColumns()
                .addAll(defineColumns());

        // TODO: Not efficient, subclasses should return observable array list or
        // there should be wrapper
        FilteredList<R> filteredList = new FilteredList<>(
                FXCollections.observableArrayList(defineRows()),
                createFilter(filter.getValue()));

        // TODO: Make sure to clear search field listeners when changing view
        filter.addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(createFilter(filter.get())));

        tableView.itemsProperty().set((FilteredList<Object>) (FilteredList<?>) filteredList);
    }

    protected abstract List<TableColumn<R, String>> defineColumns();

    protected abstract List<R> defineRows();

    protected abstract Predicate<R> createFilter(String searchPhrase);

    private void resetTable(TableView<?> tableView) {
        tableView.itemsProperty().set(FXCollections.emptyObservableList());
        tableView.getColumns().clear();
    }

    private static void setColumnDefaults(TableColumn<?, ?> column) {
        column.setSortable(false);
        column.setEditable(false);
        column.setResizable(true);
    }

    protected String titleDouble(String upperTitle, String lowerTitle) {
        return String.format("%s\n---\n%s", upperTitle, lowerTitle);
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

    protected static Function<String[], Object> indexAccessor(final int index) {
        return (String[] arr) -> index < arr.length ? arr[index] : "";
    }

    protected static String[] mkStrings(String... strings) {
        return strings;
    }

    protected static Predicate<String[]> mkStringsFilter(String phrase) {
        // TODO: Support multiple keywords 'foo bar'
        // TODO: Support detaching search phrase listener

        Pattern p = (phrase != null && !phrase.isEmpty())
                ? Pattern.compile(Pattern.quote(phrase), Pattern.CASE_INSENSITIVE)
                : null;

        return (strings) -> {
            if (p == null)
                return true;

            if (strings == null || strings.length == 0)
                return false;

            for (int i = 0; i < strings.length; i++) {
                if (strings[i] != null && p.matcher(strings[i]).find())
                    return true;
            }

            return false;
        };
    }

    protected static String hex(byte b) {
        return String.format("0x%02x", (int) b & 0xff);
    }

    protected static String hex(short b) {
        return String.format("0x%04x", (int) b & 0xffff);
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

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.LambdaValueFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class BaseRenderer<R, NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        implements Renderer {
    private final NativeWord<NATIVE_WORD> nativeWordMetadata;

    private final StringProperty filterPhase = new SimpleStringProperty();

    protected BaseRenderer(NativeWord<NATIVE_WORD> nativeWordMetadata) {
        this.nativeWordMetadata = nativeWordMetadata;
    }

    public StringProperty filterPhaseProperty() { return filterPhase; }

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
                createFilter(filterPhase.getValue()));

        filterPhase.addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(createFilter(filterPhase.get())));

        tableView.itemsProperty()
                .set((FilteredList<Object>) (FilteredList<?>) filteredList);
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

    protected static Predicate<String> mkStringFilter(String phrase) {
        Predicate<String[]> stringsMatcher = mkMatcher(phrase);
        return (s) -> stringsMatcher.test(new String[] { s });
    }

    protected static Predicate<String[]> mkStringsFilter(String phrase) {
        return mkMatcher(phrase);
    }

    private static Predicate<String[]> mkMatcher(String pattern) {
        if (pattern == null || pattern.isBlank())
            return (strings) -> true;

        String[] parts = pattern.split("\\s+");
        if (parts.length == 0)
            return (strings) -> true;

        Pattern[] subPatterns = Arrays.stream(parts)
                .map(Pattern::quote)
                .map(q -> Pattern.compile(q, Pattern.CASE_INSENSITIVE))
                .toArray(Pattern[]::new);

        return (strings) -> {
            // strings represents a row, we match when the row contains all parts of the pattern
            if (strings == null || strings.length == 0)
                return false;

            // TODO: Not very efficient - can we make it faster?
            for (int i = 0; i < subPatterns.length; i++) {
                Pattern p = subPatterns[i];

                boolean hasMatch = false;
                for (int j = 0; j < strings.length; j++) {
                    if (p.matcher(strings[j]).find()) {
                        hasMatch = true;
                        break;
                    }
                }

                if (!hasMatch) return false;
            }

            return true;
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

    protected String kb(NATIVE_WORD fileSize) {
        return dec(fileSize.longValue());
    }

    protected static String kb(long fileSize) {
        if (fileSize == 0) return "0 Bytes";

        int k = 1024;
        String[] sizes = {"Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int i = (int) Math.floor(Math.log(fileSize) / Math.log(k));
        return String.format("%s %s",
                simplifyNumberString(Double.toString(fileSize / Math.pow(k, i))),
                sizes[i]);
    }

    private static String simplifyNumberString(String s) {
        if (s == null || s.isEmpty()) return s;

        int i = s.length()-1;
        // TODO: Test on system with , as separator
        while (i > 0 && (s.charAt(i) == '0' || s.charAt(i) == '.')) {
            i--;
            if (s.charAt(i+1) == '.') break;
        }

        return s.substring(0, i+1);
    }

    protected static String placeholder(String placeholder, String s) {
        if (s == null || s.length() == 0) return placeholder;
        return s;
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import java.util.Objects;

public class GenericStringItem {
    private final String fieldName;
    private final String value;
    private final String description;

    public GenericStringItem(String fieldName, Object value) {
        this.fieldName = fieldName;
        this.value = Objects.toString(value);
        this.description = "TODO";
    }

    public String getFieldName() {
        return fieldName;
    }
    public String getValue() { return value; }
    public String getDescription() {
        return description;
    }
}

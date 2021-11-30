package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

import java.util.Objects;

public class StringTableEntryDto {
    private final String fieldName;
    private final String value;

    public StringTableEntryDto(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    // TODO: Remove
    public StringTableEntryDto(String fieldName, Object value) {
        this.fieldName = fieldName;
        this.value = Objects.toString(value);
    }

    public String getFieldName() {
        return fieldName;
    }
    public String getValue() { return value; }

    public int getValueLength() {
        if (value == null) return 0;
        return value.length();
    }
}

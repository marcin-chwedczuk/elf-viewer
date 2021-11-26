package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

public class StructureFieldDto {
    private final String fieldName;
    private final String rawValue;
    private final String parsedValue;
    private final String comment;

    public StructureFieldDto(String fieldName, String value) {
        this.fieldName = fieldName;
        this.rawValue = value;
        this.parsedValue = "";
        this.comment = "TODO";
    }

    public StructureFieldDto(String fieldName,
                             String rawValue,
                             String parsedValue,
                             String comment) {
        this.fieldName = fieldName;
        this.rawValue = rawValue;
        this.parsedValue = parsedValue;
        this.comment = comment;
    }

    public StructureFieldDto(String fieldName, int value) {
        this.fieldName = fieldName;
        this.rawValue = Integer.toUnsignedString(value);
        this.parsedValue = "";
        this.comment = "";
    }

    // For displaying enum values in the same table
    public StructureFieldDto(String fieldName, IntPartialEnum<?> value) {
        this.fieldName = fieldName;
        this.parsedValue = String.format("0x%08x", value.value());
        this.rawValue = value.name();
        this.comment = "TODO";
    }

    public StructureFieldDto(String fieldName, ShortPartialEnum<?> value) {
        this.fieldName = fieldName;
        this.parsedValue = String.format("0x%04x", 0xffff & value.value());
        this.rawValue = value.name();
        this.comment = "TODO";
    }

    public StructureFieldDto(String fieldName, BytePartialEnum<?> value) {
        this.fieldName = fieldName;
        this.parsedValue = String.format("0x%02x", 0xff & value.value());
        this.rawValue = value.name();
        this.comment = "TODO";
    }

    // For address & offset
    public StructureFieldDto(String fieldName, Elf32Address value) {
        this(fieldName, value.toString());
    }
    public StructureFieldDto(String fieldName, Elf32Offset value) {
        // TODO: int -> long as this is wrong currently
        this(fieldName, value.toString());
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getRawValue() {
        return rawValue;
    }

    public String getParsedValue() {
        return parsedValue;
    }

    public String getComment() {
        return comment;
    }
}

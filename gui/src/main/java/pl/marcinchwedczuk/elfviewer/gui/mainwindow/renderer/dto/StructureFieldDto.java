package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;

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

    // For address & offset
    public StructureFieldDto(String fieldName, ElfAddress<?> value) {
        this(fieldName, value.toString());
    }
    public StructureFieldDto(String fieldName, ElfOffset<?> value) {
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

package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Objects;

public class GenericNumericItem {
    private final String fieldName;
    private final String hexValue;
    private final String intValue;
    private final String description;

    public GenericNumericItem(String fieldName, String value) {
        this.fieldName = fieldName;
        this.hexValue = value;
        this.intValue = "";
        this.description = "TODO";
    }

    public GenericNumericItem(String fieldName, int value) {
        this.fieldName = fieldName;
        this.hexValue = String.format("0x%08x", value);
        this.intValue = Integer.toUnsignedString(value);
        this.description = "TODO";
    }

    // For displaying enum values in the same table
    public GenericNumericItem(String fieldName, IntPartialEnum<?> value) {
        this.fieldName = fieldName;
        this.hexValue = String.format("0x%08x", value.value());
        this.intValue = value.name();
        this.description = "TODO";
    }

    public GenericNumericItem(String fieldName, ShortPartialEnum<?> value) {
        this.fieldName = fieldName;
        this.hexValue = String.format("0x%04x", 0xffff & value.value());
        this.intValue = value.name();
        this.description = "TODO";
    }

    public GenericNumericItem(String fieldName, BytePartialEnum<?> value) {
        this.fieldName = fieldName;
        this.hexValue = String.format("0x%02x", 0xff & value.value());
        this.intValue = value.name();
        this.description = "TODO";
    }

    // For address & offset
    public GenericNumericItem(String fieldName, Elf32Address value) {
        this(fieldName, value.intValue());
    }
    public GenericNumericItem(String fieldName, Elf32Offset value) {
        // TODO: int -> long as this is wrong currently
        this(fieldName, (int)value.longValue());
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getHexValue() {
        return hexValue;
    }

    public String getIntValue() {
        return intValue;
    }

    public String getDescription() {
        return description;
    }
}

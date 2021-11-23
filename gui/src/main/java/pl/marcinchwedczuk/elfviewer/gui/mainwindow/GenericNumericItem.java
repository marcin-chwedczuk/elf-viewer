package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

public class GenericNumericItem {
    private final String fieldName;
    private final String hexValue;
    private final String intValue;
    private final String description;

    public GenericNumericItem(String fieldName, int value) {
        this.fieldName = fieldName;
        this.hexValue = String.format("0x%08x", value);
        this.intValue = Integer.toUnsignedString(value);
        this.description = "TODO";
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

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

public class DynamicTagDto {
    private final String type;
    private final String value;

    public DynamicTagDto(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

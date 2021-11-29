package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

public class DynamicTagDto {
    private final String type;
    private final String value;
    private final String comment;

    public DynamicTagDto(String type,
                         String value,
                         String comment) {
        this.type = type;
        this.value = value;
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

public class IndexValueDto {
    private final String index;
    private final String value;

    public IndexValueDto(String index, String value) {
        this.index = index;
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}

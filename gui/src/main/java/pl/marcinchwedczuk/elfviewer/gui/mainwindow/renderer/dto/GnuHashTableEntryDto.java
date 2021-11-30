package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

public class GnuHashTableEntryDto {
    private final String col1;
    private final String col2;
    private final String col3;
    private final String col4;

    public GnuHashTableEntryDto(String col1,
                                String col2,
                                String col3,
                                String col4) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
    }

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }

    public String getCol4() {
        return col4;
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

public class RelocationAddendDto {
    private final String offset;
    private final String info;
    private final String symbol;
    private final String type;
    private final String addend;

    public RelocationAddendDto(String offset,
                               String info,
                               String symbol,
                               String type,
                               String addend) {
        this.offset = offset;
        this.info = info;
        this.symbol = symbol;
        this.type = type;
        this.addend = addend;
    }

    public String getOffset() {
        return offset;
    }

    public String getInfo() {
        return info;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public String getAddend() { return addend; }
}

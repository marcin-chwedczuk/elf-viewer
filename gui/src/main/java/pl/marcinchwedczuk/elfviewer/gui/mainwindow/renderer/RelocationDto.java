package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

public class RelocationDto {
    private final String offset;
    private final String info;
    private final String symbol;
    private final String type;

    public RelocationDto(String offset,
                         String info,
                         String symbol,
                         String type) {
        this.offset = offset;
        this.info = info;
        this.symbol = symbol;
        this.type = type;
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
}

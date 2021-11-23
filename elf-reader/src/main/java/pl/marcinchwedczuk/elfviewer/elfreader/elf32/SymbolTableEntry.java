package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

public class SymbolTableEntry {
    public final SymbolTableIndex index;
    public final Elf32Symbol symbol;

    public SymbolTableEntry(SymbolTableIndex index, Elf32Symbol symbol) {
        this.index = index;
        this.symbol = symbol;
    }
}

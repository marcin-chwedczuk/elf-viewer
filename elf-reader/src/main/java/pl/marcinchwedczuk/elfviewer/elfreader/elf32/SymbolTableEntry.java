package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

public class SymbolTableEntry {
    public final SymbolTableIndex index;
    public final Elf32Symbol symbol;
    public final Elf32SectionHeader relatedSection;

    public SymbolTableEntry(SymbolTableIndex index,
                            Elf32Symbol symbol,
                            Elf32SectionHeader relatedSection) {
        this.index = index;
        this.symbol = symbol;
        this.relatedSection = relatedSection;
    }
}

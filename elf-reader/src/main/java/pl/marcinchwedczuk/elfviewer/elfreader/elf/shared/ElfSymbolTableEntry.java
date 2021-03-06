package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

public class ElfSymbolTableEntry<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    public final SymbolTableIndex index;
    public final ElfSymbol<NATIVE_WORD> symbol;
    public final ElfSectionHeader<NATIVE_WORD> relatedSection;

    public ElfSymbolTableEntry(SymbolTableIndex index,
                               ElfSymbol<NATIVE_WORD> symbol,
                               ElfSectionHeader<NATIVE_WORD> relatedSection) {
        this.index = index;
        this.symbol = symbol;
        this.relatedSection = relatedSection;
    }
}

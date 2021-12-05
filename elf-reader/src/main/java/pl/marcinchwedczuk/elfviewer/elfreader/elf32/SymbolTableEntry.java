package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTableEntry;

public class SymbolTableEntry {
    public final SymbolTableIndex index;
    public final Elf32Symbol symbol;
    public final Elf32SectionHeader relatedSection;

    public SymbolTableEntry(ElfSymbolTableEntry<Integer> s) {
        this.index = s.index;
        // TODO: not effective
        this.symbol = new Elf32Symbol(s.symbol);
        this.relatedSection = Elf32SectionHeader.safeWrap(s.relatedSection);
    }
}

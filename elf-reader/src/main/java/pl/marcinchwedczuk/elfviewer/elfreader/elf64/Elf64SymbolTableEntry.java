package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTableEntry;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Symbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;

public class Elf64SymbolTableEntry {
    public final SymbolTableIndex index;
    public final Elf64Symbol symbol;
    public final Elf64SectionHeader relatedSection;

    public Elf64SymbolTableEntry(ElfSymbolTableEntry<Long> s) {
        this.index = s.index;
        // TODO: not effective
        this.symbol = new Elf64Symbol(s.symbol);
        this.relatedSection = Elf64SectionHeader.safeWrap(s.relatedSection);
    }
}

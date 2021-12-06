package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSymbolTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64SymbolTable;

public class Elf64SymbolTableSection extends Elf64Section {
    private final ElfSymbolTableSection<Long> section;

    public Elf64SymbolTableSection(ElfSymbolTableSection<Long> section) {
        super(section);
        this.section = section;
    }

    public Elf64SymbolTable symbolTable() {
        return new Elf64SymbolTable(section.symbolTable());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSymbolTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

public class Elf32SymbolTableSection extends Elf32BasicSection {
    private final ElfSymbolTableSection<Integer> section;

    public Elf32SymbolTableSection(ElfSymbolTableSection<Integer> section) {
        super(section);
        this.section = section;
    }


    public SymbolTable symbolTable() {
        return new SymbolTable(section.symbolTable());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

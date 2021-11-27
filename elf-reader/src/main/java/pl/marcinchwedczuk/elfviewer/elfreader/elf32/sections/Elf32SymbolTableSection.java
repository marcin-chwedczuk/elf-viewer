package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public class Elf32SymbolTableSection extends Elf32Section {
    public Elf32SymbolTableSection(Elf32File elfFile,
                                   Elf32SectionHeader header) {
        super(elfFile, header);

        Args.checkSectionType(header(), SYMBOL_TABLE, DYNAMIC_SYMBOLS);
    }

    public SymbolTable symbolTable() {
        int stringTableSectionIndex = header().link();

        Elf32StringTableSection stringTableSection =
                (Elf32StringTableSection) elfFile().sections().get(stringTableSectionIndex);

        return new SymbolTable(
                elfFile(),
                header(),
                stringTableSection.stringTable());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

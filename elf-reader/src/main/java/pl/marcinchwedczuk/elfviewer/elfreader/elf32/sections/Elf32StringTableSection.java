package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.STRING_TABLE;

public class Elf32StringTableSection extends Elf32Section {
    public Elf32StringTableSection(Elf32File elfFile,
                                   Elf32SectionHeader header) {
        super(elfFile, header);

        Args.checkSectionType(header, STRING_TABLE);
    }

    public StringTable stringTable() {
        // TODO: Add ctor contents + size
        return new StringTable(
                contents(),
                Elf32Offset.ZERO,
                Elf32Offset.ZERO.plus(header().sectionSize()));
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

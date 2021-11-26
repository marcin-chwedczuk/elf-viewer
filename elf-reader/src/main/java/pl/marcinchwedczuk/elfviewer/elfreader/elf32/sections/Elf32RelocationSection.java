package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Relocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.RelocationsTable;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.REL;

public class Elf32RelocationSection extends Elf32Section {
    public Elf32RelocationSection(Elf32File elfFile,
                                  Elf32SectionHeader header) {
        super(elfFile, header);
        Args.checkSectionType(header, REL);
    }

    public List<Elf32Relocation> relocations() {
        RelocationsTable relocationsTable =
                new RelocationsTable(header(), elfFile());

        return new ArrayList<>(relocationsTable.relocations());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.Optional;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.REL;

public class RelocationsTable {
    private final AbstractFile file;
    private final Endianness endianness;
    private final Elf32SectionHeader section;
    private final Elf32File elf32File;

    private final TableHelper tableHelper;

    public RelocationsTable(AbstractFile file,
                            Endianness endianness,
                            Elf32SectionHeader section,
                            Elf32File elf32File) {
        // TODO: Check this condition
        if (!section.type().is(REL))
            throw new IllegalArgumentException("Invalid section type!");

        this.file = file;
        this.endianness = endianness;
        this.section = section;
        this.elf32File = elf32File;

        tableHelper = new TableHelper(
                section.offsetInFile(),
                section.containedEntrySize(),
                section.sectionSize() / section.containedEntrySize());
    }

    public int size() {
        return section.sectionSize() / section.containedEntrySize();
    }

    Elf32Relocation get(int index) {
        Elf32Offset startOffset = tableHelper.offsetForEntry(index);
        StructuredFile symbolFile = new StructuredFile(file, endianness, startOffset);

        Elf32Address offset = symbolFile.readAddress();
        int info = symbolFile.readUnsignedInt();

        return new Elf32Relocation(
                offset,
                info);
    }
}

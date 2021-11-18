package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.REL;

public class RelocationsTable {
    private final Elf32SectionHeader section;
    private final Elf32File elfFile;

    private final TableHelper tableHelper;

    public RelocationsTable(Elf32SectionHeader section,
                            Elf32File elfFile) {
        requireNonNull(section);
        requireNonNull(elfFile);

        // TODO: Check this condition
        Args.checkSectionType(section, REL);

        this.section = section;
        this.elfFile = elfFile;

        tableHelper = TableHelper.forSectionEntries(section);
    }

    public int size() {
        return tableHelper.tableSize();
    }

    Elf32Relocation get(int index) {
        Elf32Offset startOffset = tableHelper.offsetForEntry(index);
        StructuredFile sf = new StructuredFile(elfFile, startOffset);

        Elf32Address offset = sf.readAddress();
        int info = sf.readUnsignedInt();

        return new Elf32Relocation(
                offset,
                info);
    }
}

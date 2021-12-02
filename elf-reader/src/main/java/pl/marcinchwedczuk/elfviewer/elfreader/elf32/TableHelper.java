package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;

import static java.util.Objects.requireNonNull;

public class TableHelper {
    public static TableHelper forSectionHeaders(Elf32Header header) {
        return new TableHelper(
                header.sectionHeaderTableOffset(),
                header.sectionHeaderSize(),
                header.numberOfSectionHeaders());
    }

    public static TableHelper forProgramHeaders(Elf32Header header) {
        return new TableHelper(
                header.programHeaderTableOffset(),
                header.programHeaderSize(),
                header.numberOfProgramHeaders());
    }

    public static TableHelper forSectionEntries(Elf32SectionHeader sectionHeader) {
        return new TableHelper(
                sectionHeader.fileOffset(),
                sectionHeader.containedEntrySize(),
                // TODO: Use actual section size
                sectionHeader.size() / sectionHeader.containedEntrySize());
    }

    private final Elf32Offset startOffset;
    private final int entrySize;
    private final int entriesCount;

    private TableHelper(Elf32Offset startOffset,
                       int entrySize,
                       int entriesCount) {
        requireNonNull(startOffset);
        if (entrySize <= 0)
            throw new IllegalArgumentException("Entry size must be a positive number.");

        this.startOffset = startOffset;
        this.entrySize = entrySize;
        this.entriesCount = entriesCount;
    }

    public int tableSize() {
        return entriesCount;
    }

    public Elf32Offset offsetForEntry(int index) {
        if (index >= tableSize()) {
            throw new IndexOutOfBoundsException("Index is out of bounds: " + index);
        }

        int entryOffset = startOffset.intValue() + index * entrySize;
        return new Elf32Offset(entryOffset);
    }

    public Elf32Offset offsetForEntry(SectionHeaderIndex index) {
        return offsetForEntry(index.intValue());
    }

    public Elf32Offset offsetForEntry(SymbolTableIndex index) {
        return offsetForEntry(index.intValue());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import static java.util.Objects.requireNonNull;

public class TableHelper<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    public static <NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
    TableHelper<NATIVE_WORD> forSectionHeaders(ElfHeader<NATIVE_WORD> header) {
        return new TableHelper<NATIVE_WORD>(
                header.sectionHeaderTableOffset(),
                header.sectionHeaderSize(),
                header.numberOfSectionHeaders());
    }

    public static <NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
    TableHelper<NATIVE_WORD> forProgramHeaders(ElfHeader<NATIVE_WORD> header) {
        return new TableHelper<>(
                header.programHeaderTableOffset(),
                header.programHeaderSize(),
                header.numberOfProgramHeaders());
    }

    public static <NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
    TableHelper<NATIVE_WORD> forSectionEntries(ElfSectionHeader<NATIVE_WORD> sectionHeader) {
        return new TableHelper<>(
                sectionHeader.fileOffset(),
                Math.toIntExact(sectionHeader.containedEntrySize().longValue()),
                // TODO: Use actual section size
                Math.toIntExact(sectionHeader.size().longValue()) / Math.toIntExact(sectionHeader.containedEntrySize().longValue()));
    }

    private final ElfOffset<NATIVE_WORD> startOffset;
    private final int entrySize;
    private final int entriesCount;

    private TableHelper(ElfOffset<NATIVE_WORD> startOffset,
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

    public ElfOffset<NATIVE_WORD> offsetForEntry(int index) {
        if (index >= tableSize()) {
            throw new IndexOutOfBoundsException("Index is out of bounds: " + index);
        }

        return startOffset.plus(index * entrySize);
    }

    public ElfOffset<NATIVE_WORD> offsetForEntry(SectionHeaderIndex index) {
        return offsetForEntry(index.intValue());
    }

    public ElfOffset<NATIVE_WORD> offsetForEntry(SymbolTableIndex index) {
        return offsetForEntry(index.intValue());
    }
}

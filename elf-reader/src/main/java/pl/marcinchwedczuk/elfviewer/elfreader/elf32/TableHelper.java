package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

public class TableHelper {
    private final Elf32Offset startOffset;
    private final int entrySize;
    private final int entriesCount;

    public TableHelper(Elf32Offset startOffset,
                       int entrySize,
                       int entriesCount) {
        this.startOffset = startOffset;
        this.entrySize = entrySize;
        this.entriesCount = entriesCount;
    }

    public int tableSize() {
        return entriesCount;
    }

    public Elf32Offset offsetForEntry(int rawIndex) {
        if (rawIndex >= entriesCount) {
            // TODO: Better message
            throw new IndexOutOfBoundsException("Index is out of bounds: " + rawIndex);
        }

        long entryOffset = startOffset.longValue() + (long)rawIndex * entrySize;
        return new Elf32Offset(entryOffset);
    }

    public Elf32Offset offsetForEntry(SectionHeaderTableIndex index) {
        return offsetForEntry(index.intValue());
    }

    public Elf32Offset offsetForEntry(SymbolTableIndex index) {
        return offsetForEntry(index.intValue());
    }
}

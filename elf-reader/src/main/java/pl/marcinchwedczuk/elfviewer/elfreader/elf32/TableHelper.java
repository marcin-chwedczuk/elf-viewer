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

    public Elf32Offset offsetForEntry(SHTIndex index) {
        long rawIndex = index.intValue();
        if (rawIndex >= entriesCount) {
            // TODO: Better message
            throw new IndexOutOfBoundsException("Index is out of bounds: " + rawIndex);
        }

        long entryOffset = startOffset.longValue() + rawIndex * entrySize;
        return new Elf32Offset(entryOffset);
    }
}

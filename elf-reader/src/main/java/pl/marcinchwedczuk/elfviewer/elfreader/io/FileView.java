package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;

import static java.util.Objects.requireNonNull;

public final class FileView implements AbstractFile {
    private final AbstractFile file;
    private final long startOffset;
    private final long length;

    public FileView(AbstractFile file,
                    Elf32Offset start,
                    long length) {
        if (length < 0)
            throw new IllegalArgumentException("Length cannot be negative.");

        this.file = requireNonNull(file);
        this.startOffset = start.longValue();
        this.length = length;
    }

    public long length() {
        return length;
    }

    @Override
    public byte read(long offset) {
        checkOffset(offset, 1);
        return file.read(startOffset + offset);
    }

    @Override
    public byte[] read(long offset, int size) {
        checkOffset(offset, size);
        return file.read(startOffset + offset, size);
    }

    private void checkOffset(long offset, int size) {
        if (offset < 0 || offset+size > length)
            throw new IllegalArgumentException(String.format(
                    "Attempt to read data outside file view range, " +
                            "File view range is [%d, %d), requested range is [%d, %d).",
                    startOffset, startOffset + length, offset, offset + size));
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;

import static java.util.Objects.requireNonNull;

public final class FileView implements AbstractFile {
    private final AbstractFile file;
    private final long startOffset;
    private final long length;

    public FileView(AbstractFile file,
                    ElfOffset<?> start,
                    long length) {
        if (length < 0)
            throw new IllegalArgumentException("Length cannot be negative.");

        this.file = requireNonNull(file);
        this.startOffset = start.value().longValue();
        this.length = length;
    }

    public long startOffset() { return startOffset; }

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

    @Override
    public int readBuffer(long offsetInView, byte[] buffer) {
        if (offsetInView >= length)
            return -1;
        int nbytes = file.readBuffer(startOffset + offsetInView, buffer);
        return Math.min(nbytes, Math.toIntExact(length - offsetInView));
    }

    private void checkOffset(long offset, int size) {
        if (offset < 0 || offset+size > length)
            throw new IllegalArgumentException(String.format(
                    "Attempt to read data outside file view range, " +
                            "File view length is %d, requested range is [%d, %d).",
                    length, offset, offset + size));
    }
}

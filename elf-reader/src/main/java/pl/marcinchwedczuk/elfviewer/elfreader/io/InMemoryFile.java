package pl.marcinchwedczuk.elfviewer.elfreader.io;

import java.util.Arrays;

public class InMemoryFile implements AbstractFile {
    private final byte[] bytes;

    public InMemoryFile(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte read(long offset) {
        if (bytes.length <= offset)
            throw new RuntimeException(String.format(
                    "File size: %d. Requested range [%d, %d). Range is outside of the file.",
                    bytes.length, offset, offset + 1));

        // TODO: Overflow check
        return bytes[(int)offset];
    }

    @Override
    public byte[] read(long offset, int size) {
        if (bytes.length < offset + size)
            throw new RuntimeException(String.format(
                    "File size: %d. Requested range [%d, %d). Range is outside of the file.",
                    bytes.length, offset, offset + size));

        // TODO: Add checked conversion long -> int
        return Arrays.copyOfRange(bytes, (int)offset, (int)offset + size);
    }

    @Override
    public int readIntoBuffer(long offset, byte[] buffer) {
        if (offset >= bytes.length)
            return -1;

        int bytesToCopy = Math.min(buffer.length, Math.toIntExact(bytes.length - offset));
        System.arraycopy(bytes, Math.toIntExact(offset), buffer, 0, bytesToCopy);
        return bytesToCopy;
    }
}

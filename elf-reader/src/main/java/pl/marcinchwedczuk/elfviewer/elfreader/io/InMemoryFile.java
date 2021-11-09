package pl.marcinchwedczuk.elfviewer.elfreader.io;

import java.util.Arrays;

public class InMemoryFile implements AbstractFile {
    private final byte[] bytes;

    public InMemoryFile(byte[] bytes) {
        this.bytes = bytes;
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
}

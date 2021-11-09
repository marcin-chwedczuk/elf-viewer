package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public interface AbstractFile {
    byte[] read(long offset, int size);

    default short readUnsignedShort(Endianness endianness, long offset) {
        byte[] bytes = read(offset, 2);
        return endianness.toUnsignedShort(bytes);
    }

    default int readUnsignedInt(Endianness endianness, int offset) {
        byte[] bytes = read(offset, 4);
        return endianness.toUnsignedInt(bytes);
    }
}

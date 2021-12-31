package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public interface AbstractFile {
    byte read(long offset);
    byte[] read(long offset, int size);

    /**
     * @param buffer data will be written here.
     * @return number of bytes read into buffer,
     *          -1 when end of file is reached.
     */
    int readBuffer(long offset, byte[] buffer);

    default short readUnsignedShort(Endianness endianness, long offset) {
        byte[] bytes = read(offset, 2);
        return endianness.toUnsignedShort(bytes);
    }

    default int readUnsignedInt(Endianness endianness, long offset) {
        byte[] bytes = read(offset, 4);
        return endianness.toUnsignedInt(bytes);
    }

    default long readUnsignedLong(Endianness endianness, long offset) {
        byte[] bytes = read(offset, 8);
        return endianness.toUnsignedLong(bytes);
    }
}

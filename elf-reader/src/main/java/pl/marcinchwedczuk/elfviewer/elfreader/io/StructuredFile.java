package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFile {
    private final AbstractFile file;
    private final Endianness endianness;

    private long offset;

    public StructuredFile(AbstractFile file, Endianness endianness, long initialOffset) {
        this.file = file;
        this.endianness = endianness;
        this.offset = initialOffset;
    }

    private byte[] readNext(int nbytes) {
        byte[] bytes = file.read(offset, 4);
        offset += bytes.length;
        return bytes;
    }


    public Elf32Address readAddress() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new Elf32Address(address);
    }

    public short readUnsignedShort() {
        short half = file.readUnsignedShort(endianness, offset);
        offset += 2;
        return half;
    }

    public Elf32Offset readOffset() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new Elf32Offset(address);
    }

    public int readUnsignedInt() {
        int word = file.readUnsignedInt(endianness, offset);
        offset += 4;
        return word;
    }

    public int readSignedInt() {
        // In Java we only have signed types...
        return readUnsignedInt();
    }

    public byte readByte() {
        byte[] bytes = readNext(1);
        return bytes[0];
    }
}

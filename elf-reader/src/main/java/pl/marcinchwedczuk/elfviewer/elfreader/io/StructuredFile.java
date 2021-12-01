package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import java.nio.charset.StandardCharsets;

public class StructuredFile {
    private final AbstractFile file;
    private final Endianness endianness;

    private long offset;

    public StructuredFile(AbstractFile file, Endianness endianness) {
        this(file, endianness, 0L);
    }

    public StructuredFile(AbstractFile file, Endianness endianness, long initialOffset) {
        this.file = file;
        this.endianness = endianness;
        this.offset = initialOffset;
    }

    public StructuredFile(AbstractFile file, Endianness endianness, Elf32Offset offset) {
        this(file, endianness, offset.longValue());
    }

    public StructuredFile(Elf32File file, Elf32Offset offset) {
        this(file.storage(), file.endianness(), offset);
    }

    public Elf32Offset currentPositionInFile() { return new Elf32Offset(offset); }

    private byte[] readNext(int nbytes) {
        byte[] bytes = file.read(offset, nbytes);
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

    public String readStringNullTerminated() {
        // TODO: Make it better
        return readStringNullTerminatedWithAlignment(1);
    }

    public String readStringNullTerminatedWithAlignment(int alignment) {
        ByteList buffer = new ByteList();

        while(true) {
            byte b = readByte();
            if (b == 0) break;

            buffer.add(b);
        }

        int bytesRead = buffer.size() + 1; // +1 for NUL
        readUpToAlignment(bytesRead, alignment);

        return buffer.toAsciiString();
    }

    public String readFixedSizeStringWithAlignment(int stringLength, int alignment) {
        byte[] bytes = readFixedSizeByteArrayWithAlignment(stringLength, alignment);
        if (bytes.length == 0) {
            return "";
        }

        // The last byte should be 0 - we just skip it here
        // TODO: Think about handling malformed strings
        // TODO: Think about invalid string lengths that stick out after the sections end
        return new String(bytes, 0, bytes.length - 1, StandardCharsets.US_ASCII);
    }

    public byte[] readFixedSizeByteArrayWithAlignment(int nbytes, int alignment) {
        if (nbytes == 0) {
            // Current position should be already aligned
            // TODO: Verify above
            return new byte[] { };
        }

        byte[] bytes = readNext(nbytes);
        readUpToAlignment(bytes.length, alignment);
        return bytes;
    }

    private void readUpToAlignment(int bytesRead, int alignment) {
        while ((bytesRead % alignment) != 0) {
            bytesRead++;
            readByte();
        }
    }

    public int[] readIntArray(int size) {
        if (size < 0) throw new IllegalArgumentException();

        int[] result = new int[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = readUnsignedInt();
        }
        return result;
    }
}

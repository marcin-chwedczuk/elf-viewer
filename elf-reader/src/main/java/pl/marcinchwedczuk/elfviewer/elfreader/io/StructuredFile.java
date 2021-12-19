package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import java.nio.charset.StandardCharsets;

public abstract class StructuredFile<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    protected final AbstractFile file;
    protected final Endianness endianness;

    protected long offset;

    public StructuredFile(AbstractFile file, Endianness endianness) {
        this(file, endianness, 0L);
    }

    public StructuredFile(AbstractFile file, Endianness endianness, long initialOffset) {
        this.file = file;
        this.endianness = endianness;
        this.offset = initialOffset;
    }

    public StructuredFile(AbstractFile file, Endianness endianness, ElfOffset<NATIVE_WORD> offset) {
        this(file, endianness, offset.value().longValue());
    }

    public StructuredFile(ElfFile<NATIVE_WORD> file, ElfOffset<NATIVE_WORD> offset) {
        this(file.storage(), file.endianness(), offset);
    }

    protected abstract ElfOffset<NATIVE_WORD> mkOffset(long offset);

    public abstract ElfOffset<NATIVE_WORD> readOffset();
    public abstract ElfAddress<NATIVE_WORD> readAddress();

    public ElfOffset<NATIVE_WORD> currentPositionInFile() { return mkOffset(offset); }

    protected byte[] readNext(int nbytes) {
        byte[] bytes = file.read(offset, nbytes);
        offset += bytes.length;
        return bytes;
    }

    public ElfAddress<Long> readAddress64() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new ElfAddress<>(address);
    }

    public short readUnsignedShort() {
        short half = file.readUnsignedShort(endianness, offset);
        offset += 2;
        return half;
    }

    public ElfOffset<Long> readOffset64() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new ElfOffset<>(address);
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

    public long readUnsignedLong() {
        long xword = file.readUnsignedLong(endianness, offset);
        offset += 8;
        return xword;
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

    public long[] readLongArray(int size) {
        if (size < 0) throw new IllegalArgumentException();

        long[] result = new long[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = readUnsignedLong();
        }
        return result;
    }
}

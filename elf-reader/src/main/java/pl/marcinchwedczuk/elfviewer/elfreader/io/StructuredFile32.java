package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFile32 extends StructuredFile<Integer> {
    public StructuredFile32(AbstractFile file, Endianness endianness) {
        super(file, endianness);
    }

    public StructuredFile32(AbstractFile file, Endianness endianness, long initialOffset) {
        super(file, endianness, initialOffset);
    }

    public StructuredFile32(AbstractFile file, Endianness endianness, ElfOffset<Integer> offset) {
        super(file, endianness, offset);
    }

    public StructuredFile32(ElfFile<Integer> file, ElfOffset<Integer> offset) {
        super(file, offset);
    }

    @Override
    protected ElfOffset<Integer> mkOffset(long offset) {
        return new ElfOffsetAny<>(Math.toIntExact(offset));
    }

    @Override
    public ElfOffset<Integer> readOffset() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new ElfOffsetAny<>(address);
    }

    @Override
    public ElfAddress<Integer> readAddress() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new ElfAddress<>(address);
    }
}

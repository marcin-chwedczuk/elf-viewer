package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFile64 extends StructuredFile<Long> {
    public StructuredFile64(AbstractFile file, Endianness endianness) {
        super(file, endianness);
    }

    public StructuredFile64(AbstractFile file, Endianness endianness, long initialOffset) {
        super(file, endianness, initialOffset);
    }

    public StructuredFile64(AbstractFile file, Endianness endianness, ElfOffset<Long> offset) {
        super(file, endianness, offset);
    }

    public StructuredFile64(ElfFile<Long> file, ElfOffset<Long> offset) {
        super(file, offset);
    }

    @Override
    protected ElfOffset<Long> mkOffset(long offset) {
        return new ElfOffset<>(offset);
    }

    @Override
    public ElfOffset<Long> readOffset() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new ElfOffset<>(address);
    }

    @Override
    public ElfAddress<Long> readAddress() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new ElfAddress<>(address);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFile64 extends StructuredFile<Long> {
    public StructuredFile64(AbstractFile file, Endianness endianness) {
        super(file, endianness);
    }

    public StructuredFile64(AbstractFile file, Endianness endianness, long initialOffset) {
        super(file, endianness, initialOffset);
    }

    public StructuredFile64(AbstractFile file, Endianness endianness, Elf64Offset offset) {
        super(file, endianness, offset);
    }

    public StructuredFile64(Elf64File file, Elf64Offset offset) {
        super(file, offset);
    }

    @Override
    protected ElfOffset<Long> mkOffset(long offset) {
        return new Elf64Offset(Math.toIntExact(offset));
    }

    @Override
    public Elf64Offset readOffset() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new Elf64Offset(address);
    }

    @Override
    public Elf64Address readAddress() {
        byte[] addressBytes = readNext(8);
        long address = endianness.toUnsignedLong(addressBytes);
        return new Elf64Address(address);
    }

    @Override
    public Elf64Offset currentPositionInFile() {
        return (Elf64Offset) super.currentPositionInFile();
    }
}

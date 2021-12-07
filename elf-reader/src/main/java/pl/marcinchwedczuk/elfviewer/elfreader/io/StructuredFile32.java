package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
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
        return new Elf32Offset(Math.toIntExact(offset));
    }

    @Override
    public Elf32Offset readOffset() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new Elf32Offset(address);
    }

    @Override
    public Elf32Address readAddress() {
        byte[] addressBytes = readNext(4);
        int address = endianness.toUnsignedInt(addressBytes);
        return new Elf32Address(address);
    }

    @Override
    public Elf32Offset currentPositionInFile() {
        return (Elf32Offset) super.currentPositionInFile();
    }
}

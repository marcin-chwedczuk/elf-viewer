package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFileFactory32 implements StructuredFileFactory<Integer> {
    @Override
    public StructuredFile<Integer> mkStructuredFile(AbstractFile file, Endianness endianness) {
        return new StructuredFile32(file, endianness);
    }

    @Override
    public StructuredFile<Integer> mkStructuredFile(AbstractFile file, Endianness endianness, long initialOffset) {
        return new StructuredFile32(file, endianness, initialOffset);
    }

    @Override
    public StructuredFile<Integer> mkStructuredFile(AbstractFile file, Endianness endianness, ElfOffset<Integer> offset) {
        return new StructuredFile32(file, endianness, offset);
    }

    @Override
    public StructuredFile<Integer> mkStructuredFile(ElfFile<Integer> file, ElfOffset<Integer> offset) {
        return new StructuredFile32(file, offset);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

public class StructuredFileFactory64 implements StructuredFileFactory<Long> {
    @Override
    public StructuredFile<Long> mkStructuredFile(AbstractFile file, Endianness endianness) {
        return new StructuredFile64(file, endianness);
    }

    @Override
    public StructuredFile<Long> mkStructuredFile(AbstractFile file, Endianness endianness, long initialOffset) {
        return new StructuredFile64(file, endianness, initialOffset);
    }

    @Override
    public StructuredFile<Long> mkStructuredFile(AbstractFile file, Endianness endianness, ElfOffset<Long> offset) {
        return new StructuredFile64(file, endianness, (Elf64Offset) offset);
    }

    @Override
    public StructuredFile<Long> mkStructuredFile(ElfFile<Long> file, ElfOffset<Long> offset) {
        return new StructuredFile64(file, offset);
    }
}

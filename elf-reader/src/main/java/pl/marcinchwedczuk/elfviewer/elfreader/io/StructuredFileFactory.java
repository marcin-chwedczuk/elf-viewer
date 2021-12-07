package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

// TODO: change name to FileParser
public interface StructuredFileFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    StructuredFile<NATIVE_WORD> mkStructuredFile(AbstractFile file, Endianness endianness);
    StructuredFile<NATIVE_WORD> mkStructuredFile(AbstractFile file, Endianness endianness, long initialOffset);
    StructuredFile<NATIVE_WORD> mkStructuredFile(AbstractFile file, Endianness endianness, ElfOffset<NATIVE_WORD> offset);
    StructuredFile<NATIVE_WORD> mkStructuredFile(ElfFile<NATIVE_WORD> file, ElfOffset<NATIVE_WORD> offset);
}

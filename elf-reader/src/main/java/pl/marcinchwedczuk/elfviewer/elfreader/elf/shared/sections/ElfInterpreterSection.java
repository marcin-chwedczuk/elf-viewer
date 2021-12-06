package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class ElfInterpreterSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfInterpreterSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory, ElfFile<NATIVE_WORD> elfFile, ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);


        Args.checkSectionType(header, PROGBITS);

        if (!header.hasName(INTERP))
            throw new IllegalArgumentException("Invalid section name: " + header.name() + ".");
    }

    public String interpreterPath() {
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory
                .mkStructuredFile(contents(), elfFile().endianness());

        return sf.readStringNullTerminated();
    }
}

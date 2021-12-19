package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.GNU_WARNING_PREFIX;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.PROGBITS;

public class ElfGnuWarningSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {

    public ElfGnuWarningSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory, ElfFile<NATIVE_WORD> elfFile, ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, PROGBITS);

        if (!header.hasNameStartingWith(GNU_WARNING_PREFIX))
            throw new IllegalArgumentException("Invalid section name: " + header.name() + ".");
    }

    public String warning() {
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory
                .mkStructuredFile(contents(), elfFile().endianness());
        return sf.readStringNullTerminated();
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

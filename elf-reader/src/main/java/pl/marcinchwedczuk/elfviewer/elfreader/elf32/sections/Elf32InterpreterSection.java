package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class Elf32InterpreterSection extends Elf32Section {
    public Elf32InterpreterSection(Elf32File elfFile,
                                   Elf32SectionHeader header) {
        super(elfFile, header);

        Args.checkSectionType(header, PROGBITS);

        if (!header.hasName(INTERP))
            throw new IllegalArgumentException("Invalid section name: " + header.name() + ".");
    }

    public String interpreterPath() {
        StructuredFile sf = new StructuredFile(
                contents(),
                elfFile().endianness());

        return sf.readStringNullTerminated();
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class Elf32InvalidSection extends Elf32Section {
    private final Exception error;

    public Elf32InvalidSection(Elf32File elfFile,
                               Elf32SectionHeader header,
                               Exception error) {
        super(elfFile, header);
        this.error = error;
    }

    public Exception error() {
        return error;
    }
}

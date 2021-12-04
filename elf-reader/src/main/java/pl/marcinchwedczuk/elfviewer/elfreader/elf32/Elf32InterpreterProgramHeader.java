package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SegmentType.INTERPRETER;

public class Elf32InterpreterProgramHeader {
    private final Elf32File elfFile;
    private final Elf32Offset startOffset;

    public Elf32InterpreterProgramHeader(
            Elf32File elfFile,
            Elf32ProgramHeader programHeader) {
        if (programHeader.type().isNot(INTERPRETER))
            throw new IllegalArgumentException("programHeader");

        this.elfFile = elfFile;
        this.startOffset = programHeader.fileOffset();
    }

    public Elf32InterpreterProgramHeader(
            Elf32File elfFile,
            Elf32SectionHeader section) {

        if (!section.hasName(ElfSectionNames.INTERP)) {
            throw new IllegalArgumentException("TODO");
        }

        this.elfFile = elfFile;
        this.startOffset = section.fileOffset();
    }

    public String getInterpreterPath() {
        StructuredFile32 sf = new StructuredFile32(
                elfFile.storage(),
                elfFile.endianness(),
                startOffset);

        // TODO: Add max string length
        return sf.readStringNullTerminated();
    }
}

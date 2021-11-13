package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public class Elf32InterpreterProgramHeader {
    private final Elf32File elfFile;
    private final Elf32ProgramHeader programHeader;

    public Elf32InterpreterProgramHeader(
            Elf32File elfFile,
            Elf32ProgramHeader programHeader) {
        if (!programHeader.type().equals(Elf32SegmentType.Interpreter))
            throw new IllegalArgumentException("programHeader");

        this.elfFile = elfFile;
        this.programHeader = programHeader;
    }

    public String getInterpreterPath() {
        StructuredFile sf = new StructuredFile(
                elfFile.storage,
                elfFile.endianness,
                programHeader.fileOffset());

        // TODO: Add max string length
        return sf.readStringNullTerminated();
    }
}

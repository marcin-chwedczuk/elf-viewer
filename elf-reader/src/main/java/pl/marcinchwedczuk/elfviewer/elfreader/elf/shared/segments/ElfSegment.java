package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ElfSegment<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final ElfFile<NATIVE_WORD> elfFile;
    private final ElfProgramHeader<NATIVE_WORD> programHeader;
    private final List<ElfSection<NATIVE_WORD>> sections;

    public ElfSegment(ElfFile<NATIVE_WORD> elfFile,
                        ElfProgramHeader<NATIVE_WORD> programHeader,
                        List<ElfSection<NATIVE_WORD>> sections) {
        this.elfFile = elfFile;
        this.programHeader = requireNonNull(programHeader);
        this.sections = new ArrayList<>(sections);
    }

    public ElfProgramHeader<NATIVE_WORD> programHeader() {
        return programHeader;
    }

    public List<ElfSection<NATIVE_WORD>> containedSections() {
        return Collections.unmodifiableList(sections);
    }

    public FileView contents() {
        return new FileView(
                elfFile.storage(),
                programHeader.fileOffset(),
                programHeader.fileSize().longValue());
    }
}

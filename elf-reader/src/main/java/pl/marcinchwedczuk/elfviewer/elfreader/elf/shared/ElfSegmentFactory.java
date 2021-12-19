package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ElfSegmentFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final ElfFile<NATIVE_WORD> elfFile;

    public ElfSegmentFactory(ElfFile<NATIVE_WORD> elfFile) {
        this.elfFile = Objects.requireNonNull(elfFile);
    }

    public List<ElfSegment<NATIVE_WORD>> createSegments() {
        List<ElfSegment<NATIVE_WORD>> segments = new ArrayList<>();

        for (ElfProgramHeader<NATIVE_WORD> programHeader : elfFile.programHeaders()) {
            List<ElfSection<NATIVE_WORD>> containedSections = elfFile.sections().stream()
                    .filter(s -> s.header().type().isNot(ElfSectionType.NULL)
                            && programHeader.containsSection(s.header()))
                    .collect(toList());

            segments.add(new ElfSegment<>(elfFile, programHeader, containedSections));
        }

        return segments;
    }
}

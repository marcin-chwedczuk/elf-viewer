package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ElfSegmentFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    public List<ElfSegment<NATIVE_WORD>> createSegments(ElfFile<NATIVE_WORD> elfFile) {
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

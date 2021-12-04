package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ElfSegmentFactory {
    private final Elf32File elfFile;

    public ElfSegmentFactory(Elf32File elfFile) {
        this.elfFile = Objects.requireNonNull(elfFile);
    }

    public List<Elf32Segment> createSegments() {
        List<Elf32Segment> segments = new ArrayList<>();

        for (Elf32ProgramHeader programHeader : elfFile.programHeaders) {
            List<Elf32BasicSection> containedSections = elfFile.sections().stream()
                    .filter(s -> s.header().type().isNot(ElfSectionType.NULL)
                            && programHeader.containsSection(s.header()))
                    .collect(toList());

            segments.add(new Elf32Segment(elfFile, programHeader, containedSections));
        }

        return segments;
    }
}

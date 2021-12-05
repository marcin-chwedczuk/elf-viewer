package pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Elf64Segment {
    // TODO: Inject
    private final Elf64SectionFactory sectionFactory = new Elf64SectionFactory();

    private final ElfSegment<Long> segment;

    public Elf64Segment(ElfSegment<Long> segment) {
        this.segment = requireNonNull(segment);
    }

    public Elf64ProgramHeader programHeader() {
        return new Elf64ProgramHeader(segment.programHeader());
    }

    public List<Elf64Section> containedSections() {
        return segment.containedSections().stream()
                .map(sectionFactory::wrap)
                .collect(toList());
    }

    public FileView contents() {
        return segment.contents();
    }
}

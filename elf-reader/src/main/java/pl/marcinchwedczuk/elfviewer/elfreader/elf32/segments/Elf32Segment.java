package pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Elf32Segment implements Elf32Visitable {
    // TODO: Inject
    private final Elf32SectionFactory sectionFactory = new Elf32SectionFactory();

    private final ElfSegment<Integer> segment;

    public Elf32Segment(ElfSegment<Integer> segment) {
        this.segment = requireNonNull(segment);
    }

    public Elf32ProgramHeader programHeader() {
        return new Elf32ProgramHeader(segment.programHeader());
    }

    public List<Elf32BasicSection> containedSections() {
        return segment.containedSections().stream()
                .map(sectionFactory::wrap)
                .collect(toList());
    }

    public FileView contents() {
        return segment.contents();
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

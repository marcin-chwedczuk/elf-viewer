package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32SegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Elf32File
        implements Elf32Visitable
{
    private final Elf32SectionFactory sectionFactory = new Elf32SectionFactory();
    private final Elf32SegmentFactory segmentFactory = new Elf32SegmentFactory();

    private final ElfFile<Integer> elfFile;
    public ElfFile<Integer> unwrap() { return elfFile; }

    public Elf32File(ElfFile<Integer> elfFile) {
        this.elfFile = requireNonNull(elfFile);
    }

    public Elf32Header header() { return new Elf32Header(elfFile.header()); }
    public final AbstractFile storage() { return elfFile.storage(); }
    public final Endianness endianness() { return elfFile.endianness(); }

    public List<Elf32SectionHeader> sectionHeaders() {
        return elfFile.sectionHeaders().stream()
                .map(Elf32SectionHeader::new)
                .collect(toList());
    }

    public List<Elf32BasicSection> sections() {
        return sectionFactory.wrap(elfFile.sections());
    }

    public Optional<Elf32BasicSection> sectionWithName(String name) {
        return elfFile.sectionWithName(name)
                .map(sectionFactory::wrap);
    }

    public List<Elf32Segment> segments() {
        return elfFile.segments().stream()
                .map(segmentFactory::wrap)
                .collect(toList());
    }

    public Optional<Elf32SectionHeader> getSectionHeader(String sectionName) {
        return elfFile
                .getSectionHeader(sectionName)
                .map(Elf32SectionHeader::new);
    }

    public List<Elf32ProgramHeader> getProgramHeadersOfType(Elf32SegmentType segmentType) {
        return elfFile.getProgramHeadersOfType(segmentType).stream()
                .map(Elf32ProgramHeader::new)
                .collect(toList());
    }

    public Optional<Elf32BasicSection> sectionContainingAddress(Elf32Address inMemoryAddress) {
        return elfFile
                .sectionContainingAddress(inMemoryAddress)
                .map(sectionFactory::wrap);
    }

    public Optional<Elf32Segment> segmentContainingAddress(Elf32Address inMemoryAddress) {
        return elfFile
                .segmentContainingAddress(inMemoryAddress)
                .map(segmentFactory::wrap);
    }

    public Optional<Elf32Offset> virtualAddressToFileOffset(Elf32Address addr) {
        return elfFile
                .virtualAddressToFileOffset(addr)
                .map(Elf32Offset::new);
    }

    public List<Elf32Segment> segmentsOfType(Elf32SegmentType type) {
        return elfFile.segmentsOfType(type).stream()
                .map(segmentFactory::wrap)
                .collect(toList());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        header().accept(visitor);

        visitor.enterSections();
        for (Elf32BasicSection section : sections()) {
            section.accept(visitor);
        }
        visitor.exitSections();

        visitor.enterSegments();
        for (Elf32Segment segment : segments()) {
            segment.accept(visitor);
        }
        visitor.exitSegments();
    }

    public List<Elf32BasicSection> sectionsOfType(ElfSectionType type) {
        return elfFile.sectionsOfType(type)
                // TODO: Map though factory to map sections to their types
                .stream().map(sectionFactory::wrap)
                .collect(toList());
    }

    public Optional<Elf32BasicSection> sectionOfType(ElfSectionType type) {
        return elfFile.sectionOfType(type)
                // TODO: Map though factory to map sections to their types
                .map(sectionFactory::wrap);
    }
}

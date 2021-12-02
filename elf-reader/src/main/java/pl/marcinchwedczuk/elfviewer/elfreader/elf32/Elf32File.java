package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32SegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Elf32File extends ElfFile implements Elf32Visitable {
    private final AbstractFile storage;
    private final Endianness endianness;
    private final Elf32Header header;

    public final Memoized<List<Elf32Section>> sectionsMemoized = new Memoized<>(() ->
            new Elf32SectionFactory(this).createSections());

    public final Memoized<List<Elf32Segment>> segmentsMemoized = new Memoized<>(() ->
            new Elf32SegmentFactory(this).createSegments());

    /**
     * The section header for index 0 (SHN_UNDEF) _always_ exists,
     * even though the index marks undefined section references.
     */
    public final List<Elf32SectionHeader> sectionHeaders;
    public final List<Elf32ProgramHeader> programHeaders;

    public Elf32File(AbstractFile storage,
                     Endianness endianness,
                     Elf32Header header,
                     List<Elf32SectionHeader> sectionHeaders,
                     List<Elf32ProgramHeader> programHeaders) {
        this.storage = storage;
        this.endianness = endianness;
        this.header = header;
        this.sectionHeaders = sectionHeaders;
        this.programHeaders = programHeaders;

        // Fix circular references
        this.sectionHeaders.forEach(sh -> sh.setElfFile(this));
    }

    public Elf32Header header() {
        return header;
    }

    public AbstractFile storage() {
        return storage;
    }

    public Endianness endianness() {
        return endianness;
    }

    public List<Elf32Section> sections() {
        return sectionsMemoized.get();
    }

    public List<Elf32Segment> segments() {
        return segmentsMemoized.get();
    }

    public Optional<Elf32SectionHeader> getSectionHeader(String sectionName) {
        Optional<Elf32SectionHeader> maybeSymbolTableSection = sectionHeaders.stream()
                .filter(s -> s.name().equals(sectionName))
                .findFirst();

        return maybeSymbolTableSection;
    }

    public List<Elf32ProgramHeader> getProgramHeadersOfType(Elf32SegmentType segmentType) {
        List<Elf32ProgramHeader> programHeaders = this.programHeaders.stream()
                .filter(ph -> ph.type().equals(segmentType))
                .collect(toList());

        return programHeaders;
    }

    public Optional<Elf32Section> sectionContainingAddress(Elf32Address inMemoryAddress) {
        for (Elf32Section section: sections()) {
            Elf32SectionHeader header = section.header();

            if (inMemoryAddress.isAfterOrAt(header.virtualAddress()) &&
                    inMemoryAddress.isBefore(header.endVirtualAddress())) {
                return Optional.of(section);
            }
        }

        return Optional.empty();
    }

    public Optional<Elf32Segment> segmentContainingAddress(Elf32Address inMemoryAddress) {
        for (Elf32Segment segment: segments()) {
            Elf32ProgramHeader ph = segment.programHeader();

            if (inMemoryAddress.isAfterOrAt(ph.virtualAddress()) &&
                    inMemoryAddress.isBefore(ph.endVirtualAddressInFile())) {
                return Optional.of(segment);
            }
        }

        return Optional.empty();
    }

    public Optional<Elf32Offset> virtualAddressToFileOffset(Elf32Address addr) {
        Optional<Elf32Segment> maybeSegment = segmentContainingAddress(addr);

        return maybeSegment.map(segment -> {
            long offset = addr.minus(segment.programHeader().virtualAddress());

            return segment.programHeader().fileOffset()
                    .plus(offset);
        });
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        header.accept(visitor);

        visitor.enterSections();
        for (Elf32Section section : sections()) {
            section.accept(visitor);
        }
        visitor.exitSections();

        visitor.enterSegments();
        for (Elf32Segment segment : segments()) {
            segment.accept(visitor);
        }
        visitor.exitSegments();
    }

    public List<Elf32Section> sectionsOfType(ElfSectionType type) {
        return sections().stream()
                .filter(s -> s.header().type().is(type))
                .collect(toList());
    }

    public Optional<Elf32Section> sectionOfType(ElfSectionType type) {
        List<Elf32Section> sections = sectionsOfType(type);

        if (sections.size() > 1)
            throw new ElfReaderException("More than one section has type " + type + ".");

        return sections.size() == 1
                ? Optional.of(sections.get(0))
                : Optional.empty();
    }

    public Optional<Elf32Section> sectionWithName(String name) {
        return sections().stream()
                .filter(s -> Objects.equals(name, s.header().name()))
                .findFirst();
    }

    public List<Elf32Segment> segmentsOfType(Elf32SegmentType type) {
        return segments().stream()
                .filter(s -> s.programHeader().type().is(type))
                .collect(toList());
    }
}

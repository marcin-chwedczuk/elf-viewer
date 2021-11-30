package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32SegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Elf32File extends Elf32Element {
    public final AbstractFile storage;
    public final Endianness endianness;
    public final Elf32Header header;

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

    public Optional<Elf32Section> findSection(ElfSectionType type) {
        return sections().stream()
                .filter(s -> s.header().type().is(type))
                .findFirst();
    }
}

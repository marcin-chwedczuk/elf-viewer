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

    private final ElfFile<Integer> elfFile;
    public ElfFile<Integer> rawElfFile() { return elfFile; }

    public final Memoized<List<Elf32Segment>> segmentsMemoized = new Memoized<>(() ->
            new Elf32SegmentFactory(this).createSegments());

   public final List<Elf32ProgramHeader> programHeaders;

    public Elf32File(ElfFile<Integer> elfFile,
                     List<Elf32ProgramHeader> programHeaders) {
        this.elfFile = requireNonNull(elfFile);
        this.programHeaders = programHeaders;
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
        return segmentsMemoized.get();
    }

    public Optional<Elf32SectionHeader> getSectionHeader(String sectionName) {
        Optional<Elf32SectionHeader> maybeSymbolTableSection = sectionHeaders().stream()
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

    public Optional<Elf32BasicSection> sectionContainingAddress(Elf32Address inMemoryAddress) {
        for (Elf32BasicSection section: sections()) {
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

    public List<Elf32Segment> segmentsOfType(Elf32SegmentType type) {
        return segments().stream()
                .filter(s -> s.programHeader().type().is(type))
                .collect(toList());
    }
}

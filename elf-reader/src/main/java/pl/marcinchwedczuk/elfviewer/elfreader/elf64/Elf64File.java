package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SegmentType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments.Elf64ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments.Elf64Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments.Elf64SegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Elf64File {
    private final Elf64SectionFactory sectionFactory = new Elf64SectionFactory();
    private final Elf64SegmentFactory segmentFactory = new Elf64SegmentFactory();

    private final ElfFile<Long> elfFile;
    public ElfFile<Long> unwrap() { return elfFile; }

    public Elf64File(ElfFile<Long> elfFile) {
        this.elfFile = requireNonNull(elfFile);
    }

    public Elf64Header header() { return new Elf64Header(elfFile.header()); }
    public final AbstractFile storage() { return elfFile.storage(); }
    public final Endianness endianness() { return elfFile.endianness(); }

    public List<Elf64SectionHeader> sectionHeaders() {
        return elfFile.sectionHeaders().stream()
                .map(Elf64SectionHeader::new)
                .collect(toList());
    }

    public List<Elf64Section> sections() {
        return sectionFactory.wrap(elfFile.sections());
    }

    public Optional<Elf64Section> sectionWithName(String name) {
        return elfFile.sectionWithName(name)
                .map(sectionFactory::wrap);
    }

    public List<Elf64Segment> segments() {
        return elfFile.segments().stream()
                .map(segmentFactory::wrap)
                .collect(toList());
    }

    public Optional<Elf64SectionHeader> getSectionHeader(String sectionName) {
        return elfFile
                .getSectionHeader(sectionName)
                .map(Elf64SectionHeader::new);
    }

    public List<Elf64ProgramHeader> getProgramHeadersOfType(Elf32SegmentType segmentType) {
        return elfFile.getProgramHeadersOfType(segmentType).stream()
                .map(Elf64ProgramHeader::new)
                .collect(toList());
    }

    public Optional<Elf64Section> sectionContainingAddress(Elf64Address inMemoryAddress) {
        return elfFile
                .sectionContainingAddress(inMemoryAddress)
                .map(sectionFactory::wrap);
    }

    public Optional<Elf64Segment> segmentContainingAddress(Elf64Address inMemoryAddress) {
        return elfFile
                .segmentContainingAddress(inMemoryAddress)
                .map(segmentFactory::wrap);
    }

    public Optional<Elf64Offset> virtualAddressToFileOffset(Elf64Address addr) {
        return elfFile
                .virtualAddressToFileOffset(addr)
                .map(Elf64Offset::new);
    }

    public List<Elf64Segment> segmentsOfType(Elf32SegmentType type) {
        return elfFile.segmentsOfType(type).stream()
                .map(segmentFactory::wrap)
                .collect(toList());
    }

    public List<Elf64Section> sectionsOfType(ElfSectionType type) {
        return elfFile.sectionsOfType(type)
                // TODO: Map though factory to map sections to their types
                .stream().map(sectionFactory::wrap)
                .collect(toList());
    }

    public Optional<Elf64Section> sectionOfType(ElfSectionType type) {
        return elfFile.sectionOfType(type)
                // TODO: Map though factory to map sections to their types
                .map(sectionFactory::wrap);
    }
}

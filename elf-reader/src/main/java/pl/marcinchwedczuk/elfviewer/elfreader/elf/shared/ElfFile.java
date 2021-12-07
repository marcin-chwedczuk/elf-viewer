package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ElfFile<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
    extends ElfElement<NATIVE_WORD>
{
    private final NativeWord<NATIVE_WORD> nativeWord;

    private final AbstractFile storage;
    private final Endianness endianness;

    private final ElfHeader<NATIVE_WORD> header;

    /**
     * The section header for index 0 (SHN_UNDEF) _always_ exists,
     * even though the index marks undefined section references.
     */
    private final List<ElfSectionHeader<NATIVE_WORD>> sectionHeaders;
    private final Memoized<List<ElfSection<NATIVE_WORD>>> sectionsMemoized;

    private final List<ElfProgramHeader<NATIVE_WORD>> programHeaders;
    private final Memoized<List<ElfSegment<NATIVE_WORD>>> segmentsMemoized;

    public ElfFile(NativeWord<NATIVE_WORD> nativeWord,
                   AbstractFile storage,
                   Endianness endianness,
                   ElfHeader<NATIVE_WORD> header,
                   List<? extends ElfSectionHeader<NATIVE_WORD>> sectionHeaders,
                   ElfSectionFactory<NATIVE_WORD> sectionFactory,
                   List<? extends ElfProgramHeader<NATIVE_WORD>> programHeaders,
                   ElfSegmentFactory<NATIVE_WORD> segmentFactory) {
        this.nativeWord = requireNonNull(nativeWord);
        this.storage = requireNonNull(storage);
        this.endianness = requireNonNull(endianness);

        this.header = requireNonNull(header);

        this.sectionHeaders = new ArrayList<>(sectionHeaders);
        this.sectionsMemoized = new Memoized<>(
                () -> sectionFactory.createSections(this));

        this.programHeaders = new ArrayList<>(programHeaders);
        this.segmentsMemoized = new Memoized<>(
                () -> segmentFactory.createSegments(this));
    }

    public final NativeWord<NATIVE_WORD> nativeWordMetadata() { return nativeWord; }

    public final AbstractFile storage() {
        return storage;
    }

    public final Endianness endianness() {
        return endianness;
    }

    public ElfHeader<NATIVE_WORD> header() {
        return header;
    }

    public List<ElfSectionHeader<NATIVE_WORD>> sectionHeaders() {
        return Collections.unmodifiableList(sectionHeaders);
    }

    public List<ElfSection<NATIVE_WORD>> sections() {
        return sectionsMemoized.get();
    }

    public List<ElfProgramHeader<NATIVE_WORD>> programHeaders() {return programHeaders;}
    public List<ElfSegment<NATIVE_WORD>> segments() {
        return segmentsMemoized.get();
    }

    public Optional<ElfSection<NATIVE_WORD>> sectionWithName(String name) {
        return sections().stream()
                .filter(s -> Objects.equals(name, s.header().name()))
                .findFirst();
    }

    public Optional<ElfSection<NATIVE_WORD>> sectionContainingAddress(ElfAddress<NATIVE_WORD> inMemoryAddress) {
        for (ElfSection<NATIVE_WORD> section: sections()) {
            ElfSectionHeader<NATIVE_WORD> header = section.header();

            if (inMemoryAddress.isAfterOrAt(header.virtualAddress()) &&
                    inMemoryAddress.isBefore(header.endVirtualAddress())) {
                return Optional.of(section);
            }
        }

        return Optional.empty();
    }


    public List<ElfSection<NATIVE_WORD>> sectionsOfType(ElfSectionType type) {
        return sections().stream()
                .filter(s -> s.header().type().is(type))
                .collect(toList());
    }

    public Optional<ElfSection<NATIVE_WORD>> sectionOfType(ElfSectionType type) {
        List<ElfSection<NATIVE_WORD>> sections = sectionsOfType(type);

        if (sections.size() > 1)
            throw new ElfReaderException("More than one section has type " + type + ".");

        return sections.size() == 1
                ? Optional.of(sections.get(0))
                : Optional.empty();
    }

    public Optional<ElfSectionHeader<NATIVE_WORD>> getSectionHeader(String sectionName) {
        Optional<ElfSectionHeader<NATIVE_WORD>> maybeSymbolTableSection = sectionHeaders().stream()
                .filter(s -> s.name().equals(sectionName))
                .findFirst();

        return maybeSymbolTableSection;
    }

    public List<ElfProgramHeader<NATIVE_WORD>> getProgramHeadersOfType(Elf32SegmentType segmentType) {
        List<ElfProgramHeader<NATIVE_WORD>> programHeaders = this.programHeaders.stream()
                .filter(ph -> ph.type().equals(segmentType))
                .collect(toList());

        return programHeaders;
    }

    public Optional<ElfSegment<NATIVE_WORD>> segmentContainingAddress(ElfAddress<NATIVE_WORD> inMemoryAddress) {
        for (ElfSegment<NATIVE_WORD> segment: segments()) {
            ElfProgramHeader<NATIVE_WORD> ph = segment.programHeader();

            if (inMemoryAddress.isAfterOrAt(ph.virtualAddress()) &&
                    inMemoryAddress.isBefore(ph.endVirtualAddressInFile())) {
                return Optional.of(segment);
            }
        }

        return Optional.empty();
    }

    public Optional<ElfOffset<NATIVE_WORD>> virtualAddressToFileOffset(ElfAddress<NATIVE_WORD> addr) {
        Optional<ElfSegment<NATIVE_WORD>> maybeSegment = segmentContainingAddress(addr);

        return maybeSegment.map(segment -> {
            long offset = addr.minus(segment.programHeader().virtualAddress());

            return segment.programHeader().fileOffset()
                    .plus(offset);
        });
    }


    public List<ElfSegment<NATIVE_WORD>> segmentsOfType(Elf32SegmentType type) {
        return segments().stream()
                .filter(s -> s.programHeader().type().is(type))
                .collect(toList());
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        header().accept(visitor);

        visitor.enterSections();
        for (ElfSection<NATIVE_WORD> section : sections()) {
            section.accept(visitor);
        }
        visitor.exitSections();

        visitor.enterSegments();
        for (ElfSegment<NATIVE_WORD> segment : segments()) {
            segment.accept(visitor);
        }
        visitor.exitSegments();
    }
}

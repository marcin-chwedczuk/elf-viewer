package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32SegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class ElfFile<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final AbstractFile storage;
    private final Endianness endianness;

    private final ElfHeader<NATIVE_WORD> header;

    /**
     * The section header for index 0 (SHN_UNDEF) _always_ exists,
     * even though the index marks undefined section references.
     */
    private final List<ElfSectionHeader<NATIVE_WORD>> sectionHeaders;
    private final Memoized<List<? extends ElfSection<NATIVE_WORD>>> sectionsMemoized;

    protected ElfFile(AbstractFile storage,
                      Endianness endianness,
                      ElfHeader<NATIVE_WORD> header,
                      List<? extends ElfSectionHeader<NATIVE_WORD>> sectionHeaders,
                      ElfSectionFactory<NATIVE_WORD> sectionFactory) {
        this.storage = requireNonNull(storage);
        this.endianness = requireNonNull(endianness);

        this.header = requireNonNull(header);

        this.sectionHeaders = new ArrayList<>(sectionHeaders);
        this.sectionsMemoized = new Memoized<>(
                () -> sectionFactory.createSections(this));
    }

    public final AbstractFile storage() {
        return storage;
    }

    public final Endianness endianness() {
        return endianness;
    }

    public ElfHeader<NATIVE_WORD> header() {
        return header;
    }

    public List<? extends ElfSectionHeader<NATIVE_WORD>> sectionHeaders() {
        return Collections.unmodifiableList(sectionHeaders);
    }

    public List<? extends ElfSection<NATIVE_WORD>> sections() {
        return sectionsMemoized.get();
    }

    public Optional<? extends ElfSection<NATIVE_WORD>> sectionWithName(String name) {
        return sections().stream()
                .filter(s -> Objects.equals(name, s.header().name()))
                .findFirst();
    }

}

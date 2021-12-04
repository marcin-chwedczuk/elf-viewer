package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Memoized;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ElfFile<
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

    public ElfFile(AbstractFile storage,
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
}

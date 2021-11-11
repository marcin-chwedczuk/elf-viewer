package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;

import java.util.List;
import java.util.Optional;

public class Elf32File {
    public final Endianness endianness;
    public final Elf32Header header;

    /**
     * The section header for index 0 (SHN_UNDEF) _always_ exists,
     * even though the index marks undefined section references.
     */
    public final List<Elf32SectionHeader> sectionHeaders;

    public Elf32File(Endianness endianness,
                     Elf32Header header,
                     List<Elf32SectionHeader> sectionHeaders) {
        this.endianness = endianness;
        this.header = header;
        this.sectionHeaders = sectionHeaders;
    }

    public Optional<Elf32SectionHeader> getSectionHeader(String sectionName) {
        Optional<Elf32SectionHeader> maybeSymbolTableSection = sectionHeaders.stream()
                .filter(s -> s.name().equals(sectionName))
                .findFirst();

        return maybeSymbolTableSection;
    }
}

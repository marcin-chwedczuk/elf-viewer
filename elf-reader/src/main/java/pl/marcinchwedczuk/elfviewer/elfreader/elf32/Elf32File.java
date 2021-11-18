package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Elf32File {
    public final AbstractFile storage;
    public final Endianness endianness;
    public final Elf32Header header;

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
}

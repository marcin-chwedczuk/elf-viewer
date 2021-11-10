package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import java.util.List;

public class Elf32File {
    public final Elf32Header header;

    /**
     * The section header for index 0 (SHN_UNDEF) _always_ exists,
     * even though the index marks undefined section references.
     */
    public final List<Elf32SectionHeader> sectionHeaders;

    public Elf32File(Elf32Header header,
                     List<Elf32SectionHeader> sectionHeaders) {
        this.header = header;
        this.sectionHeaders = sectionHeaders;
    }
}

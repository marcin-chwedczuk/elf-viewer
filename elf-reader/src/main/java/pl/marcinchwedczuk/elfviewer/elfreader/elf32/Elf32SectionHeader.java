package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Elf32SectionHeader {
    private Elf32File elfFile;

    @ElfApi("sh_name")
    private final StringTableIndex nameIndex;

    private final String name;

    @ElfApi("sh_type")
    private final ElfSectionType type;

    @ElfApi("sh_flags")
    private final SectionAttributes flags;

    @ElfApi("sh_addr")
    private final Elf32Address inMemoryAddress;

    @ElfApi("sh_offset")
    private final Elf32Offset offsetInFile;

    @ElfApi("sh_size")
    private final int sectionSize;

    @ElfApi("sh_link")
    private final int link;

    @ElfApi("sh_info")
    private final int info;

    @ElfApi("sh_addralign")
    private final int addressAlignment;

    @ElfApi("sh_entsize")
    private final int containedEntrySize;

    public Elf32SectionHeader(StringTableIndex nameIndex,
                              String name,
                              ElfSectionType type,
                              SectionAttributes flags,
                              Elf32Address inMemoryAddress,
                              Elf32Offset offsetInFile,
                              int sectionSize,
                              int link,
                              int info,
                              int addressAlignment,
                              int containedEntrySize) {
        this.elfFile = null;
        this.nameIndex = nameIndex;
        this.name = requireNonNull(name);
        this.type = requireNonNull(type);
        this.flags = requireNonNull(flags);
        this.inMemoryAddress = requireNonNull(inMemoryAddress);
        this.offsetInFile = requireNonNull(offsetInFile);
        this.sectionSize = sectionSize;
        this.link = link;
        this.info = info;
        this.addressAlignment = addressAlignment;
        this.containedEntrySize = containedEntrySize;
    }

    public Elf32File elfFile() {
        return elfFile;
    }
    void setElfFile(Elf32File elfFile) {
        if (this.elfFile != null)
            throw new IllegalStateException("Elf file is already set.");
        this.elfFile = requireNonNull(elfFile);
    }

    /**
     * @returns The name of the section.  Its value
     * is an index into the section header string table section,
     * giving the location of a null-terminated string.
     */
    public StringTableIndex nameIndex() {
        return nameIndex;
    }

    public String name() {
        return name;
    }

    public boolean hasName(String name) {
        return Objects.equals(this.name(), name);
    }

    public ElfSectionType type() {
        return type;
    }

    public SectionAttributes flags() {
        return flags;
    }

    /**
     * @return If this section appears in the memory image of a process,
     * this member holds the address at which the section's first
     * byte should reside.  Otherwise, the member contains zero.
     */
    public Elf32Address inMemoryAddress() {
        return inMemoryAddress;
    }

    /**
     * @return This member's value holds the byte offset from the
     * beginning of the file to the first byte in the section.
     * One section type, SHT_NOBITS, occupies no space in the
     * file, and its sh_offset member locates the conceptual
     * placement in the file.
     */
    public Elf32Offset offsetInFile() {
        return offsetInFile;
    }

    /**
     * @return This member holds the section's size in bytes. Unless the
     * section type is SHT_NOBITS, the section occupies sh_size
     * bytes in the file.  A section of type SHT_NOBITS may have
     * a nonzero size, but it occupies no space in the file.
     */
    public int sectionSize() {
        return sectionSize;
    }

    /**
     * @return This member holds a section header table index link, whose
     * interpretation depends on the section type.
     */
    public int link() {
        return link;
    }

    /**
     * @return This member holds extra information, whose interpretation
     * depends on the section type.
     */
    public int info() {
        return info;
    }

    /**
     * Some sections have address alignment constraints.
     * If a section holds a doubleword, the system must ensure
     * doubleword alignment for the entire section.  That is, the
     * value of sh_addr must be congruent to zero, modulo the
     * value of sh_addralign.  Only zero and positive integral
     * powers of two are allowed.  The value 0 or 1 means that
     * the section has no alignment constraints.
     */
    public int addressAlignment() {
        return addressAlignment;
    }

    /**
     * Some sections hold a table of fixed-sized entries, such as
     * a symbol table.  For such a section, this member gives the
     * size in bytes for each entry.  This member contains zero
     * if the section does not hold a table of fixed-size
     * entries.
     */
    public int containedEntrySize() {
        return containedEntrySize;
    }

    /**
     * @return Offset of the first byte in ELF file located after this section end.
     */
    public Elf32Offset sectionEndOffsetInFile() {
        // TODO: Consider alignment
        return offsetInFile.plus(sectionSize);
    }

    @Override
    public String toString() {
        return String.format(
                "%4s | %20s | %24s | %40s | %s | %s | 0x%08x | 0x%08x | %2d | %2d",
                nameIndex, name, type, flags, inMemoryAddress, offsetInFile,
                link, info, addressAlignment, containedEntrySize);
    }
}

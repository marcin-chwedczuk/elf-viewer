package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32SectionHeader {
    /**
     * This member specifies the name of the section.
     * Its value is an index into the section header
     * string table section [see "String Table'' below],
     * giving the location of a null-terminated string.
     */
    @ElfApi("sh_name")
    private final int nameIndex;
    private String sectionName;

    /**
     * This member categorizes the section's contents and semantics.
     * Section types and their descriptions appear below.
     */
    @ElfApi("sh_type")
    private final ElfSectionType type;

    /**
     * Sections support 1-bit flags that describe miscellaneous attributes.
     */
    @ElfApi("sh_flags")
    private final SectionAttributes flags;

    /**
     * If the section will appear in the memory image of a process,
     * this member gives the address at which the section's first byte should reside.
     * Otherwise, the member contains 0.
     */
    @ElfApi("sh_addr")
    private final Elf32Address inMemoryAddress;

    /**
     * This member's value gives the byte offset from the beginning
     * of the file to the first byte in the section.
     * One section type, SHT_NOBITS described below, occupies no space in the file,
     * and its sh_offset member locates the conceptual placement in the file.
     */
    @ElfApi("sh_offset")
    private final Elf32Offset offsetInFile;

    /**
     * This member gives the section's size in bytes.
     * Unless the section type is SHT_NOBITS,
     * the section occupies sh_size bytes in the file.
     * A section of type SHT_NOBITS may have a non-zero size,
     * but it occupies no space in the file.
     */
    @ElfApi("sh_size")
    private final int sectionSize;

    /**
     * This member holds a section header table index link,
     * whose interpretation depends on the section type.
     * A table below describes the values.
     */
    @ElfApi("sh_link")
    private final int link;

    /**
     * This member holds extra information, whose interpretation depends on the section type.
     * A table below describes the values.
     */
    @ElfApi("sh_info")
    private final int info;

    /**
     * Some sections have address alignment constraints.
     * For example, if a section holds a doubleword,
     * the system must ensure doubleword alignment for the entire section.
     * That is, the value of sh_addr must be congruent to 0, modulo the value of sh_addralign.
     * Currently, only 0 and positive integral powers of two are allowed.
     * Values 0 and 1 mean the section has no alignment constraints.
     */
    @ElfApi("sh_addralign")
    private final int addressAlignment;

    /**
     * Some sections hold a table of fixed-size entries, such as a symbol table.
     * For such a section, this member gives the size in bytes of each entry.
     * The member contains 0 if the section does not hold a table of fixed-size entries.
     */
    @ElfApi("sh_entsize")
    private final int containedEntrySize;

    public Elf32SectionHeader(int nameIndex,
                              ElfSectionType type,
                              SectionAttributes flags,
                              Elf32Address inMemoryAddress,
                              Elf32Offset offsetInFile,
                              int sectionSize,
                              int link,
                              int info,
                              int addressAlignment,
                              int containedEntrySize) {
        // TODO: Add arguments checks
        this.nameIndex = nameIndex;
        this.type = type;
        this.flags = flags;
        this.inMemoryAddress = inMemoryAddress;
        this.offsetInFile = offsetInFile;
        this.sectionSize = sectionSize;
        this.link = link;
        this.info = info;
        this.addressAlignment = addressAlignment;
        this.containedEntrySize = containedEntrySize;
    }

    public StringTableIndex nameIndex() { return new StringTableIndex(nameIndex); }
    public ElfSectionType type() { return type; }
    public SectionAttributes flags() { return flags; }
    public Elf32Address inMemoryAddress() { return inMemoryAddress; }
    public Elf32Offset offsetInFile() { return offsetInFile; }
    public int link() { return link; }
    public int info() { return info; }
    public int addressAlignment() { return addressAlignment; }
    public int containedEntrySize() { return containedEntrySize; }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String sectionName() { return sectionName; }

    @Override
    public String toString() {
        return String.format(
                "%4d | %20s | %24s | %40s | %s | %s | 0x%08x | 0x%08x | %2d | %2d",
                nameIndex, sectionName, type, flags, inMemoryAddress, offsetInFile,
                link, info, addressAlignment, containedEntrySize);
    }

}

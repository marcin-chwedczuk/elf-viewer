package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32SectionHeader {
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
        // TODO: Add arguments checks
        this.nameIndex = nameIndex;
        this.name = name;
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

    public ElfSectionType type() {
        return type;
    }

    public SectionAttributes flags() {
        return flags;
    }

    public Elf32Address inMemoryAddress() {
        return inMemoryAddress;
    }

    public Elf32Offset offsetInFile() {
        return offsetInFile;
    }

    public int sectionSize() {
        return sectionSize;
    }

    public int link() {
        return link;
    }

    public int info() {
        return info;
    }

    public int addressAlignment() {
        return addressAlignment;
    }

    public int containedEntrySize() {
        return containedEntrySize;
    }

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

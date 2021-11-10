package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum SectionAttributeFlags {
    /**
     * The section contains data that should be writable during process execution.
     */
    @ElfApi("SHF_WRITE")
    Write(1 << 0),

    /**
     * The section occupies memory during process execution.
     * Some control sections do not reside in the memory image of an object file;
     * this attribute is off for those sections.
     */
    @ElfApi("SHF_ALLOC")
    Allocate(1 << 1),

    /**
     * The section contains executable machine instructions.
     */
    @ElfApi("SHF_EXECINSTR")
    Executable(1 << 2),

    // GNU specific:
    // https://github.com/lattera/glibc/blob/master/elf/elf.h

    /**
     * Might be merged
     */
    @ElfApi("SHF_MERGE")
    Merge(1 << 4),

    /**
     * Contains nul-terminated strings
     */
    @ElfApi("SHF_STRINGS")
    Strings(1 << 5),

    /**
     * `sh_info' contains SHT index
     */
    @ElfApi("SHF_INFO_LINK")
    InfoLink(1 << 6),

    /**
     * Preserve order after combining
     */
    @ElfApi("SHF_LINK_ORDER")
    LinkOrder(1 << 7),

    /**
     * Non-standard OS specific handling
     * required
     */
    @ElfApi("SHF_OS_NONCONFORMING")
    NonConforming(1 << 8),

    /**
     * Section is member of a group
     */
    @ElfApi("SHF_GROUP")
    GroupMember(1 << 9),

    /**
     * Section hold thread-local data
     */
    @ElfApi("SHF_TLS")
    ThreadLocalData(1 << 10),

    /**
     * Section with compressed data
     */
    @ElfApi("SHF_COMPRESSED")
    Compressed(1 << 11),

    /**
     * Mask for OS-specific flags
     */
    @ElfApi("SHF_MASKOS")
    MaskOsSpecific(0x0ff00000),

    /**
     * All bits included in this mask are reserved for processor-specific semantics.
     */
    @ElfApi("SHF_MASKPROC")
    MaskProcessorSpecific(0xf0000000),

    /**
     * Special ordering requirement (Solaris).
     */
    @ElfApi("SHF_ORDERED")
    Ordered(1 << 30),

    /**
     * Section is excluded unless referenced or allocated (Solaris).
     */
    @ElfApi("SHF_EXCLUDE")
    Excluded(1 << 31);

    private int value;

    SectionAttributeFlags(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum Elf32SegmentType {
    /**
     * The array element is unused; other members' values are undefined.
     * This type lets the program header table have ignored entries.
     */
    @ElfApi("PT_NULL")
    Null(0),

    /**
     * The array element specifies a loadable segment,
     * described by p_filesz and p_memsz.
     * The bytes from the file are mapped to the beginning of the memory segment.
     * If the segment's memory size (p_memsz) is larger than the file size (p_filesz),
     * the "extra'' bytes are defined to hold the value 0 and to follow the segment's
     * initialized area. The file size may not be larger than the memory size.
     * Loadable segment entries in the program header table appear in ascending order,
     * sorted on the p_vaddr member.
     */
    @ElfApi("PT_LOAD")
    Load(1),

    /**
     * The array element specifies dynamic linking information. See Book III.
     */
    @ElfApi("PT_DYNAMIC")
    Dynamic(2),

    /**
     * The array element specifies the location and size of a null-terminated path name
     * to invoke as an interpreter. See Book III.
     */
    @ElfApi("PT_INTERP")
    Interpreter(3),

    /**
     * The array element specifies the location and size of auxiliary information.
     */
    @ElfApi("PT_NOTE")
    Note(4),

    /**
     * This segment type is reserved but has unspecified semantics. See Book III.
     */
    @ElfApi("PT_SHLIB")
    ShLib(5),

    /**
     * The array element, if present, specifies the location and size of the program
     * header table itself, both in the file and in the memory image of the program.
     * This segment type may not occur more than once in a file.
     * Moreover, it may occur only if the program header table is part of the memory
     * image of the program. If it is present, it must precede any loadable segment entry.
     * See "Program Interpreter" in the appendix at the end of Book III
     * for further information.
     */
    @ElfApi("PT_PHDR")
    ProgramHeader(6),

    /**
     * Thread-local storage segment.
     */
    @ElfApi("PT_TLS")
    ThreadLocalStorage(7),

    /**
     * Number of defined types.
     */
    @ElfApi("PT_NUM")
    NumberOfDefinedTypes(8),

    /**
     * GCC .eh_frame_hdr segment.
     */
    @ElfApi("PT_GNU_EH_FRAME")
    GnuEhFrame(0x6474e550),

    /**
     * Indicates stack executability.
     */
    @ElfApi("PT_GNU_STACK")
    GnuStack(0x6474e551),

    /**
     * Read-only after relocation.
     */
    @ElfApi("PT_GNU_RELRO")
    GnuRelRo(0x6474e552),

    // TODO: add other values from elf.h

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("PT_LOPROC")
    LoProcessorSpecific(0x70000000),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("PT_HIPROC")
    HiProcessorSpecific(0x7fffffff);

    public static Elf32SegmentType fromUnsignedInt(int v) {
        for (Elf32SegmentType value : Elf32SegmentType.values()) {
            // No sign extension byte -> int
            if (value.value == v) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized uint: " + Integer.toHexString(v));
    }

    private final int value;

    Elf32SegmentType(int value) {
        this.value = value;
    }
}

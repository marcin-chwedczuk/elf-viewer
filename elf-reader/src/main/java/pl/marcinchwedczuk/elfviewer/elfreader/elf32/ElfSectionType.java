package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

// TODO: Handle values between reserved ranges
public enum ElfSectionType {
    /**
     * This value marks the section header as inactive; it does not have an associated section.
     * Other members of the section header have undefined values.
     */
    @ElfApi("SHT_NULL")
    Null(0),

    /**
     * The section holds information defined by the program,
     * whose format and meaning are determined solely by the program.
     */
    @ElfApi("SHT_PROGBITS")
    ProgBits(1),

    /**
     * These sections hold a symbol table.
     */
    @ElfApi("SHT_SYMTAB")
    SymbolTable(2),

    /**
     * The section holds a string table.
     */
    @ElfApi("SHT_STRTAB")
    StringTable(3),

    /**
     * The section holds relocation entries with explicit addends,
     * such as type Elf32_Rela for the 32-bit class of object files.
     * An object file may have multiple relocation sections.
     * See "Relocation'' below for details.
     */
    @ElfApi("SHT_RELA")
    RelocationAddends(4),

    /**
     * The section holds a symbol hash table.
     */
    @ElfApi("SHT_HASH")
    Hash(5),

    /**
     * The section holds information for dynamic linking.
     */
    @ElfApi("SHT_DYNAMIC")
    Dynamic(6),

    /**
     * This section holds information that marks the file in some way.
     */
    @ElfApi("SHT_NOTE")
    Note(7),

    /**
     * A section of this type occupies no space in the file but otherwise resembles SHT_PROGBITS.
     * Although this section contains no bytes, the sh_offset member contains the conceptual file offset.
     */
    @ElfApi("SHT_NOBITS")
    NoBits(8),

    /**
     * The section holds relocation entries without explicit addends,
     * such as type Elf32_Rel for the 32-bit class of object files.
     * An object file may have multiple relocation sections.
     * See "Relocation'' below for details.
     */
    @ElfApi("SHT_REL")
    Relocation(9),

    /**
     * This section type is reserved but has unspecified semantics.
     */
    @ElfApi("SHT_SHLIB")
    ReservedShLib(10),

    /**
     * These sections hold a symbol table.
     */
    @ElfApi("SHT_DYNSYM")
    DynamicSymbols(11),


    // Additional types from:
    // http://ftp.linuxfoundation.org/pub/lsb/lsb-openjdk/openjdk7-lsb-overrides/include/elf.h

    /**
     * Array of constructors
     */
    @ElfApi("SHT_INIT_ARRAY")
    InitArray(14),

    /**
     * Array of destructors
     */
    @ElfApi("SHT_FINI_ARRAY")
    FiniArray(15),

    /**
     * Array of pre-constructors
     */
    @ElfApi("SHT_PREINIT_ARRAY")
    PreInitArray(16),

    /**
     * Section group
     */
    @ElfApi("SHT_GROUP")
    SectionGroup(17),

    /**
     * Extended section indeces
     */
    @ElfApi("SHT_SYMTAB_SHNDX")
    SymbolTableExtendedIndices(18),

    /**
     * Number of defined types
     */
    @ElfApi("SHT_NUM")
    NumberOfDefinedTypes(19),

    @ElfApi("SHT_LOOS")
    LoOsSpecific(0x60000000),

    /**
     * Object attributes
     */
    @ElfApi("SHT_GNU_ATTRIBUTES")
    GnuAttributes(0x6ffffff5),

    /**
     * GNU-style hash table
     */
    @ElfApi("SHT_GNU_HASH")
    GnuHash(0x6ffffff6),

    /**
     * Prelink library list
     */
    @ElfApi("SHT_GNU_LIBLIST")
    GnuLibList(0x6ffffff7),

    /**
     * Checksum for DSO content
     */
    @ElfApi("SHT_CHECKSUM")
    Checksum(0x6ffffff8),

    /**
     * Sun-specific low bound
     */
    @ElfApi("SHT_LOSUNW")
    LoSunSpecific(0x6ffffffa),

    @ElfApi("SHT_SUNW_move")
    SunMove(0x6ffffffa),

    @ElfApi("SHT_SUNW_COMDAT")
    SunComdat(0x6ffffffb),

    @ElfApi("SHT_SUNW_syminfo")
    SunSyminfo(0x6ffffffc),

    /**
     * Version definition section
     */
    @ElfApi("SHT_GNU_verdef")
    GnuVersionDefinition(0x6ffffffd),

    /**
     * Version needs section
     */
    @ElfApi("SHT_GNU_verneed")
    GnuVersionNeeds(0x6ffffffe),

    /**
     * Version symbol table
     */
    @ElfApi("SHT_GNU_versym")
    GnuVersionSymbolTable(0x6fffffff),

    /**
     * Sun-specific high bound
     */
    @ElfApi("SHT_HISUNW")
    HiSunSpecific(0x6fffffff),

    /**
     * End OS-specific type
     */
    @ElfApi("SHT_HIOS")
    HiOsSpecific(0x6fffffff),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHT_LOPROC")
    LoProcessorSpecific(0x70000000),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHT_HIPROC")
    HiProcessorSpecific(0x7fffffff),

    /**
     * This value specifies the lower bound of the range of indexes reserved for application programs.
     */
    @ElfApi("SHT_LOUSER")
    LoUser(0x80000000),

    /**
     * This value specifies the upper bound of the range of indexes reserved for application programs.
     * Section types between SHT_LOUSER and SHT_HIUSER may be used by the application,
     * without conflicting with current or future system-defined section types.
     */
    @ElfApi("SHT_HIPROC")
    HiUser(0xffffffff);


    public static ElfSectionType fromUnsignedInt(int v) {
        for (ElfSectionType value : ElfSectionType.values()) {
            if (value.value == v) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized uint: " + Integer.toHexString(v));
    }

    private int value;

    ElfSectionType(int value) {
        this.value = value;
    }
}

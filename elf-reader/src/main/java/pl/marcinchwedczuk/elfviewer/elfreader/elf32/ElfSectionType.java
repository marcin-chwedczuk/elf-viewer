package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;

public class ElfSectionType extends IntPartialEnum<ElfSectionType> {
    private static final Map<Integer, ElfSectionType> byValue = mkByValueMap();
    private static final Map<String, ElfSectionType> byName = mkByNameMap();

    /**
     * This value marks the section header as inactive; it does not have an associated section.
     * Other members of the section header have undefined values.
     */
    @ElfApi("SHT_NULL")
    public static final ElfSectionType NULL = new ElfSectionType(0, "NULL");

    /**
     * The section holds information defined by the program,
     * whose format and meaning are determined solely by the program.
     */
    @ElfApi("SHT_PROGBITS")
    public static final ElfSectionType PROGBITS = new ElfSectionType(1, "PROGBITS");

    /**
     * These sections hold a symbol table.
     */
    @ElfApi("SHT_SYMTAB")
    public static final ElfSectionType SYMBOL_TABLE = new ElfSectionType(2, "SYMBOL_TABLE");

    /**
     * The section holds a string table.
     */
    @ElfApi("SHT_STRTAB")
    public static final ElfSectionType STRING_TABLE = new ElfSectionType(3, "STRING_TABLE");

    /**
     * The section holds relocation entries with explicit addends,
     * such as type Elf32_Rela for the 32-bit class of object files.
     * An object file may have multiple relocation sections.
     * See "Relocation'' below for details.
     */
    @ElfApi("SHT_RELA")
    public static final ElfSectionType RELA = new ElfSectionType(4, "RELA");

    /**
     * The section holds a symbol hash table.
     */
    @ElfApi("SHT_HASH")
    public static final ElfSectionType HASH = new ElfSectionType(5, "HASH");

    /**
     * The section holds information for dynamic linking.
     */
    @ElfApi("SHT_DYNAMIC")
    public static final ElfSectionType DYNAMIC = new ElfSectionType(6, "DYNAMIC");

    /**
     * This section holds information that marks the file in some way.
     */
    @ElfApi("SHT_NOTE")
    public static final ElfSectionType NOTE = new ElfSectionType(7, "NOTE");

    /**
     * A section of this type occupies no space in the file but otherwise resembles SHT_PROGBITS.
     * Although this section contains no bytes, the sh_offset member contains the conceptual file offset.
     */
    @ElfApi("SHT_NOBITS")
    public static final ElfSectionType NO_BITS = new ElfSectionType(8, "NO_BITS");

    /**
     * The section holds relocation entries without explicit addends,
     * such as type Elf32_Rel for the 32-bit class of object files.
     * An object file may have multiple relocation sections.
     * See "Relocation'' below for details.
     */
    @ElfApi("SHT_REL")
    public static final ElfSectionType REL = new ElfSectionType(9, "REL");

    /**
     * This section type is reserved but has unspecified semantics.
     */
    @ElfApi("SHT_SHLIB")
    public static final ElfSectionType SHLIB = new ElfSectionType(10, "SHLIB");

    /**
     * These sections hold a symbol table.
     */
    @ElfApi("SHT_DYNSYM")
    public static final ElfSectionType DYNAMIC_SYMBOLS = new ElfSectionType(11, "DYNAMIC_SYMBOLS");

    /**
     * Array of constructors
     */
    @ElfApi("SHT_INIT_ARRAY")
    public static final ElfSectionType INIT_ARRAY = new ElfSectionType(14, "INIT_ARRAY");

    /**
     * Array of destructors
     */
    @ElfApi("SHT_FINI_ARRAY")
    public static final ElfSectionType FINI_ARRAY = new ElfSectionType(15, "FINI_ARRAY");

    /**
     * Array of pre-constructors
     */
    @ElfApi("SHT_PREINIT_ARRAY")
    public static final ElfSectionType PREINIT_ARRAY = new ElfSectionType(16, "PREINIT_ARRAY");

    /**
     * Section group
     */
    @ElfApi("SHT_GROUP")
    public static final ElfSectionType GROUP = new ElfSectionType(17, "GROUP");

    /**
     * Extended section indeces
     */
    @ElfApi("SHT_SYMTAB_SHNDX")
    public static final ElfSectionType SYMTAB_SHNDX = new ElfSectionType(18, "SYMTAB_SHNDX");

    /**
     * Number of defined types
     */
    @ElfApi("SHT_NUM")
    public static final ElfSectionType NUMBER_DEFINED_TYPES = new ElfSectionType(19, "NUMBER_DEFINED_TYPES");

    @ElfApi("SHT_LOOS")
    public static final int LO_OS_SPECIFIC = 0x60000000;

    /**
     * Object attributes
     */
    @ElfApi("SHT_GNU_ATTRIBUTES")
    public static final ElfSectionType GNU_ATTRIBUTES = new ElfSectionType(0x6ffffff5, "GNU_ATTRIBUTES");

    /**
     * GNU-style hash table
     */
    @ElfApi("SHT_GNU_HASH")
    public static final ElfSectionType GNU_HASH = new ElfSectionType(0x6ffffff6, "GNU_HASH");

    /**
     * Prelink library list
     */
    @ElfApi("SHT_GNU_LIBLIST")
    public static final ElfSectionType GNU_LIBLIST = new ElfSectionType(0x6ffffff7, "GNU_LIBLIST");

    /**
     * Checksum for DSO content
     */
    @ElfApi("SHT_CHECKSUM")
    public static final ElfSectionType CHECKSUM = new ElfSectionType(0x6ffffff8, "CHECKSUM");

    /**
     * Sun-specific low bound
     */
    @ElfApi("SHT_LOSUNW")
    public static final int LO_SUN_SPECIFIC = 0x6ffffffa;

    @ElfApi("SHT_SUNW_move")
    public static final ElfSectionType SUNW_MOVE = new ElfSectionType(0x6ffffffa, "SUNW_MOVE");

    @ElfApi("SHT_SUNW_COMDAT")
    public static final ElfSectionType SUNW_COMDAT = new ElfSectionType(0x6ffffffb, "SUNW_COMDAT");

    @ElfApi("SHT_SUNW_syminfo")
    public static final ElfSectionType SUNW_SYMINFO = new ElfSectionType(0x6ffffffc, "SUNW_SYMINFO");

    /**
     * Version definition section
     */
    @ElfApi("SHT_GNU_verdef")
    public static final ElfSectionType GNU_VERDEF = new ElfSectionType(0x6ffffffd, "GNU_VERDEF");

    /**
     * Version needs section
     */
    @ElfApi("SHT_GNU_verneed")
    public static final ElfSectionType GNU_VERNEED = new ElfSectionType(0x6ffffffe, "GNU_VERNEED");

    /**
     * Version symbol table
     */
    @ElfApi("SHT_GNU_versym")
    public static final ElfSectionType GNU_VERSYM = new ElfSectionType(0x6fffffff, "GNU_VERSYM");

    /**
     * Sun-specific high bound
     */
    @ElfApi("SHT_HISUNW")
    public static final int HI_SUN_SPECIFIC = 0x6fffffff;

    /**
     * End OS-specific type
     */
    @ElfApi("SHT_HIOS")
    public static final int HI_OS_SPECIFIC = 0x6fffffff;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHT_LOPROC")
    public static final int LO_PROCESSOR_SPECIFIC = 0x70000000;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHT_HIPROC")
    public static final int HI_PROCESSOR_SPECIFIC = 0x7fffffff;

    /**
     * This value specifies the lower bound of the range of indexes reserved for application programs.
     */
    @ElfApi("SHT_LOUSER")
    public static final int LO_USER_SPECIFIC = 0x80000000;

    /**
     * This value specifies the upper bound of the range of indexes reserved for application programs.
     * Section types between SHT_LOUSER and SHT_HIUSER may be used by the application,
     * without conflicting with current or future system-defined section types.
     */
    @ElfApi("SHT_HIPROC")
    public static final int HI_USER_SPECIFIC = 0xffffffff;

    private ElfSectionType(int value) {
        super(value);
    }

    private ElfSectionType(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSectionType fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfSectionType::new);
    }

    public static ElfSectionType fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfSectionType> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }
}

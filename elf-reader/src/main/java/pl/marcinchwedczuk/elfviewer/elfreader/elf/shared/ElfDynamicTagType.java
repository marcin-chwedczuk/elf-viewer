package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfDynamicTagType extends IntPartialEnum<ElfDynamicTagType> {
    private static final Map<Integer, ElfDynamicTagType> byValue = mkByValueMap();
    private static final Map<String, ElfDynamicTagType> byName = mkByNameMap();

    /**
     * Marks end of dynamic section
     */
    @ElfApi("DT_NULL")
    public static final ElfDynamicTagType NULL = new ElfDynamicTagType(0, "NULL");

    /**
     * Name of needed library
     */
    @ElfApi("DT_NEEDED")
    public static final ElfDynamicTagType NEEDED = new ElfDynamicTagType(1, "NEEDED");

    /**
     * Size in bytes of PLT relocs
     */
    @ElfApi("DT_PLTRELSZ")
    public static final ElfDynamicTagType PLTRELSZ = new ElfDynamicTagType(2, "PLTRELSZ");

    /**
     * Processor defined value
     */
    @ElfApi("DT_PLTGOT")
    public static final ElfDynamicTagType PLTGOT = new ElfDynamicTagType(3, "PLTGOT");

    /**
     * Address of symbol hash table
     */
    @ElfApi("DT_HASH")
    public static final ElfDynamicTagType HASH = new ElfDynamicTagType(4, "HASH");

    /**
     * Address of string table
     */
    @ElfApi("DT_STRTAB")
    public static final ElfDynamicTagType STRTAB = new ElfDynamicTagType(5, "STRTAB");

    /**
     * Address of symbol table
     */
    @ElfApi("DT_SYMTAB")
    public static final ElfDynamicTagType SYMTAB = new ElfDynamicTagType(6, "SYMTAB");

    /**
     * Address of Rela relocs
     */
    @ElfApi("DT_RELA")
    public static final ElfDynamicTagType RELA = new ElfDynamicTagType(7, "RELA");

    /**
     * Total size of Rela relocs
     */
    @ElfApi("DT_RELASZ")
    public static final ElfDynamicTagType RELASZ = new ElfDynamicTagType(8, "RELASZ");

    /**
     * Size of one Rela reloc
     */
    @ElfApi("DT_RELAENT")
    public static final ElfDynamicTagType RELAENT = new ElfDynamicTagType(9, "RELAENT");

    /**
     * Size of string table
     */
    @ElfApi("DT_STRSZ")
    public static final ElfDynamicTagType STRSZ = new ElfDynamicTagType(10, "STRSZ");

    /**
     * Size of one symbol table entry
     */
    @ElfApi("DT_SYMENT")
    public static final ElfDynamicTagType SYMENT = new ElfDynamicTagType(11, "SYMENT");

    /**
     * Address of init function
     */
    @ElfApi("DT_INIT")
    public static final ElfDynamicTagType INIT = new ElfDynamicTagType(12, "INIT");

    /**
     * Address of termination function
     */
    @ElfApi("DT_FINI")
    public static final ElfDynamicTagType FINI = new ElfDynamicTagType(13, "FINI");

    /**
     * Name of shared object
     */
    @ElfApi("DT_SONAME")
    public static final ElfDynamicTagType SONAME = new ElfDynamicTagType(14, "SONAME");

    /**
     * Library search path (deprecated)
     */
    @ElfApi("DT_RPATH")
    public static final ElfDynamicTagType RPATH = new ElfDynamicTagType(15, "RPATH");

    /**
     * Start symbol search here
     */
    @ElfApi("DT_SYMBOLIC")
    public static final ElfDynamicTagType SYMBOLIC = new ElfDynamicTagType(16, "SYMBOLIC");

    /**
     * Address of Rel relocs
     */
    @ElfApi("DT_REL")
    public static final ElfDynamicTagType REL = new ElfDynamicTagType(17, "REL");

    /**
     * Total size of Rel relocs
     */
    @ElfApi("DT_RELSZ")
    public static final ElfDynamicTagType RELSZ = new ElfDynamicTagType(18, "RELSZ");

    /**
     * Size of one Rel reloc
     */
    @ElfApi("DT_RELENT")
    public static final ElfDynamicTagType RELENT = new ElfDynamicTagType(19, "RELENT");

    /**
     * Type of reloc in PLT
     */
    @ElfApi("DT_PLTREL")
    public static final ElfDynamicTagType PLTREL = new ElfDynamicTagType(20, "PLTREL");

    /**
     * For debugging; unspecified
     */
    @ElfApi("DT_DEBUG")
    public static final ElfDynamicTagType DEBUG = new ElfDynamicTagType(21, "DEBUG");

    /**
     * Reloc might modify .text
     */
    @ElfApi("DT_TEXTREL")
    public static final ElfDynamicTagType TEXTREL = new ElfDynamicTagType(22, "TEXTREL");

    /**
     * Address of PLT relocs
     */
    @ElfApi("DT_JMPREL")
    public static final ElfDynamicTagType JMPREL = new ElfDynamicTagType(23, "JMPREL");

    /**
     * Process relocations of object
     */
    @ElfApi("DT_BIND_NOW")
    public static final ElfDynamicTagType BIND_NOW = new ElfDynamicTagType(24, "BIND_NOW");

    /**
     * Array with addresses of init fct
     */
    @ElfApi("DT_INIT_ARRAY")
    public static final ElfDynamicTagType INIT_ARRAY = new ElfDynamicTagType(25, "INIT_ARRAY");

    /**
     * Array with addresses of fini fct
     */
    @ElfApi("DT_FINI_ARRAY")
    public static final ElfDynamicTagType FINI_ARRAY = new ElfDynamicTagType(26, "FINI_ARRAY");

    /**
     * Size in bytes of DT_INIT_ARRAY
     */
    @ElfApi("DT_INIT_ARRAYSZ")
    public static final ElfDynamicTagType INIT_ARRAYSZ = new ElfDynamicTagType(27, "INIT_ARRAYSZ");

    /**
     * Size in bytes of DT_FINI_ARRAY
     */
    @ElfApi("DT_FINI_ARRAYSZ")
    public static final ElfDynamicTagType FINI_ARRAYSZ = new ElfDynamicTagType(28, "FINI_ARRAYSZ");

    /**
     * Library search path
     */
    @ElfApi("DT_RUNPATH")
    public static final ElfDynamicTagType RUNPATH = new ElfDynamicTagType(29, "RUNPATH");

    /**
     * Flags for the object being loaded
     */
    @ElfApi("DT_FLAGS")
    public static final ElfDynamicTagType FLAGS = new ElfDynamicTagType(30, "FLAGS");

    // See: https://docs.oracle.com/cd/E53394_01/html/E54813/chapter6-42444.html#scrolltoc
    /**
     * Start of encoded range.
     */
    @ElfApi("DT_ENCODING")
    public static final int ENCODING = 31;

    /**
     * Array with addresses of preinit fct
     */
    @ElfApi("DT_PREINIT_ARRAY")
    public static final ElfDynamicTagType PREINIT_ARRAY = new ElfDynamicTagType(32, "PREINIT_ARRAY");

    /**
     * size in bytes of DT_PREINIT_ARRAY
     */
    @ElfApi("DT_PREINIT_ARRAYSZ")
    public static final ElfDynamicTagType PREINIT_ARRAYSZ = new ElfDynamicTagType(33, "PREINIT_ARRAYSZ");

    /**
     * Address of SYMTAB_SHNDX section
     */
    @ElfApi("DT_SYMTAB_SHNDX")
    public static final ElfDynamicTagType SYMTAB_SHNDX = new ElfDynamicTagType(34, "SYMTAB_SHNDX");

    /**
     * Start of OS-specific
     */
    @ElfApi("DT_LOOS")
    public static final int LO_OS_SPECIFIC = 0x6000000d;

    /**
     * End of OS-specific
     */
    @ElfApi("DT_HIOS")
    public static final int HI_OS_SPECIFIC = 0x6ffff000;

    /**
     * Start of processor-specific
     */
    @ElfApi("DT_LOPROC")
    public static final int LO_PROCESSOR_SPECIFIC = 0x70000000;

    /**
     * End of processor-specific
     */
    @ElfApi("DT_HIPROC")
    public static final int HI_PROCESSOR_SPECIFIC = 0x7fffffff;

    /**
     * DT_* entries which fall between DT_VALRNGHI & DT_VALRNGLO use the
     * Dyn.d_un.d_val field of the Elf*_Dyn structure.  This follows Sun's
     * approach.
     */
    @ElfApi("DT_VALRNGLO")
    public static final int VALRNGLO = 0x6ffffd00;

    /**
     * Prelinking timestamp
     */
    @ElfApi("DT_GNU_PRELINKED")
    public static final ElfDynamicTagType GNU_PRELINKED = new ElfDynamicTagType(0x6ffffdf5, "GNU_PRELINKED");

    /**
     * Size of conflict section
     */
    @ElfApi("DT_GNU_CONFLICTSZ")
    public static final ElfDynamicTagType GNU_CONFLICTSZ = new ElfDynamicTagType(0x6ffffdf6, "GNU_CONFLICTSZ");

    /**
     * Size of library list
     */
    @ElfApi("DT_GNU_LIBLISTSZ")
    public static final ElfDynamicTagType GNU_LIBLISTSZ = new ElfDynamicTagType(0x6ffffdf7, "GNU_LIBLISTSZ");

    @ElfApi("DT_CHECKSUM")
    public static final ElfDynamicTagType CHECKSUM = new ElfDynamicTagType(0x6ffffdf8, "CHECKSUM");

    @ElfApi("DT_PLTPADSZ")
    public static final ElfDynamicTagType PLTPADSZ = new ElfDynamicTagType(0x6ffffdf9, "PLTPADSZ");

    @ElfApi("DT_MOVEENT")
    public static final ElfDynamicTagType MOVEENT = new ElfDynamicTagType(0x6ffffdfa, "MOVEENT");

    @ElfApi("DT_MOVESZ")
    public static final ElfDynamicTagType MOVESZ = new ElfDynamicTagType(0x6ffffdfb, "MOVESZ");

    /**
     * Feature selection (DTF_*).
     */
    @ElfApi("DT_FEATURE_1")
    public static final ElfDynamicTagType FEATURE_1 = new ElfDynamicTagType(0x6ffffdfc, "FEATURE_1");

    /**
     * Flags for DT_* entries, effecting the following DT_* entry.
     */
    @ElfApi("DT_POSFLAG_1")
    public static final ElfDynamicTagType POSFLAG_1 = new ElfDynamicTagType(0x6ffffdfd, "POSFLAG_1");

    /**
     * Size of syminfo table (in bytes)
     */
    @ElfApi("DT_SYMINSZ")
    public static final ElfDynamicTagType SYMINSZ = new ElfDynamicTagType(0x6ffffdfe, "SYMINSZ");

    /**
     * Entry size of syminfo
     */
    @ElfApi("DT_SYMINENT")
    public static final ElfDynamicTagType SYMINENT = new ElfDynamicTagType(0x6ffffdff, "SYMINENT");

    /**
     * no info
     */
    @ElfApi("DT_VALRNGHI")
    public static final int VALRNGHI = 0x6ffffdff;

    /**
     * DT_* entries which fall between DT_ADDRRNGHI & DT_ADDRRNGLO use the
     * Dyn.d_un.d_ptr field of the Elf*_Dyn structure.
     * If any adjustment is made to the ELF object after it has been
     * built these entries will need to be adjusted.
     */
    @ElfApi("DT_ADDRRNGLO")
    public static final int ADDRRNGLO = 0x6ffffe00;

    /**
     * GNU-style hash table.
     */
    @ElfApi("DT_GNU_HASH")
    public static final ElfDynamicTagType GNU_HASH = new ElfDynamicTagType(0x6ffffef5, "GNU_HASH");

    @ElfApi("DT_TLSDESC_PLT")
    public static final ElfDynamicTagType TLSDESC_PLT = new ElfDynamicTagType(0x6ffffef6, "TLSDESC_PLT");

    @ElfApi("DT_TLSDESC_GOT")
    public static final ElfDynamicTagType TLSDESC_GOT = new ElfDynamicTagType(0x6ffffef7, "TLSDESC_GOT");

    /**
     * Start of conflict section
     */
    @ElfApi("DT_GNU_CONFLICT")
    public static final ElfDynamicTagType GNU_CONFLICT = new ElfDynamicTagType(0x6ffffef8, "GNU_CONFLICT");

    /**
     * Library list
     */
    @ElfApi("DT_GNU_LIBLIST")
    public static final ElfDynamicTagType GNU_LIBLIST = new ElfDynamicTagType(0x6ffffef9, "GNU_LIBLIST");

    /**
     * Configuration information.
     */
    @ElfApi("DT_CONFIG")
    public static final ElfDynamicTagType CONFIG = new ElfDynamicTagType(0x6ffffefa, "CONFIG");

    /**
     * Dependency auditing.
     */
    @ElfApi("DT_DEPAUDIT")
    public static final ElfDynamicTagType DEPAUDIT = new ElfDynamicTagType(0x6ffffefb, "DEPAUDIT");

    /**
     * Object auditing.
     */
    @ElfApi("DT_AUDIT")
    public static final ElfDynamicTagType AUDIT = new ElfDynamicTagType(0x6ffffefc, "AUDIT");

    /**
     * PLT padding.
     */
    @ElfApi("DT_PLTPAD")
    public static final ElfDynamicTagType PLTPAD = new ElfDynamicTagType(0x6ffffefd, "PLTPAD");

    /**
     * Move table.
     */
    @ElfApi("DT_MOVETAB")
    public static final ElfDynamicTagType MOVETAB = new ElfDynamicTagType(0x6ffffefe, "MOVETAB");

    /**
     * Syminfo table.
     */
    @ElfApi("DT_SYMINFO")
    public static final ElfDynamicTagType SYMINFO = new ElfDynamicTagType(0x6ffffeff, "SYMINFO");

    @ElfApi("DT_ADDRRNGHI")
    public static final int ADDRRNGHI = 0x6ffffeff;

    /* The versioning entry types.  The next are defined as part of the GNU extension.  */

    @ElfApi("DT_RELACOUNT")
    public static final ElfDynamicTagType RELACOUNT = new ElfDynamicTagType(0x6ffffff9, "RELACOUNT");

    @ElfApi("DT_RELCOUNT")
    public static final ElfDynamicTagType RELCOUNT = new ElfDynamicTagType(0x6ffffffa, "RELCOUNT");


    /**
     * These were chosen by Sun.
     */
    @ElfApi("DT_VERSYM")
    public static final ElfDynamicTagType VERSYM = new ElfDynamicTagType(0x6ffffff0, "VERSYM");

    /**
     * State flags, see DF_1_* below.
     */
    @ElfApi("DT_FLAGS_1")
    public static final ElfDynamicTagType FLAGS_1 = new ElfDynamicTagType(0x6ffffffb, "FLAGS_1");

    /**
     * Address of version definition table
     */
    @ElfApi("DT_VERDEF")
    public static final ElfDynamicTagType VERDEF = new ElfDynamicTagType(0x6ffffffc, "VERDEF");

    /**
     * Number of version definitions
     */
    @ElfApi("DT_VERDEFNUM")
    public static final ElfDynamicTagType VERDEFNUM = new ElfDynamicTagType(0x6ffffffd, "VERDEFNUM");

    /**
     * Address of table with needed versions
     */
    @ElfApi("DT_VERNEED")
    public static final ElfDynamicTagType VERNEED = new ElfDynamicTagType(0x6ffffffe, "VERNEED");

    /**
     * Number of needed versions
     */
    @ElfApi("DT_VERNEEDNUM")
    public static final ElfDynamicTagType VERNEEDNUM = new ElfDynamicTagType(0x6fffffff, "VERNEEDNUM");

    /* Sun added these machine-independent extensions in the "processor-specific"
     range.  Be compatible.  */

    /**
     * Shared object to load before self
     */
    @ElfApi("DT_AUXILIARY")
    public static final ElfDynamicTagType AUXILIARY = new ElfDynamicTagType(0x7ffffffd, "AUXILIARY");

    /**
     * Shared object to get values from
     */
    @ElfApi("DT_FILTER")
    public static final ElfDynamicTagType FILTER = new ElfDynamicTagType(0x7fffffff, "FILTER");


    private ElfDynamicTagType(int value) {
        super(value);
    }

    private ElfDynamicTagType(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfDynamicTagType fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfDynamicTagType::new);
    }

    public static ElfDynamicTagType fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfDynamicTagType> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}


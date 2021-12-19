package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfSegmentType extends IntPartialEnum<ElfSegmentType> {
    private static final Map<Integer, ElfSegmentType> byValue = mkByValueMap();
    private static final Map<String, ElfSegmentType> byName = mkByNameMap();

    /**
     * The array element is unused and the other
     * members' values are undefined.  This lets the
     * program header have ignored entries.
     */
    @ElfApi("PT_NULL")
    public static final ElfSegmentType NULL = new ElfSegmentType(0, "NULL");

    /**
     * The array element specifies a loadable segment,
     * described by p_filesz and p_memsz.  The bytes
     * from the file are mapped to the beginning of the
     * memory segment.  If the segment's memory size
     * p_memsz is larger than the file size p_filesz,
     * the "extra" bytes are defined to hold the value
     * 0 and to follow the segment's initialized area.
     * The file size may not be larger than the memory
     * size.  Loadable segment entries in the program
     * header table appear in ascending order, sorted
     * on the p_vaddr member.
     */
    @ElfApi("PT_LOAD")
    public static final ElfSegmentType LOAD = new ElfSegmentType(1, "LOAD");

    /**
     * The array element specifies dynamic linking
     * information.
     */
    @ElfApi("PT_DYNAMIC")
    public static final ElfSegmentType DYNAMIC = new ElfSegmentType(2, "DYNAMIC");

    /**
     * The array element specifies the location and
     * size of a null-terminated pathname to invoke as
     * an interpreter.  This segment type is meaningful
     * only for executable files (though it may occur
     * for shared objects).  However it may not occur
     * more than once in a file.  If it is present, it
     * must precede any loadable segment entry.
     */
    @ElfApi("PT_INTERP")
    public static final ElfSegmentType INTERPRETER = new ElfSegmentType(3, "INTERPRETER");

    /**
     * The array element specifies the location of
     * notes (ElfN_Nhdr).
     */
    @ElfApi("PT_NOTE")
    public static final ElfSegmentType NOTE = new ElfSegmentType(4, "NOTE");

    /**
     * This segment type is reserved but has
     * unspecified semantics.  Programs that contain an
     * array element of this type do not conform to the
     * ABI.
     */
    @ElfApi("PT_SHLIB")
    public static final ElfSegmentType SHLIB = new ElfSegmentType(5, "SHLIB");

    /**
     * The array element, if present, specifies the
     * location and size of the program header table
     * itself, both in the file and in the memory image
     * of the program.  This segment type may not occur
     * more than once in a file.  Moreover, it may
     * occur only if the program header table is part
     * of the memory image of the program.  If it is
     * present, it must precede any loadable segment
     * entry.
     */
    @ElfApi("PT_PHDR")
    public static final ElfSegmentType PROGRAM_HEADER = new ElfSegmentType(6, "PROGRAM_HEADER");

    @ElfApi("PT_TLS")
    public static final ElfSegmentType THREAD_LOCAL_STORAGE = new ElfSegmentType(7, "THREAD_LOCAL_STORAGE");

    @ElfApi("PT_NUM")
    public static final ElfSegmentType NUM_DEFINED_TYPES = new ElfSegmentType(8, "NUM_DEFINED_TYPES");

    @ElfApi("PT_GNU_EH_FRAME")
    public static final ElfSegmentType GNU_EH_FRAME = new ElfSegmentType(0x6474e550, "GNU_EH_FRAME");

    /**
     * GNU extension which is used by the Linux kernel
     * to control the state of the stack via the flags
     * set in the p_flags member.
     */
    @ElfApi("PT_GNU_STACK")
    public static final ElfSegmentType GNU_STACK = new ElfSegmentType(0x6474e551, "GNU_STACK");

    @ElfApi("PT_GNU_RELRO")
    public static final ElfSegmentType GNU_RELO = new ElfSegmentType(0x6474e552, "GNU_RELO");

    @ElfApi("PT_SUNWBSS")
    public static final ElfSegmentType SUNW_BSS = new ElfSegmentType(0x6ffffffa, "SUNW_BSS");

    @ElfApi("PT_SUNWSTACK")
    public static final ElfSegmentType SUNW_STACK = new ElfSegmentType(0x6ffffffb, "SUNW_STACK");

    @ElfApi("PT_LOOS")
    public static final int LO_OS_SPECIFIC = 0x60000000;
    @ElfApi("PT_HIOS")
    public static final int HI_OS_SPECIFIC = 0x6fffffff;

    @ElfApi("PT_LOPROC")
    public static final int LO_PROCESSOR_SPECIFIC = 0x70000000;
    @ElfApi("PT_HIPROC")
    public static final int HI_PROCESSOR_SPECIFIC = 0x7fffffff;

    @ElfApi("PT_LOSUNW")
    public static final int LO_SUNW = 0x6ffffffa;
    @ElfApi("PT_HISUNW")
    public static final int HI_SUNW = 0x6fffffff;

    private ElfSegmentType(int value) {
        super(value);
    }

    private ElfSegmentType(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSegmentType fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfSegmentType::new);
    }

    public static ElfSegmentType fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfSegmentType> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

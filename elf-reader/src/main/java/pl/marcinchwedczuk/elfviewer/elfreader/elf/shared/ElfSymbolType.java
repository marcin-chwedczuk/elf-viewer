package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfSymbolType extends BytePartialEnum<ElfSymbolType> {
    private static final Map<Byte, ElfSymbolType> byValue = mkByValueMap();
    private static final Map<String, ElfSymbolType> byName = mkByNameMap();

    /**
     * The symbol's type is not specified.
     */
    @ElfApi("STT_NOTYPE")
    public static ElfSymbolType NOTYPE = new ElfSymbolType(b(0), "NOTYPE");

    /**
     * The symbol is associated with a data object, such as a variable, an array, and so on.
     */
    @ElfApi("STT_OBJECT")
    public static ElfSymbolType OBJECT = new ElfSymbolType(b(1), "OBJECT");

    /**
     * The symbol is associated with a function or other executable code.
     */
    @ElfApi("STT_FUNC")
    public static ElfSymbolType FUNCTION = new ElfSymbolType(b(2), "FUNCTION");

    /**
     * The symbol is associated with a section. Symbol table entries of this type
     * exist primarily for relocation and normally have STB_LOCAL binding.
     */
    @ElfApi("STT_SECTION")
    public static ElfSymbolType SECTION = new ElfSymbolType(b(3), "SECTION");

    /**
     * A file symbol has STB_LOCAL binding, its section index is SHN_ABS,
     * and it precedes the other STB_LOCAL symbols for the file, if it is present.
     */
    @ElfApi("STT_FILE")
    public static ElfSymbolType FILE = new ElfSymbolType(b(4), "FILE");

    /**
     * Symbol is a common data object.
     */
    @ElfApi("STT_COMMON")
    public static ElfSymbolType COMMON = new ElfSymbolType(b(5), "COMMON");

    /**
     * Symbol is thread-local data object.
     */
    @ElfApi("STT_TLS")
    public static ElfSymbolType THREAD_LOCAL_STORAGE = new ElfSymbolType(b(6), "THREAD_LOCAL_STORAGE");

    /**
     * Number of defined types.
     */
    @ElfApi("STT_NUM")
    public static ElfSymbolType NUMBER_OF_TYPES = new ElfSymbolType(b(7), "NUMBER_OF_TYPES");

    /**
     * Symbol is indirect code object.
     */
    @ElfApi("STT_GNU_IFUNC")
    public static ElfSymbolType INDIRECT_FUNCTION = new ElfSymbolType(b(10), "INDIRECT_FUNCTION");

    /**
     * Values in this inclusive range are reserved for OS-specific semantics.
     */
    @ElfApi("STT_LOOS")
    public static final int LO_OS_SPECIFIC = 10;

    /**
     * Values in this inclusive range are reserved for OS-specific semantics.
     */
    @ElfApi("STT_HIOS")
    public static final int HI_OS_SPECIFIC = 12;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STT_LOPROC")
    public static final int LO_PROCESSOR_SPECIFIC = 13;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STT_HIPROC")
    public static final int HI_PROCESSOR_SPECIFIC = 15;

    private ElfSymbolType(byte value) {
        super(value);
    }

    private ElfSymbolType(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSymbolType fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfSymbolType::new);
    }

    public static ElfSymbolType fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static ElfSymbolType fromSymbolInfo(byte info) {
        return fromValue((byte)(info & 0x0f));
    }

    public static Collection<ElfSymbolType> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

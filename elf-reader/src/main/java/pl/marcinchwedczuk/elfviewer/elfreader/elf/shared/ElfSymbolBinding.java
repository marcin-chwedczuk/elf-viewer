package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfSymbolBinding extends BytePartialEnum<ElfSymbolBinding> {
    private static final Map<Byte, ElfSymbolBinding> byValue = mkByValueMap();
    private static final Map<String, ElfSymbolBinding> byName = mkByNameMap();

    /**
     * Local symbols are not visible outside the object file containing their definition.
     * Local symbols of the same name may exist in multiple files without interfering with each other.
     */
    @ElfApi("STB_LOCAL")
    public static final ElfSymbolBinding LOCAL = new ElfSymbolBinding(b(0), "LOCAL");

    /**
     * Global symbols are visible to all object files being combined.
     * One file's definition of a global symbol will satisfy another file's
     * undefined reference to the same global symbol.
     */
    @ElfApi("STB_GLOBAL")
    public static final ElfSymbolBinding GLOBAL = new ElfSymbolBinding(b(1), "GLOBAL");

    /**
     * Weak symbols resemble global symbols, but their definitions have lower precedence.
     */
    @ElfApi("STB_WEAK")
    public static final ElfSymbolBinding WEAK = new ElfSymbolBinding(b(2), "WEAK");

    /**
     * Number of defined types.
     */
    @ElfApi("STB_NUM")
    public static final ElfSymbolBinding NUMBER_DEFINED_TYPES = new ElfSymbolBinding(b(3), "NUMBER_DEFINED_TYPES");

    /**
     * Number of defined types.
     */
    @ElfApi("STB_GNU_UNIQUE")
    public static final ElfSymbolBinding GNU_UNIQUE = new ElfSymbolBinding(b(10), "GNU_UNIQUE");

    /**
     * Values in this inclusive range are reserved for OS-specific semantics.
     */
    @ElfApi("STB_LOOS")
    public static final int LO_OS_SPECIFIC = 10;

    /**
     * Values in this inclusive range are reserved for OS-specific semantics.
     */
    @ElfApi("STB_HIOS")
    public static final int HI_OS_SPECIFIC = 12;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STB_LOPROC")
    public static final int LO_PROCESSOR_SPECIFIC = 13;

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STB_HIPROC")
    public static final int HI_PROCESSOR_SPECIFIC = 15;

    private ElfSymbolBinding(byte value) {
        super(value);
    }

    private ElfSymbolBinding(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSymbolBinding fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfSymbolBinding::new);
    }

    public static ElfSymbolBinding fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static ElfSymbolBinding fromSymbolInfo(byte info) {
        return fromValue((byte) (info >>> 4));
    }

    public static Collection<ElfSymbolBinding> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }


    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

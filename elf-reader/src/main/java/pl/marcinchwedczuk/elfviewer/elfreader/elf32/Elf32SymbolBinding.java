package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfMachine;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Elf32SymbolBinding extends BytePartialEnum<Elf32SymbolBinding> {
    private static final Map<Byte, Elf32SymbolBinding> byValue = mkByValueMap();
    private static final Map<String, Elf32SymbolBinding> byName = mkByNameMap();

    /**
     * Local symbols are not visible outside the object file containing their definition.
     * Local symbols of the same name may exist in multiple files without interfering with each other.
     */
    @ElfApi("STB_LOCAL")
    public static final Elf32SymbolBinding LOCAL = new Elf32SymbolBinding(b(0), "LOCAL");

    /**
     * Global symbols are visible to all object files being combined.
     * One file's definition of a global symbol will satisfy another file's
     * undefined reference to the same global symbol.
     */
    @ElfApi("STB_GLOBAL")
    public static final Elf32SymbolBinding GLOBAL = new Elf32SymbolBinding(b(1), "GLOBAL");

    /**
     * Weak symbols resemble global symbols, but their definitions have lower precedence.
     */
    @ElfApi("STB_WEAK")
    public static final Elf32SymbolBinding WEAK = new Elf32SymbolBinding(b(2), "WEAK");

    /**
     * Number of defined types.
     */
    @ElfApi("STB_NUM")
    public static final Elf32SymbolBinding NUMBER_DEFINED_TYPES = new Elf32SymbolBinding(b(3), "NUMBER_DEFINED_TYPES");

    /**
     * Number of defined types.
     */
    @ElfApi("STB_GNU_UNIQUE")
    public static final Elf32SymbolBinding GNU_UNIQUE = new Elf32SymbolBinding(b(10), "GNU_UNIQUE");

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

    private Elf32SymbolBinding(byte value) {
        super(value);
    }

    private Elf32SymbolBinding(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static Elf32SymbolBinding fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, Elf32SymbolBinding::new);
    }

    public static Elf32SymbolBinding fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Elf32SymbolBinding fromSymbolInfo(byte info) {
        return fromValue((byte) (info >>> 4));
    }

    public static Collection<Elf32SymbolBinding> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }


    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

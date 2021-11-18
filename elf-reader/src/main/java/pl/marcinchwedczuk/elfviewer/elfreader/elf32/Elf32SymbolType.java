package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;

public class Elf32SymbolType extends BytePartialEnum<Elf32SymbolType> {
    private static final Map<Byte, Elf32SymbolType> byValue = mkByValueMap();
    private static final Map<String, Elf32SymbolType> byName = mkByNameMap();

    /**
     * The symbol's type is not specified.
     */
    @ElfApi("STT_NOTYPE")
    public static Elf32SymbolType NOTYPE = new Elf32SymbolType(b(0), "NOTYPE");

    /**
     * The symbol is associated with a data object, such as a variable, an array, and so on.
     */
    @ElfApi("STT_OBJECT")
    public static Elf32SymbolType OBJECT = new Elf32SymbolType(b(1), "OBJECT");

    /**
     * The symbol is associated with a function or other executable code.
     */
    @ElfApi("STT_FUNC")
    public static Elf32SymbolType FUNCTION = new Elf32SymbolType(b(2), "FUNCTION");

    /**
     * The symbol is associated with a section. Symbol table entries of this type
     * exist primarily for relocation and normally have STB_LOCAL binding.
     */
    @ElfApi("STT_SECTION")
    public static Elf32SymbolType SECTION = new Elf32SymbolType(b(3), "SECTION");

    /**
     * A file symbol has STB_LOCAL binding, its section index is SHN_ABS,
     * and it precedes the other STB_LOCAL symbols for the file, if it is present.
     */
    @ElfApi("STT_FILE")
    public static Elf32SymbolType FILE = new Elf32SymbolType(b(4), "FILE");

    /**
     * Symbol is a common data object.
     */
    @ElfApi("STT_COMMON")
    public static Elf32SymbolType COMMON = new Elf32SymbolType(b(5), "COMMON");

    /**
     * Symbol is thread-local data object.
     */
    @ElfApi("STT_TLS")
    public static Elf32SymbolType THREAD_LOCAL_STORAGE = new Elf32SymbolType(b(6), "THREAD_LOCAL_STORAGE");

    /**
     * Number of defined types.
     */
    @ElfApi("STT_NUM")
    public static Elf32SymbolType NUMBER_OF_TYPES = new Elf32SymbolType(b(7), "NUMBER_OF_TYPES");

    /**
     * Symbol is indirect code object.
     */
    @ElfApi("STT_GNU_IFUNC")
    public static Elf32SymbolType INDIRECT_FUNCTION = new Elf32SymbolType(b(10), "INDIRECT_FUNCTION");

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

    private Elf32SymbolType(byte value) {
        super(value);
    }

    private Elf32SymbolType(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static Elf32SymbolType fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, Elf32SymbolType::new);
    }

    public static Elf32SymbolType fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Elf32SymbolType fromSymbolInfo(byte info) {
        return fromValue((byte)(info & 0x0f));
    }

    public static Collection<Elf32SymbolType> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }
}

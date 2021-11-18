package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;

public class Elf32SymbolVisibility extends BytePartialEnum<Elf32SymbolVisibility> {
    private static final Map<Byte, Elf32SymbolVisibility> byValue = mkByValueMap();
    private static final Map<String, Elf32SymbolVisibility> byName = mkByNameMap();

    /**
     * Default symbol visibility rules.  Global and weak
     * symbols are available to other modules; references
     * in the local module can be interposed by
     * definitions in other modules.
     */
    @ElfApi("STV_DEFAULT")
    public static Elf32SymbolVisibility DEFAULT = new Elf32SymbolVisibility(b(0), "DEFAULT");

    /**
     * Processor-specific hidden class.
     */
    @ElfApi("STV_INTERNAL")
    public static Elf32SymbolVisibility INTERNAL = new Elf32SymbolVisibility(b(1), "INTERNAL");

    /**
     * Symbol is unavailable to other modules; references
     * in the local module always resolve to the local
     * symbol (i.e., the symbol can't be interposed by
     * definitions in other modules).
     */
    @ElfApi("STV_HIDDEN")
    public static Elf32SymbolVisibility HIDDEN = new Elf32SymbolVisibility(b(2), "HIDDEN");

    /**
     * Symbol is available to other modules, but
     * references in the local module always resolve to
     * the local symbol.
     */
    @ElfApi("STV_PROTECTED")
    public static Elf32SymbolVisibility PROTECTED = new Elf32SymbolVisibility(b(3), "PROTECTED");

    private Elf32SymbolVisibility(byte value) {
        super(value);
    }

    private Elf32SymbolVisibility(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static Elf32SymbolVisibility fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, Elf32SymbolVisibility::new);
    }

    public static Elf32SymbolVisibility fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Elf32SymbolVisibility fromSymbolOther(byte other) {
        return fromValue((byte) (other & 0x03));
    }

    public static Collection<Elf32SymbolVisibility> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }
}

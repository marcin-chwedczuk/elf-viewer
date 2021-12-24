package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfSymbolVisibility extends BytePartialEnum<ElfSymbolVisibility> {
    private static final Map<Byte, ElfSymbolVisibility> byValue = mkByValueMap();
    private static final Map<String, ElfSymbolVisibility> byName = mkByNameMap();

    /**
     * Default symbol visibility rules.  Global and weak
     * symbols are available to other modules; references
     * in the local module can be interposed by
     * definitions in other modules.
     */
    @ElfApi("STV_DEFAULT")
    public static ElfSymbolVisibility DEFAULT = new ElfSymbolVisibility(b(0), "DEFAULT");

    /**
     * Processor-specific hidden class.
     */
    @ElfApi("STV_INTERNAL")
    public static ElfSymbolVisibility INTERNAL = new ElfSymbolVisibility(b(1), "INTERNAL");

    /**
     * Symbol is unavailable to other modules; references
     * in the local module always resolve to the local
     * symbol (i.e., the symbol can't be interposed by
     * definitions in other modules).
     */
    @ElfApi("STV_HIDDEN")
    public static ElfSymbolVisibility HIDDEN = new ElfSymbolVisibility(b(2), "HIDDEN");

    /**
     * Symbol is available to other modules, but
     * references in the local module always resolve to
     * the local symbol.
     */
    @ElfApi("STV_PROTECTED")
    public static ElfSymbolVisibility PROTECTED = new ElfSymbolVisibility(b(3), "PROTECTED");

    private ElfSymbolVisibility(byte value) {
        super(value);
    }

    private ElfSymbolVisibility(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSymbolVisibility fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfSymbolVisibility::new);
    }

    public static ElfSymbolVisibility fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static ElfSymbolVisibility fromSymbolOther(byte other) {
        return fromValue((byte) (other & 0x03));
    }

    public static Collection<ElfSymbolVisibility> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

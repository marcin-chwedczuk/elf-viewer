package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfSymbolVersion extends ShortPartialEnum<ElfSymbolVersion> {
    private static final Map<Short, ElfSymbolVersion> byValue = mkByValueMap();
    private static final Map<String, ElfSymbolVersion> byName = mkByNameMap();

    /**
     * Symbol has local scope.
     */
    @ElfApi("VER_NDX_LOCAL")
    public static final ElfSymbolVersion LOCAL = new ElfSymbolVersion(s(0), "LOCAL");

    /**
     * Symbol has global scope and is assigned to the base version definition.
     */
    @ElfApi("VER_NDX_GLOBAL")
    public static final ElfSymbolVersion GLOBAL = new ElfSymbolVersion(s(1), "GLOBAL");

    /**
     * Symbol is to be eliminated.
     */
    @ElfApi("VER_NDX_ELIMINATE")
    public static final ElfSymbolVersion ELIMINATE = new ElfSymbolVersion(s(0xff01), "ELIMINATE");

    @ElfApi("VER_NDX_LORESERVE")
    public static final short LO_RESERVED = s(0xff00);
    // There is no upper bound

    private ElfSymbolVersion(short value) {
        super(value);
    }

    private ElfSymbolVersion(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfSymbolVersion fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfSymbolVersion::new);
    }

    public static ElfSymbolVersion fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfSymbolVersion> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}
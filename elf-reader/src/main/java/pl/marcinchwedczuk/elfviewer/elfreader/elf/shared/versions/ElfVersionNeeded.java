package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfVersionNeeded extends ShortPartialEnum<ElfVersionNeeded> {
    private static final Map<Short, ElfVersionNeeded> byValue = mkByValueMap();
    private static final Map<String, ElfVersionNeeded> byName = mkByNameMap();

    /**
     * Invalid version.
     */
    @ElfApi("VER_NEED_NONE")
    public static final ElfVersionNeeded NONE = new ElfVersionNeeded(s(0), "NONE");

    /**
     * Current version.
     */
    @ElfApi("VER_NEED_CURRENT")
    public static final ElfVersionNeeded CURRENT = new ElfVersionNeeded(s(1), "CURRENT");

    private ElfVersionNeeded(short value) {
        super(value);
    }

    private ElfVersionNeeded(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfVersionNeeded fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfVersionNeeded::new);
    }

    public static ElfVersionNeeded fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfVersionNeeded> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}
package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfVersionNeededRevision extends ShortPartialEnum<ElfVersionNeededRevision> {
    private static final Map<Short, ElfVersionNeededRevision> byValue = mkByValueMap();
    private static final Map<String, ElfVersionNeededRevision> byName = mkByNameMap();

    /**
     * Invalid version.
     */
    @ElfApi("VER_NEED_NONE")
    public static final ElfVersionNeededRevision NONE = new ElfVersionNeededRevision(s(0), "NONE");

    /**
     * Current version.
     */
    @ElfApi("VER_NEED_CURRENT")
    public static final ElfVersionNeededRevision CURRENT = new ElfVersionNeededRevision(s(1), "CURRENT");

    private ElfVersionNeededRevision(short value) {
        super(value);
    }

    private ElfVersionNeededRevision(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfVersionNeededRevision fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfVersionNeededRevision::new);
    }

    public static ElfVersionNeededRevision fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfVersionNeededRevision> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}
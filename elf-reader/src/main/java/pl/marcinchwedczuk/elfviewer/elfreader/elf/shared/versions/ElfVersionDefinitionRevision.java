package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfVersionDefinitionRevision extends ShortPartialEnum<ElfVersionDefinitionRevision> {
    private static final Map<Short, ElfVersionDefinitionRevision> byValue = mkByValueMap();
    private static final Map<String, ElfVersionDefinitionRevision> byName = mkByNameMap();

    /**
     * Invalid version.
     */
    @ElfApi("VER_NEED_NONE")
    public static final ElfVersionDefinitionRevision NONE = new ElfVersionDefinitionRevision(s(0), "NONE");

    /**
     * Current version.
     */
    @ElfApi("VER_NEED_CURRENT")
    public static final ElfVersionDefinitionRevision CURRENT = new ElfVersionDefinitionRevision(s(1), "CURRENT");

    private ElfVersionDefinitionRevision(short value) {
        super(value);
    }

    private ElfVersionDefinitionRevision(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfVersionDefinitionRevision fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfVersionDefinitionRevision::new);
    }

    public static ElfVersionDefinitionRevision fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfVersionDefinitionRevision> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}
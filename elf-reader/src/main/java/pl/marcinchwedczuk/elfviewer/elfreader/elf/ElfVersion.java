package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The value 1 signifies the original file format;
 * extensions will create new versions with higher numbers.
 * The value of EV_CURRENT, though given as 1 above,
 * will change as necessary to reflect the current version number.
 */
public class ElfVersion extends IntPartialEnum<ElfVersion> {
    private static final Map<Integer, ElfVersion> byValue = mkByValueMap();
    private static final Map<String, ElfVersion> byName = mkByNameMap();

    /**
     * Invalid version
     */
    @ElfApi("EV_NONE")
    public static final ElfVersion NONE = new ElfVersion(0, "NONE");

    /**
     * Current version
     */
    @ElfApi("EV_CURRENT")
    public static final ElfVersion CURRENT = new ElfVersion(1, "CURRENT");

    private ElfVersion(int value) {
        super(value);
    }

    private ElfVersion(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfVersion fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfVersion::new);
    }

    public static ElfVersion fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfVersion> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

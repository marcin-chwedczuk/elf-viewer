package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Applicable to notes without name.
 */
public class ElfNoteTypeDefault extends IntPartialEnum<ElfNoteTypeDefault> {
    private static final Map<Integer, ElfNoteTypeDefault> byValue = mkByValueMap();
    private static final Map<String, ElfNoteTypeDefault> byName = mkByNameMap();

    @ElfApi("NT_VERSION")
    public static final ElfNoteTypeDefault VERSION = new ElfNoteTypeDefault(1, "VERSION");

    @ElfApi("NT_ARCH")
    public static final ElfNoteTypeDefault ARCH = new ElfNoteTypeDefault(2, "ARCH");

    protected ElfNoteTypeDefault(int value) {
        super(value);
    }

    protected ElfNoteTypeDefault(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfNoteTypeDefault fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfNoteTypeDefault::new);
    }

    public static ElfNoteTypeDefault fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfNoteTypeDefault> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

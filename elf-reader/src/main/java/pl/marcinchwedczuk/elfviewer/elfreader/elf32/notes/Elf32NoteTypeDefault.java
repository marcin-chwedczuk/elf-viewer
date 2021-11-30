package pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Applicable to notes without name.
 */
public class Elf32NoteTypeDefault extends IntPartialEnum<Elf32NoteTypeDefault> {
    private static final Map<Integer, Elf32NoteTypeDefault> byValue = mkByValueMap();
    private static final Map<String, Elf32NoteTypeDefault> byName = mkByNameMap();

    @ElfApi("NT_VERSION")
    public static final Elf32NoteTypeDefault VERSION = new Elf32NoteTypeDefault(1, "VERSION");

    @ElfApi("NT_ARCH")
    public static final Elf32NoteTypeDefault ARCH = new Elf32NoteTypeDefault(2, "ARCH");

    protected Elf32NoteTypeDefault(int value) {
        super(value);
    }

    protected Elf32NoteTypeDefault(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static Elf32NoteTypeDefault fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, Elf32NoteTypeDefault::new);
    }

    public static Elf32NoteTypeDefault fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<Elf32NoteTypeDefault> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfClass extends BytePartialEnum<ElfClass> {
    private static final Map<Byte, ElfClass> byValue = mkByValueMap();
    private static final Map<String, ElfClass> byName = mkByNameMap();

    /**
     * This class is invalid.
     */
    @ElfApi("ELFCLASSNONE")
    public static final ElfClass ELF_CLASS_NONE = new ElfClass(b(0), "ELF_CLASS_NONE");

    /**
     * This defines the 32-bit architecture.  It
     * supports machines with files and virtual
     * address spaces up to 4 Gigabytes.
     */
    @ElfApi("ELFCLASS32")
    public static final ElfClass ELF_CLASS_32 = new ElfClass(b(1), "ELF_CLASS_32");

    /**
     * This defines the 64-bit architecture.
     */
    @ElfApi("ELFCLASS64")
    public static final ElfClass ELF_CLASS_64 = new ElfClass(b(2), "ELF_CLASS_64");

    private ElfClass(byte value) {
        super(value);
    }

    private ElfClass(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfClass fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfClass::new);
    }

    public static ElfClass fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<ElfClass> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }


    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

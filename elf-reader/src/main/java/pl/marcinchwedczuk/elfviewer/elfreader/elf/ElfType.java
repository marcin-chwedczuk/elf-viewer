package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfType extends ShortPartialEnum<ElfType> {
    private static final Map<Short, ElfType> byValue = mkByValueMap();
    private static final Map<String, ElfType> byName = mkByNameMap();

    @ElfApi("ET_NONE")
    public static final ElfType NONE = new ElfType(s(0), "NONE");

    @ElfApi("ET_REL")
    public static final ElfType RELOCATABLE = new ElfType(s(1), "RELOCATABLE");

    @ElfApi("ET_EXEC")
    public static final ElfType EXECUTABLE = new ElfType(s(2), "EXECUTABLE");

    @ElfApi("ET_DYN")
    public static final ElfType SHARED_OBJECT = new ElfType(s(3), "SHARED_OBJECT");

    @ElfApi("ET_CORE")
    public static final ElfType COREDUMP = new ElfType(s(4), "COREDUMP");

    @ElfApi("ET_LOOS")
    public static final short LO_OS_SPECIFIC = s(0xfe00);
    @ElfApi("ET_HIOS")
    public static final short HI_OS_SPECIFIC = s(0xfeff);

    @ElfApi("ET_LOPROC")
    public static final short LO_PROCESSOR_SPECIFIC = s(0xff00);
    @ElfApi("ET_HIPROC")
    public static final short HI_PROCESSOR_SPECIFIC = s(0xffff);

    private ElfType(short value) {
        super(value);
    }

    private ElfType(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfType fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfType::new);
    }

    public static ElfType fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfType> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

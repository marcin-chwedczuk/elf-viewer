package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfClass;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfOsAbi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * For details see: https://fedoraproject.org/wiki/Toolchain/Watermark
 */
public class ElfGnuBuildAttribute extends BytePartialEnum<ElfGnuBuildAttribute> {
    private static final Map<Byte, ElfGnuBuildAttribute> byValue = mkByValueMap();
    private static final Map<String, ElfGnuBuildAttribute> byName = mkByNameMap();

    /**
     * Version of the specification supported and producer(s) of the notes.
     */
    public static final ElfGnuBuildAttribute SPECIFICATION_VERSION = new ElfGnuBuildAttribute(b(1), "SPECIFICATION_VERSION");

    /**
     * -fstack-protector status.
     */
    public static final ElfGnuBuildAttribute STACK_PROTECTOR = new ElfGnuBuildAttribute(b(2), "STACK_PROTECTOR");

    public static final ElfGnuBuildAttribute RELRO = new ElfGnuBuildAttribute(b(3), "RELRO");

    /**
     * Stack size.
     */
    public static final ElfGnuBuildAttribute STACK_SIZE = new ElfGnuBuildAttribute(b(4), "STACK_SIZE");

    /**
     * Build tool & version.
     */
    public static final ElfGnuBuildAttribute BUILD_TOOL_VERSION = new ElfGnuBuildAttribute(b(5), "BUILD_TOOL_VERSION");

    /**
     * ABI
     */
    public static final ElfGnuBuildAttribute ABI = new ElfGnuBuildAttribute(b(6), "ABI");

    /**
     * Position Independence (0=>static, 1=>pic, 2=>PIC, 3=>pie).
     */
    public static final ElfGnuBuildAttribute POSITION_INDEPENDENCE = new ElfGnuBuildAttribute(b(7), "POSITION_INDEPENDENCE");

    /**
     * Short enums.
     */
    public static final ElfGnuBuildAttribute SHORT_ENUMS = new ElfGnuBuildAttribute(b(8), "SHORT_ENUMS");

    protected ElfGnuBuildAttribute(byte value) {
        super(value);
    }

    protected ElfGnuBuildAttribute(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfGnuBuildAttribute fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfGnuBuildAttribute::new);
    }

    public static ElfGnuBuildAttribute fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<ElfGnuBuildAttribute> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

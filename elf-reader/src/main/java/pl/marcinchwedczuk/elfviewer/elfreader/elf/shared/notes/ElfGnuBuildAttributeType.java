package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * For details see: https://fedoraproject.org/wiki/Toolchain/Watermark
 */
public class ElfGnuBuildAttributeType extends BytePartialEnum<ElfGnuBuildAttributeType> {
    private static final Map<Byte, ElfGnuBuildAttributeType> byValue = mkByValueMap();
    private static final Map<String, ElfGnuBuildAttributeType> byName = mkByNameMap();

    /**
     * Version of the specification supported and producer(s) of the notes.
     */
    public static final ElfGnuBuildAttributeType SPECIFICATION_VERSION = new ElfGnuBuildAttributeType(b(1), "SPECIFICATION_VERSION");

    /**
     * -fstack-protector status.
     */
    public static final ElfGnuBuildAttributeType STACK_PROTECTOR = new ElfGnuBuildAttributeType(b(2), "STACK_PROTECTOR");

    public static final ElfGnuBuildAttributeType RELRO = new ElfGnuBuildAttributeType(b(3), "RELRO");

    /**
     * Stack size.
     */
    public static final ElfGnuBuildAttributeType STACK_SIZE = new ElfGnuBuildAttributeType(b(4), "STACK_SIZE");

    /**
     * Build tool & version.
     */
    public static final ElfGnuBuildAttributeType BUILD_TOOL_VERSION = new ElfGnuBuildAttributeType(b(5), "BUILD_TOOL_VERSION");

    /**
     * ABI
     */
    public static final ElfGnuBuildAttributeType ABI = new ElfGnuBuildAttributeType(b(6), "ABI");

    /**
     * Position Independence (0=>static, 1=>pic, 2=>PIC, 3=>pie).
     */
    public static final ElfGnuBuildAttributeType POSITION_INDEPENDENCE = new ElfGnuBuildAttributeType(b(7), "POSITION_INDEPENDENCE");

    /**
     * Short enums.
     */
    public static final ElfGnuBuildAttributeType SHORT_ENUMS = new ElfGnuBuildAttributeType(b(8), "SHORT_ENUMS");

    protected ElfGnuBuildAttributeType(byte value) {
        super(value);
    }

    protected ElfGnuBuildAttributeType(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfGnuBuildAttributeType fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfGnuBuildAttributeType::new);
    }

    public static ElfGnuBuildAttributeType fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<ElfGnuBuildAttributeType> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

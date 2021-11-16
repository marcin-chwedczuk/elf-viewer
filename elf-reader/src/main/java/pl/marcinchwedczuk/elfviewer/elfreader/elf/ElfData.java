package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;

public class ElfData extends BytePartialEnum<ElfData> {
    private static final Map<Byte, ElfData> byValue = mkByValueMap();
    private static final Map<String, ElfData> byName = mkByNameMap();

    /**
     * Unknown data format.
     */
    public static final ElfData ELF_DATA_NONE = new ElfData(b(0), "ELFDATANONE");

    /**
     * Two's complement, little-endian.
     */
    public static final ElfData ELF_DATA_LSB = new ElfData(b(1), "ELFDATA2LSB");

    /**
     * Two's complement, big-endian.
     */
    public static final ElfData ELF_DATA_MSB = new ElfData(b(2), "ELFDATA2MSB");

    private ElfData(byte value) {
        super(value);
    }

    private ElfData(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfData fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfData::new);
    }

    public static ElfData fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<ElfData> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;

public class ElfOsAbi extends BytePartialEnum<ElfOsAbi> {
    private static final Map<Byte, ElfOsAbi> byValue = mkByValueMap();
    private static final Map<String, ElfOsAbi> byName = mkByNameMap();

    @ElfApi("ELFOSABI_NONE")
    public static final ElfOsAbi NONE = new ElfOsAbi(b(0), "NONE");

    /**
     * Alias for NONE.
     */
    @ElfApi("ELFOSABI_SYSV")
    public static final ElfOsAbi SYSTEM_V = NONE;

    @ElfApi("ELFOSABI_HPUX")
    public static final ElfOsAbi HPUX = new ElfOsAbi(b(1), "HPUX");

    @ElfApi("ELFOSABI_NETBSD")
    public static final ElfOsAbi NETBSD = new ElfOsAbi(b(2), "NETBSD");

    @ElfApi("ELFOSABI_GNU")
    public static final ElfOsAbi GNU = new ElfOsAbi(b(3), "GNU");

    /**
     * Alias for GNU.
     */
    @ElfApi("ELFOSABI_LINUX")
    public static final ElfOsAbi LINUX = GNU;

    @ElfApi("ELFOSABI_SOLARIS")
    public static final ElfOsAbi SOLARIS = new ElfOsAbi(b(6), "SOLARIS");

    @ElfApi("ELFOSABI_AIX")
    public static final ElfOsAbi AIX = new ElfOsAbi(b(7), "AIX");

    @ElfApi("ELFOSABI_IRIX")
    public static final ElfOsAbi IRIX = new ElfOsAbi(b(8), "IRIX");

    @ElfApi("ELFOSABI_FREEBSD")
    public static final ElfOsAbi FREEBSD = new ElfOsAbi(b(9), "FREEBSD");

    @ElfApi("ELFOSABI_TRU64")
    public static final ElfOsAbi TRU64 = new ElfOsAbi(b(10), "TRU64");

    @ElfApi("ELFOSABI_MODESTO")
    public static final ElfOsAbi MODESTO = new ElfOsAbi(b(11), "MODESTO");

    @ElfApi("ELFOSABI_OPENBSD")
    public static final ElfOsAbi OPENBSD = new ElfOsAbi(b(12), "OPENBSD");

    @ElfApi("ELFOSABI_ARM_AEABI")
    public static final ElfOsAbi ARM_AEABI = new ElfOsAbi(b(64), "ARM_AEABI");

    @ElfApi("ELFOSABI_ARM")
    public static final ElfOsAbi ARM = new ElfOsAbi(b(97), "ARM");

    @ElfApi("ELFOSABI_STANDALONE")
    public static final ElfOsAbi STANDALONE = new ElfOsAbi(b(255), "STANDALONE");

    private ElfOsAbi(byte value) {
        super(value);
    }

    private ElfOsAbi(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfOsAbi fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, ElfOsAbi::new);
    }

    public static ElfOsAbi fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<ElfOsAbi> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }
}

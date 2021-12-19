package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Flag;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BitFlags;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Mask;

import java.util.Collection;
import java.util.List;

public class SectionAttributes extends BitFlags<SectionAttributes> {
    /**
     * The section contains data that should be writable during process execution.
     */
    @ElfApi("SHF_WRITE")
    public static final Flag<SectionAttributes> WRITE = flag("WRITE", 1L << 0);

    /**
     * The section occupies memory during process execution.
     * Some control sections do not reside in the memory image of an object file;
     * this attribute is off for those sections.
     */
    @ElfApi("SHF_ALLOC")
    public static final Flag<SectionAttributes> ALLOCATE = flag("ALLOC", 1L << 1);

    /**
     * The section contains executable machine instructions.
     */
    @ElfApi("SHF_EXECINSTR")
    public static final Flag<SectionAttributes> EXECUTABLE = flag("EXECINSTR", 1L << 2);

    // GNU specific:
    // https://github.com/lattera/glibc/blob/master/elf/elf.h

    /**
     * Might be merged
     */
    @ElfApi("SHF_MERGE")
    public static final Flag<SectionAttributes> MERGABLE = flag("MERGE", 1L << 4);

    /**
     * Contains nul-terminated strings
     */
    @ElfApi("SHF_STRINGS")
    public static final Flag<SectionAttributes> STRINGS = flag("STRINGS", 1L << 5);

    /**
     * `sh_info' contains SHT index
     */
    @ElfApi("SHF_INFO_LINK")
    public static final Flag<SectionAttributes> INFO_LINK = flag("INFO_LINK", 1L << 6);

    /**
     * Preserve order after combining
     */
    @ElfApi("SHF_LINK_ORDER")
    public static final Flag<SectionAttributes> LINK_ORDER = flag("LINK_ORDER", 1L << 7);

    /**
     * Non-standard OS specific handling
     * required
     */
    @ElfApi("SHF_OS_NONCONFORMING")
    public static final Flag<SectionAttributes> OS_NONCONFORMING = flag("OS_NONCONFORMING", 1L << 8);

    /**
     * Section is member of a group
     */
    @ElfApi("SHF_GROUP")
    public static final Flag<SectionAttributes> GROUP_MEMBER = flag("GROUP", 1L << 9);

    /**
     * Section hold thread-local data
     */
    @ElfApi("SHF_TLS")
    public static final Flag<SectionAttributes> THREAD_LOCAL_STORAGE = flag("TLS", 1L << 10);

    /**
     * Section with compressed data
     */
    @ElfApi("SHF_COMPRESSED")
    public static final Flag<SectionAttributes> COMPRESSED = flag("COMPRESSED", 1L << 11);

    /**
     * Mask for OS-specific flags
     */
    @ElfApi("SHF_MASKOS")
    public static final Mask<SectionAttributes> OS_SPECIFIC_MASK = mask(0x0ff00000L);

    /**
     * All bits included in this mask are reserved for processor-specific semantics.
     */
    @ElfApi("SHF_MASKPROC")
    public static final Mask<SectionAttributes> PROCESSOR_SPECIFIC_MASK = mask(0xf0000000L);

    /**
     * Special ordering requirement (Solaris).
     */
    @ElfApi("SHF_ORDERED")
    public static final Flag<SectionAttributes> ORDERED = flag("ORDERED", 1L << 30);

    /**
     * Section is excluded unless referenced or allocated (Solaris).
     */
    @ElfApi("SHF_EXCLUDE")
    public static final Flag<SectionAttributes> EXCLUDED = flag("EXCLUDE", 1L << 31);

    public SectionAttributes(long init) {
        super(init);
    }

    @SafeVarargs
    public SectionAttributes(Flag<SectionAttributes>... flags) {
        super(flags);
    }

    @Override
    protected SectionAttributes mkCopy(long newRaw) {
        return new SectionAttributes(Math.toIntExact(newRaw));
    }

    @Override
    public Collection<Flag<SectionAttributes>> flags() {
        return List.of(
                WRITE,
                ALLOCATE,
                EXECUTABLE,
                MERGABLE,
                STRINGS,
                INFO_LINK,
                LINK_ORDER,
                OS_NONCONFORMING,
                GROUP_MEMBER,
                THREAD_LOCAL_STORAGE,
                COMPRESSED,
                ORDERED,
                EXCLUDED
        );
    }
}

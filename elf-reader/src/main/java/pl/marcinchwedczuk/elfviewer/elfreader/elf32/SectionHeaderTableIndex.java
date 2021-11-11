package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

/**
 * Represents an index into Section Header Table.
 */
public class SectionHeaderTableIndex {
    /**
     * This value marks an undefined, missing, irrelevant, or otherwise meaningless section reference.
     * For example, a symbol "defined" relative to section number SHN_UNDEF is an undefined symbol.
     */
    @ElfApi("SHN_UNDEF")
    public static final SectionHeaderTableIndex Undefined = new SectionHeaderTableIndex(0);

    /**
     * This value specifies the lower bound of the range of reserved indexes.
     */
    @ElfApi("SHN_LORESERVE")
    public static final SectionHeaderTableIndex LoReserved = new SectionHeaderTableIndex(0xff00);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_LOPROC")
    public static final SectionHeaderTableIndex LoProcessorSpecific = new SectionHeaderTableIndex(0xff00);

    /**
     * Order section before all others (Solaris).
     */
    @ElfApi("SHN_BEFORE")
    public static final SectionHeaderTableIndex Before = new SectionHeaderTableIndex(0xff00);

    /**
     * Order section after all others (Solaris).
     */
    @ElfApi("SHN_AFTER")
    public static final SectionHeaderTableIndex After = new SectionHeaderTableIndex(0xff01);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_HIPROC")
    public static final SectionHeaderTableIndex HiProcessorSpecific = new SectionHeaderTableIndex(0xff1f);

    /**
     * Start of OS-specific
     */
    @ElfApi("SHN_LOOS")
    public static final SectionHeaderTableIndex LoOsSpecific = new SectionHeaderTableIndex(0xff20);

    /**
     * End of OS-specific
     */
    @ElfApi("SHN_HIOS")
    public static final SectionHeaderTableIndex HiOsSpecific = new SectionHeaderTableIndex(0xff3f);


    /**
     * This value specifies absolute values for the corresponding reference.
     * For example, symbols defined relative to section number SHN_ABS have
     * absolute values and are not affected by relocation.
     */
    @ElfApi("SHN_ABS")
    public static final SectionHeaderTableIndex Absolute = new SectionHeaderTableIndex(0xfff1);

    /**
     * This value specifies absolute values for the corresponding reference.
     * For example, symbols defined relative to section number SHN_ABS have
     * absolute values and are not affected by relocation.
     */
    @ElfApi("SHN_COMMON")
    public static final SectionHeaderTableIndex Common = new SectionHeaderTableIndex(0xfff2);

    /**
     * Index is in extra table.
     */
    @ElfApi("SHN_XINDEX")
    public static final SectionHeaderTableIndex XIndex = new SectionHeaderTableIndex(0xffff);

    /**
     * This value specifies the upper bound of the range of reserved indexes.
     * The system reserves indexes between SHN_LORESERVE and SHN_HIRESERVE, inclusive;
     * the values do not reference the section header table.
     * That is, the section header table does not contain entries for the reserved indexes.
     */
    @ElfApi("SHN_HIRESERVE")
    public static final SectionHeaderTableIndex HiReserved = new SectionHeaderTableIndex(0xffff);

    private final int index;

    public SectionHeaderTableIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException();
        this.index = index;
    }

    public SectionHeaderTableIndex(short unsignedShort) {
        this(Short.toUnsignedInt(unsignedShort));
    }

    public int intValue() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionHeaderTableIndex that = (SectionHeaderTableIndex) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        if (this.equals(Undefined)) return "Undefined";
        if (this.equals(Absolute)) return "Absolute";
        if (this.equals(Common)) return "Common";
        if (this.equals(XIndex)) return "XIndex";
        if (this.equals(Before)) return "Before";
        if (this.equals(After)) return "After";

        return "0x" + Integer.toHexString(index);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

/**
 * Represents an index into Section Header Table.
 */
public class SHTIndex {
    /**
     * This value marks an undefined, missing, irrelevant, or otherwise meaningless section reference.
     * For example, a symbol "defined" relative to section number SHN_UNDEF is an undefined symbol.
     */
    @ElfApi("SHN_UNDEF")
    public static final SHTIndex Undefined = new SHTIndex(0);

    /**
     * This value specifies the lower bound of the range of reserved indexes.
     */
    @ElfApi("SHN_LORESERVE")
    public static final SHTIndex LoReserved = new SHTIndex(0xff00);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_LOPROC")
    public static final SHTIndex LoProcessor = new SHTIndex(0xff00);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_HIPROC")
    public static final SHTIndex HiProcessor = new SHTIndex(0xff1f);

    /**
     * This value specifies absolute values for the corresponding reference.
     * For example, symbols defined relative to section number SHN_ABS have
     * absolute values and are not affected by relocation.
     */
    @ElfApi("SHN_ABS")
    public static final SHTIndex Absolute = new SHTIndex(0xfff1);

    /**
     * This value specifies absolute values for the corresponding reference.
     * For example, symbols defined relative to section number SHN_ABS have
     * absolute values and are not affected by relocation.
     */
    @ElfApi("SHN_COMMON")
    public static final SHTIndex Common = new SHTIndex(0xfff2);

    /**
     * This value specifies the upper bound of the range of reserved indexes.
     * The system reserves indexes between SHN_LORESERVE and SHN_HIRESERVE, inclusive;
     * the values do not reference the section header table.
     * That is, the section header table does not contain entries for the reserved indexes.
     */
    @ElfApi("SHN_HIRESERVE")
    public static final SHTIndex HiReserved = new SHTIndex(0xffff);

    private final int index;

    public SHTIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException();
        this.index = index;
    }

    public int intValue() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SHTIndex that = (SHTIndex) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString(index);
    }
}

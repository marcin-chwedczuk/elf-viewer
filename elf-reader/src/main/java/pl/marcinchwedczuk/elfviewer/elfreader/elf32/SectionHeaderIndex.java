package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

/**
 * Represents an index into Section Header Table.
 */
public class SectionHeaderIndex {
    // TODO: Consider moving to common as this is the same for 32 and 64 bits

    /**
     * This value marks an undefined, missing, irrelevant, or otherwise meaningless section reference.
     * For example, a symbol "defined" relative to section number SHN_UNDEF is an undefined symbol.
     */
    @ElfApi("SHN_UNDEF")
    public static final SectionHeaderIndex UNDEFINED = new SectionHeaderIndex(0);

    /**
     * This value specifies the lower bound of the range of reserved indexes.
     */
    @ElfApi("SHN_LORESERVE")
    public static final SectionHeaderIndex LO_RESERVED = new SectionHeaderIndex(0xff00);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_LOPROC")
    public static final SectionHeaderIndex LO_PROCESSOR_SPECIFIC = new SectionHeaderIndex(0xff00);

    /**
     * Order section before all others (Solaris).
     */
    @ElfApi("SHN_BEFORE")
    public static final SectionHeaderIndex BEFORE = new SectionHeaderIndex(0xff00);

    /**
     * Order section after all others (Solaris).
     */
    @ElfApi("SHN_AFTER")
    public static final SectionHeaderIndex AFTER = new SectionHeaderIndex(0xff01);

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("SHN_HIPROC")
    public static final SectionHeaderIndex HI_PROCESSOR_SPECIFIC = new SectionHeaderIndex(0xff1f);

    /**
     * Start of OS-specific
     */
    @ElfApi("SHN_LOOS")
    public static final SectionHeaderIndex LO_OS_SPECIFIC = new SectionHeaderIndex(0xff20);

    /**
     * End of OS-specific
     */
    @ElfApi("SHN_HIOS")
    public static final SectionHeaderIndex HI_OS_SPECIFIC = new SectionHeaderIndex(0xff3f);


    /**
     * This value specifies absolute values for the corresponding reference.
     * For example, symbols defined relative to section number SHN_ABS have
     * absolute values and are not affected by relocation.
     */
    @ElfApi("SHN_ABS")
    public static final SectionHeaderIndex ABSOLUTE = new SectionHeaderIndex(0xfff1);

    /**
     * Symbols defined relative to this section are common
     * symbols, such as FORTRAN COMMON or unallocated C external
     * variables.
     */
    @ElfApi("SHN_COMMON")
    public static final SectionHeaderIndex COMMON = new SectionHeaderIndex(0xfff2);

    /**
     * This value is an escape value. It indicates that the actual section header
     * index is too large to fit in the containing field and is to be found
     * in another location (specific to the structure where it appears).
     */
    @ElfApi("SHN_XINDEX")
    public static final SectionHeaderIndex XINDEX = new SectionHeaderIndex(0xffff);

    /**
     * This value specifies the upper bound of the range of reserved indexes.
     * The system reserves indexes between SHN_LORESERVE and SHN_HIRESERVE, inclusive;
     * the values do not reference the section header table.
     * That is, the section header table does not contain entries for the reserved indexes.
     */
    @ElfApi("SHN_HIRESERVE")
    public static final SectionHeaderIndex HI_RESERVED = new SectionHeaderIndex(0xffff);

    private final int index;

    public SectionHeaderIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException();
        this.index = index;
    }

    public SectionHeaderIndex(short unsignedShort) {
        this(Short.toUnsignedInt(unsignedShort));
    }

    public int intValue() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionHeaderIndex that = (SectionHeaderIndex) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        if (this.equals(UNDEFINED)) return "UNDEFINED";
        if (this.equals(ABSOLUTE)) return "ABSOLUTE";
        if (this.equals(COMMON)) return "COMMON";
        if (this.equals(XINDEX)) return "XINDEX";
        if (this.equals(BEFORE)) return "BEFORE";
        if (this.equals(AFTER)) return "AFTER";

        return String.format("0x%08x", index);
    }
}

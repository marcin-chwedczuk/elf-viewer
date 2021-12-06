package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

public abstract class ElfRelocationAddend<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("r_offset")
    private final ElfAddress<NATIVE_WORD> offset;

    @ElfApi("r_info")
    private final NATIVE_WORD info;

    @ElfApi("r_addend")
    private final NATIVE_WORD addend;

    public ElfRelocationAddend(ElfAddress<NATIVE_WORD> offset,
                               NATIVE_WORD info,
                               NATIVE_WORD addend) {
        this.offset = offset;
        this.info = info;
        this.addend = addend;
    }

    /**
     * This member gives the location at which to apply the
     * relocation action.  For a relocatable file, the value is
     * the byte offset from the beginning of the section to the
     * storage unit affected by the relocation.  For an
     * executable file or shared object, the value is the virtual
     * address of the storage unit affected by the relocation.
     */
    public ElfAddress<NATIVE_WORD> offset() {
        return offset;
    }

    /**
     * This member gives both the symbol table index with respect
     *               to which the relocation must be made and the type of
     *               relocation to apply.  Relocation types are processor-
     *               specific.  When the text refers to a relocation entry's
     *               relocation type or symbol table index, it means the result
     *               of applying ELF[32|64]_R_TYPE or ELF[32|64]_R_SYM,
     *               respectively, to the entry's r_info member.
     */
    public NATIVE_WORD info() {
        return info;
    }

    // TODO: JavaDoc
    public NATIVE_WORD addend() { return addend; }

    public abstract int symbol();
    public abstract int type();

    public Intel386RelocationType intel386RelocationType() {
        return Intel386RelocationType.fromType(type());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElfRelocationAddend that = (ElfRelocationAddend) o;
        return info == that.info && Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, info);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", offset, info, addend);
    }

    public static class ElfRelocationAddend32 extends ElfRelocationAddend<Integer> {
        public ElfRelocationAddend32(ElfAddress<Integer> offset,
                                     Integer info,
                                     Integer addend) {
            super(offset, info, addend);
        }

        public int symbol() {
            return (info() >>> 8) & 0xff;
        }

        public int type() {
            return (info() & 0xff);
        }
    }

    public static class ElfRelocationAddend64 extends ElfRelocationAddend<Long> {
        public ElfRelocationAddend64(ElfAddress<Long> offset,
                                     Long info,
                                     Long addend) {
            super(offset, info, addend);
        }

        public int symbol() {
            return (int)(info() >> 32);
        }

        public int type() {
            return (int)(info() & 0xffffffffL);
        }
    }
}

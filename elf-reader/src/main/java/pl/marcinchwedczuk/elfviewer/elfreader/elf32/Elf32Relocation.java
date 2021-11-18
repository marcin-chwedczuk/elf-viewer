package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Rel")
public class Elf32Relocation {
    @ElfApi("r_offset")
    private final Elf32Address offset;
    @ElfApi("r_info")
    private final int info;

    public Elf32Relocation(Elf32Address offset, int info) {
        this.offset = offset;
        this.info = info;
    }

    /**
     * This member gives the location at which to apply the
     * relocation action.  For a relocatable file, the value is
     * the byte offset from the beginning of the section to the
     * storage unit affected by the relocation.  For an
     * executable file or shared object, the value is the virtual
     * address of the storage unit affected by the relocation.
     */
    public Elf32Address offset() {
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
    public int info() {
        return info;
    }

    public int symbol() {
        return (info >>> 8) & 0xff;
    }

    public int type() {
        return (info & 0xff);
    }

    public Intel386RelocationType intel386RelocationType() {
        return Intel386RelocationType.fromType(type());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32Relocation that = (Elf32Relocation) o;
        return info == that.info && Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, info);
    }

    @Override
    public String toString() {
        return String.format("%s 0x%08x", offset, info);
    }
}

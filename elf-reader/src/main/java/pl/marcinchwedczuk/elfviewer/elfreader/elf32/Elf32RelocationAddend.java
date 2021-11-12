package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Rela")
public class Elf32RelocationAddend {
    @ElfApi("r_offset")
    private final Elf32Address offset;

    @ElfApi("r_info")
    private final int info;

    @ElfApi("r_addend")
    private final int addend;

    public Elf32RelocationAddend(Elf32Address offset,
                                 int info,
                                 int addend) {
        this.offset = offset;
        this.info = info;
        this.addend = addend;
    }

    public Elf32Address offset() { return offset; }
    public int info() { return info; }

    public int symbol() { return (info >>> 8) & 0xff; }
    public int type() { return (info & 0xff); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32RelocationAddend that = (Elf32RelocationAddend) o;
        return info == that.info && Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, info);
    }
}

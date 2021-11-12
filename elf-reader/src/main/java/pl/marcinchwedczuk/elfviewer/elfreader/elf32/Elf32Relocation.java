package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

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

    public Elf32Address offset() { return offset; }
    public int info() { return info; }

    public int symbol() { return (info >>> 8) & 0xff; }
    public int type() { return (info & 0xff); }

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

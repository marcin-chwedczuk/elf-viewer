package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Off")
public class Elf32Offset {
    private final long offset;

    public Elf32Offset(long offset) {
        this.offset = offset;
    }

    public long longValue() {
        return offset;
    }

    public Elf32Offset plus(int nbytes) {
        return new Elf32Offset(offset + nbytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32Offset that = (Elf32Offset) o;
        return offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset);
    }

    @Override
    public String toString() {
        return String.format("0x%08x", offset);
    }

    public boolean isBefore(Elf32Offset other) {
        return this.offset < other.offset;
    }

    public boolean isAfterOrAt(Elf32Offset other) {
        return this.offset >= other.offset;
    }
}

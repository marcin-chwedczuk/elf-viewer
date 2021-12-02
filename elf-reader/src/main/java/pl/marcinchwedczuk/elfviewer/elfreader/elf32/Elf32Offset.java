package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Off")
public class Elf32Offset extends ElfOffset<Elf32Offset> {
    public static final Elf32Offset ZERO = new Elf32Offset(0);

    private final int offset;

    public Elf32Offset(int offset) {
        this.offset = offset;
    }

    public int intValue() {
        return offset;
    }

    @Override
    public boolean isAfter(Elf32Offset other) {
        return Integer.compareUnsigned(this.offset, other.offset) > 0;
    }

    @Override
    public boolean isBefore(Elf32Offset other) {
        return Integer.compareUnsigned(this.offset, other.offset) < 0;
    }

    public Elf32Offset plus(long nbytes) {
        // TODO: Overflow
        return new Elf32Offset(offset + Math.toIntExact(nbytes));
    }

    public long minus(Elf32Offset fileOffset) {
        // Normal subtraction works for unsigned int's
        return this.offset - fileOffset.offset;
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
}

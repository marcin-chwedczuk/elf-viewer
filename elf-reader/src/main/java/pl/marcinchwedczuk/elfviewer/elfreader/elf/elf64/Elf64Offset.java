package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;

import java.util.Objects;

public class Elf64Offset extends ElfOffset<Elf64Offset> {
    public static final Elf64Offset ZERO = new Elf64Offset(0);

    private final long offset;

    public Elf64Offset(long offset) {
        this.offset = offset;
    }

    public long longValue() {
        return offset;
    }

    @Override
    public boolean isAfter(Elf64Offset other) {
        return Long.compareUnsigned(this.offset, other.offset) > 0;
    }

    @Override
    public boolean isBefore(Elf64Offset other) {
        return Long.compareUnsigned(this.offset, other.offset) < 0;
    }

    public Elf64Offset plus(long nbytes) {
        // TODO: Overflow
        return new Elf64Offset(offset + nbytes);
    }

    public long minus(Elf64Offset fileOffset) {
        // Normal subtraction works for unsigned int's
        return this.offset - fileOffset.offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf64Offset that = (Elf64Offset) o;
        return offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset);
    }

    @Override
    public String toString() {
        return String.format("0x%016x", offset);
    }
}

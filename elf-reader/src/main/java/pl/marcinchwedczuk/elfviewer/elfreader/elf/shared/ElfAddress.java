package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ElfAddress<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final NATIVE_WORD address;

    public ElfAddress(NATIVE_WORD address) {
        this.address = requireNonNull(address);
    }

    public NATIVE_WORD value() { return address; }

    protected abstract ElfAddress<NATIVE_WORD> mkAddress(long value);

    public boolean isNull() {
        return (address.longValue() == 0);
    }

    public boolean isAfter(ElfAddress<NATIVE_WORD> address) {
        return Long.compareUnsigned(
                this.address.longValue(),
                address.address.longValue()) > 0;
    }
    public boolean isAfterOrAt(ElfAddress<NATIVE_WORD> address) {
        return this.isAfter(address) || this.equals(address);
    }

    public boolean isBefore(ElfAddress<NATIVE_WORD> address) {
        return Long.compareUnsigned(
                this.address.longValue(),
                address.address.longValue()) < 0;
    }
    public boolean isBeforeOrAt(ElfAddress<NATIVE_WORD> address) {
        return this.isBefore(address) || this.equals(address);
    }

    public ElfAddress<NATIVE_WORD> plus(long bytesCount) {
        // TODO: Check overflow
        return mkAddress(address.longValue() + bytesCount);
    }

    public long minus(ElfAddress<NATIVE_WORD> address) {
        // Normal subtract works for unsigned int as well
        return this.address.longValue() - address.address.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElfAddress<?> that = (ElfAddress<?>) o;
        // TODO: Check returns false addr<int> and addr<long>
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public abstract String toString();
}
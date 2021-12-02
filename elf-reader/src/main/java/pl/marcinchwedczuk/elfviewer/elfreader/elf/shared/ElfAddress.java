package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;

public abstract class ElfAddress<T extends ElfAddress<T>> {
    public abstract boolean isNull();

    public abstract boolean isAfter(T address);
    public boolean isAfterOrAt(T address) {
        return this.isAfter(address) || this.equals(address);
    }

    public abstract boolean isBefore(T address);
    public boolean isBeforeOrAt(T address) {
        return this.isBefore(address) || this.equals(address);
    }

    public abstract T plus(long bytesCount);
    public abstract long minus(T address);

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
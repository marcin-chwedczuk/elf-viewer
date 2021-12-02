package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

public abstract class ElfOffset<T extends ElfOffset<T>> {
    public abstract boolean isAfter(T fileOffset);
    public boolean isAfterOrAt(T fileOffset) {
        return this.isAfter(fileOffset) || this.equals(fileOffset);
    }

    public abstract boolean isBefore(T fileOffset);
    public boolean isBeforeOrAt(T fileOffset) {
        return this.isBefore(fileOffset) || this.equals(fileOffset);
    }

    public abstract T plus(long bytesCount);
    public abstract long minus(T fileOffset);

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ElfOffset<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final NATIVE_WORD fileOffset;

    public ElfOffset(NATIVE_WORD fileOffset) {
        this.fileOffset = requireNonNull(fileOffset);
    }

    public NATIVE_WORD value() { return fileOffset; }

    protected abstract ElfOffset<NATIVE_WORD> mkFileOffset(long value);

    public boolean isNull() {
        return (fileOffset.longValue() == 0);
    }

    public boolean isAfter(ElfOffset<NATIVE_WORD> fileOffset) {
        return Long.compareUnsigned(
                this.fileOffset.longValue(),
                fileOffset.fileOffset.longValue()) > 0;
    }
    public boolean isAfterOrAt(ElfOffset<NATIVE_WORD> fileOffset) {
        return this.isAfter(fileOffset) || this.equals(fileOffset);
    }

    public boolean isBefore(ElfOffset<NATIVE_WORD> fileOffset) {
        return Long.compareUnsigned(
                this.fileOffset.longValue(),
                fileOffset.fileOffset.longValue()) < 0;
    }
    public boolean isBeforeOrAt(ElfOffset<NATIVE_WORD> fileOffset) {
        return this.isBefore(fileOffset) || this.equals(fileOffset);
    }

    public ElfOffset<NATIVE_WORD> plus(long bytesCount) {
        // TODO: Check overflow
        return mkFileOffset(fileOffset.longValue() + bytesCount);
    }

    public long minus(ElfOffset<NATIVE_WORD> fileOffset) {
        // Normal subtract works for unsigned int as well
        return this.fileOffset.longValue() - fileOffset.fileOffset.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElfOffset<?> that = (ElfOffset<?>) o;
        // TODO: Check returns false addr<int> and addr<long>
        return Objects.equals(fileOffset, that.fileOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileOffset);
    }

    @Override
    public abstract String toString();
}

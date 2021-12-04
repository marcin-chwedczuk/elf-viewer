package pl.marcinchwedczuk.elfviewer.elfreader.utils;

public final class Mask<Owner extends BitFlags<Owner>> {
    private final long value;

    public Mask(long value) {
        if (value == 0)
            throw new IllegalArgumentException("Mask has no bits set.");
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("0x%08x", value);
    }
}

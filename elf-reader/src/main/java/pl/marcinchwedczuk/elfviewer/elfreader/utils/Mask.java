package pl.marcinchwedczuk.elfviewer.elfreader.utils;

public final class Mask<Owner extends IntFlags<Owner>> {
    private final int value;

    public Mask(int value) {
        if (value == 0)
            throw new IllegalArgumentException("Mask has no bits set.");
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("0x%08x", value);
    }
}

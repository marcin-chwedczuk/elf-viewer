package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import static java.util.Objects.requireNonNull;

public final class Flag<Owner extends IntFlags<Owner>> {
    private final String name;
    private final int value;

    public Flag(String name, int value) {
        checkSingleBitSet(value);

        this.name = requireNonNull(name);
        this.value = value;
    }

    public String name() {
        return name;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    private static void checkSingleBitSet(int v) {
        if (v != 0 && (v & (v - 1)) == 0) {
            return;
        }

        throw new IllegalArgumentException(
                "Invalid flag value, flag should have only one bit set.");
    }
}

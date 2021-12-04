package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import static java.util.Objects.requireNonNull;

public final class Flag<Owner extends BitFlags<Owner>> {
    private final String name;
    private final long value;

    public Flag(String name, long value) {
        checkSingleBitSet(value);

        this.name = requireNonNull(name);
        this.value = value;
    }

    public String name() {
        return name;
    }

    public long longValue() {
        return value;
    }

    public int intValue() {
        return Math.toIntExact(value);
    }

    @Override
    public String toString() {
        return name;
    }

    private static void checkSingleBitSet(long v) {
        if (v != 0 && (v & (v - 1)) == 0) {
            return;
        }

        throw new IllegalArgumentException(
                "Invalid flag value, flag should have only one bit set.");
    }
}

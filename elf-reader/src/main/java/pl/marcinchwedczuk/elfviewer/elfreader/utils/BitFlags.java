package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.Collection;
import java.util.Objects;

public abstract class BitFlags<T extends BitFlags<T>> {
    private final long raw;

    protected BitFlags(long init) {
        this.raw = init;
    }

    @SafeVarargs
    protected BitFlags(Flag<T>... flags) {
        this(combine(flags));
    }

    public final boolean hasFlag(Flag<T> flag) {
        return (raw & flag.longValue()) == flag.longValue();
    }

    public final T setFlag(Flag<T> flag) {
        long newRaw = raw | flag.longValue();
        return mkCopy(newRaw);
    }

    public final T clearFlag(Flag<T> flag) {
        long newRaw = raw & ~flag.longValue();
        return mkCopy(newRaw);
    }

    protected abstract T mkCopy(long newRaw);
    public abstract Collection<Flag<T>> flags();

    public int intValue() {
        // TODO: Overflow
        return (int)raw;
    }

    public long longValue() {
        return raw;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitFlags<?> bitFlags = (BitFlags<?>) o;
        return raw == bitFlags.raw;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(raw);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        long curr = raw;
        for (Flag<T> flag : flags()) {
            if ((curr & flag.longValue()) == flag.longValue()) {
                curr &= ~flag.longValue();
                sb.append(flag.name()).append("|");
            }
        }

        if (curr != 0) {
            sb.append(String.format("0x%08x", curr));
        }

        // Remove trailing '|'
        int lastChar = sb.length()-1;
        if (sb.length() > 0 && sb.charAt(lastChar) == '|') {
            sb.deleteCharAt(lastChar);
        }

        return sb.toString();
    }

    private static int combine(Flag<?>[] flags) {
        int v = 0;
        for (Flag<?> f : flags) {
            v |= f.longValue();
        }
        return v;
    }

    public static <T extends BitFlags<T>>
    Flag<T> flag(String name, long value) {
        return new Flag<>(name, value);
    }

    public static <T extends BitFlags<T>>
    Mask<T> mask(long value) {
        return new Mask<>(value);
    }
}
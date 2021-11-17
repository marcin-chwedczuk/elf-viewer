package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.Collection;
import java.util.Objects;

public abstract class IntFlags<T extends IntFlags<T>> {
    private final int raw;

    protected IntFlags(int init) {
        this.raw = init;
    }

    @SafeVarargs
    protected IntFlags(Flag<T>... flags) {
        this(combine(flags));
    }

    public final boolean hasFlag(Flag<T> flag) {
        return (raw & flag.value()) == flag.value();
    }

    public final T setFlag(Flag<T> flag) {
        int newRaw = raw | flag.value();
        return mkCopy(newRaw);
    }

    public final T clearFlag(Flag<T> flag) {
        int newRaw = raw & ~flag.value();
        return mkCopy(newRaw);
    }

    protected abstract T mkCopy(int newRaw);
    public abstract Collection<Flag<T>> flags();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntFlags<?> intFlags = (IntFlags<?>) o;
        return raw == intFlags.raw;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(raw);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int curr = raw;
        for (Flag<T> flag : flags()) {
            if ((curr & flag.value()) == flag.value()) {
                curr &= ~flag.value();
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
            v |= f.value();
        }
        return v;
    }

    public static <T extends IntFlags<T>>
    Flag<T> flag(String name, int value) {
        return new Flag<>(name, value);
    }

    public static <T extends IntFlags<T>>
    Mask<T> mask(int value) {
        return new Mask<>(value);
    }
}
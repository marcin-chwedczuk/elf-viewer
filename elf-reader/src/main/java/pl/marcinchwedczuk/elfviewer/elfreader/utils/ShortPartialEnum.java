package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class ShortPartialEnum<T extends ShortPartialEnum<T>> extends BasePartialEnum {
    private final short value;

    protected ShortPartialEnum(short value) {
        // For unnamed constants
        super();
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    protected ShortPartialEnum(short value,
                               String name,
                               Map<Short, T> byValueMap,
                               Map<String, T> byNameMap) {
        super(name);
        this.value = value;

        if (byValueMap.containsKey(value))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum value: 0x" + Integer.toHexString(value) + ".");

        if (byNameMap.containsKey(name))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum name: '" + name + "'.");

        byValueMap.put(value, (T)this);
        byNameMap.put(name, (T)this);
    }

    public final short value() { return value; }

    @Override
    public final String hexString() {
        return String.format("0x%04x", value & 0xffff);
    }

    public final boolean is(T value) {
        return this.equals(value);
    }
    public final boolean isOneOf(T... values) {
        for (T value : values) {
            if (this.is(value))
                return true;
        }
        return false;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortPartialEnum<?> that = (ShortPartialEnum<?>) o;
        return value == that.value && Objects.equals(name(), that.name());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value, name());
    }

    @Override
    public final String toString() {
        if (isKnownValue()) {
            return name();
        } else {
            return hexString();
        }
    }

    protected static <T extends ShortPartialEnum<T>>
    Map<Short, T> mkByValueMap() {
        return new HashMap<>();
    }

    protected static <T extends ShortPartialEnum<T>>
    Map<String, T> mkByNameMap() {
        return new HashMap<>();
    }

    protected static <T extends ShortPartialEnum<T>>
    T fromValueOrCreate(short value, Map<Short, T> cache, Function<Short, T> factory) {
        if (cache.containsKey(value)) {
            return cache.get(value);
        } else {
            return factory.apply(value);
        }
    }

    protected static <T extends ShortPartialEnum<T>>
    T fromName(String name, Map<String, T> cache) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        } else {
            throw new IllegalArgumentException("No constant with name '" + name + "' is registered in this enum.");
        }
    }

    protected static <T extends ShortPartialEnum<T>>
    Collection<T> knownValues(Map<?, T> cache) {
        return Collections.unmodifiableCollection(cache.values());
    }

    protected static byte s(int value) {
        if (value < 0 || value > 0xffff)
            throw new IllegalArgumentException(
                    "Value " + value + " is outside Byte's range.");

        return (byte)(value & 0xffff);
    }
}


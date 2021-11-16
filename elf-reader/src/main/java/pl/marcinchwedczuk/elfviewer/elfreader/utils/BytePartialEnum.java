package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class BytePartialEnum<T extends BytePartialEnum<T>> {
    private final byte value;
    private final String name;

    protected BytePartialEnum(byte value) {
        // For unnamed constants
        this.value = value;
        this.name = null;
    }

    @SuppressWarnings("unchecked")
    protected BytePartialEnum(byte value,
                              String name,
                              Map<Byte, T> byValueMap,
                              Map<String, T> byNameMap) {
        this.value = value;
        this.name = requireNonNull(name);

        if (byValueMap.containsKey(value))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum value: 0x" + Integer.toHexString(value) + ".");

        if (byNameMap.containsKey(name))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum name: '" + name + "'.");

        byValueMap.put(value, (T)this);
        byNameMap.put(name, (T)this);
    }

    public final byte value() { return value; }
    public final String name() { return name; }

    public final boolean isKnownValue() {
        return (this.name != null);
    }
    public final boolean isUnknownValue() {
        return !isKnownValue();
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
        BytePartialEnum<?> that = (BytePartialEnum<?>) o;
        return value == that.value && Objects.equals(name, that.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value, name);
    }

    @Override
    public final String toString() {
        if (isKnownValue()) {
            return name;
        } else {
            return String.format("0x%02x", value);
        }
    }

    protected static <T extends BytePartialEnum<T>>
    Map<Byte, T> mkByValueMap() {
        return new HashMap<>();
    }

    protected static <T extends BytePartialEnum<T>>
    Map<String, T> mkByNameMap() {
        return new HashMap<>();
    }

    protected static <T extends BytePartialEnum<T>>
    T fromValueOrCreate(byte value, Map<Byte, T> cache, Function<Byte, T> factory) {
        if (cache.containsKey(value)) {
            return cache.get(value);
        } else {
            return factory.apply(value);
        }
    }

    protected static <T extends BytePartialEnum<T>>
    T fromName(String name, Map<String, T> cache) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        } else {
            throw new IllegalArgumentException("No constant with name '" + name + "' is registered in this enum.");
        }
    }

    protected static <T extends BytePartialEnum<T>>
    Collection<T> knownValues(Map<?, T> cache) {
        return Collections.unmodifiableCollection(cache.values());
    }

    protected static byte b(int value) {
        if (value < 0 || value > 255)
            throw new IllegalArgumentException(
                    "Value " + value + " is outside Byte's range.");

        return (byte)(value & 0xff);
    }
}


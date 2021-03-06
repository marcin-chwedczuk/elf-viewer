package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.*;
import java.util.function.Function;

public abstract class IntPartialEnum<T extends IntPartialEnum<T>> extends BasePartialEnum {
    private final int value;

    protected IntPartialEnum(int value) {
        // For unnamed constants
        super();
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    protected IntPartialEnum(int value,
                             String name,
                             Map<Integer, T> byValueMap,
                             Map<String, T> byNameMap) {
        super(name);
        this.value = value;

        if (byValueMap.containsKey(value))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum value: 0x" + Integer.toHexString(value) + "."
                    + " Class name: " + this.getClass().getSimpleName() + ".");

        if (byNameMap.containsKey(name))
            throw new IllegalArgumentException(
                    "Attempt to redefine enum name: '" + name + "'.");

        byValueMap.put(value, (T)this);
        byNameMap.put(name, (T)this);
    }

    public final int value() { return value; }

    @Override
    public final String hexString() {
        return String.format("0x%08x", value);
    }

    public final boolean is(T value) {
        return this.equals(value);
    }
    public final boolean isNot(T value) {
        return !this.equals(value);
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
        IntPartialEnum<?> that = (IntPartialEnum<?>) o;
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

    protected static <T extends IntPartialEnum<T>>
    Map<Integer, T> mkByValueMap() {
        return new HashMap<>();
    }

    protected static <T extends IntPartialEnum<T>>
    Map<String, T> mkByNameMap() {
        return new HashMap<>();
    }

    protected static <T extends IntPartialEnum<T>>
    T fromValueOrCreate(int value, Map<Integer, T> cache, Function<Integer, T> factory) {
        if (cache.containsKey(value)) {
            return cache.get(value);
        } else {
            return factory.apply(value);
        }
    }

    protected static <T extends IntPartialEnum<T>>
    T fromName(String name, Map<String, T> cache) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        } else {
            throw new IllegalArgumentException("No constant with name '" + name + "' is registered in this enum.");
        }
    }

    protected static <T extends IntPartialEnum<T>>
    Collection<T> knownValues(Map<?, T> cache) {
        return Collections.unmodifiableCollection(cache.values());
    }
}


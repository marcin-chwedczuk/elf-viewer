package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Memoized<V> {
    private final Supplier<V> factory;
    private final AtomicReference<V> value;

    public Memoized(Supplier<V> factory) {
        this.factory = requireNonNull(factory);
        this.value = new AtomicReference<>(null);
    }

    public V get() {
        V v = value.get();

        if (v == null) {
            v = factory.get();
            value.set(v);
        }

        return v;
    }
}

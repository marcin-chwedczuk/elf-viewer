package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.utils;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {

    public static <T>
    Stream<ValueWithIndex<T>> zipWithIndex(List<T> list) {
        return IntStream.range(0, list.size())
                .mapToObj(index -> new ValueWithIndex<T>(index, list.get(index)));
    }

    public static class ValueWithIndex<T> {
        public final int index;
        public final T value;

        public ValueWithIndex(int index, T value) {
            this.index = index;
            this.value = value;
        }
    }
}

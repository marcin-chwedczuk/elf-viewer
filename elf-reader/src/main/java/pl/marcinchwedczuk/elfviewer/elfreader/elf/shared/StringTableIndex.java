package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import java.util.Objects;

public class StringTableIndex {
    private final int index;

    public StringTableIndex(int index) {
        this.index = index;
    }

    public int intValue() { return index; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTableIndex that = (StringTableIndex) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}

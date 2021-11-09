package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import java.util.Objects;

/**
 * Represents an index into Section Header Table.
 */
public class SHTIndex {
    private final int index;

    public SHTIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SHTIndex that = (SHTIndex) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString(index);
    }
}

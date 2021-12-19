package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

public class SymbolTableIndex {
    @ElfApi("STN_UNDEF")
    public static final SymbolTableIndex Undefined = new SymbolTableIndex(0);

    private final int index;

    public SymbolTableIndex(int index) {
        this.index = index;
    }

    public boolean isUndefined() {
        return index == 0;
    }

    public int intValue() { return index; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolTableIndex that = (SymbolTableIndex) o;
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

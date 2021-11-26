package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

public class StringTableEntry {
    public final StringTableIndex index;
    public final String value;

    public StringTableEntry(StringTableIndex index, String value) {
        this.index = index;
        this.value = value;
    }

    public StringTableIndex getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    // TODO: HashCode Equals etc.
}

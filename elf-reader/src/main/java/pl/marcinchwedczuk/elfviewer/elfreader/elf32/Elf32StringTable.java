package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfStringTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * String table sections hold null-terminated character sequences,
 * commonly called strings.  The object file uses these strings to
 * represent symbol and section names.  One references a string as
 * an index into the string table section.  The first byte, which is
 * index zero, is defined to hold a null byte ('\0').  Similarly, a
 * string table's last byte is defined to hold a null byte, ensuring
 * null termination for all strings.
 */
public class Elf32StringTable {
    private final ElfStringTable<Integer> stringTable;

    public Elf32StringTable(ElfStringTable<Integer> stringTable) {
        this.stringTable = requireNonNull(stringTable);
    }

    public boolean isValidIndex(StringTableIndex index) { return stringTable.isValidIndex(index); }
    public Collection<StringTableEntry> getContents() { return stringTable.getContents(); }
    public String getStringAtIndex(StringTableIndex index) { return stringTable.getStringAtIndex(index); }
}

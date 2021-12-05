package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfStringTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableEntry;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

public class Elf64StringTable {
    private final ElfStringTable<Long> stringTable;

    public Elf64StringTable(ElfStringTable<Long> stringTable) {
        this.stringTable = requireNonNull(stringTable);
    }

    public boolean isValidIndex(StringTableIndex index) { return stringTable.isValidIndex(index); }
    public Collection<StringTableEntry> getContents() { return stringTable.getContents(); }
    public String getStringAtIndex(StringTableIndex index) { return stringTable.getStringAtIndex(index); }
}

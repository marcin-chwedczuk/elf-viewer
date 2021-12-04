package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationsTable;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class RelocationsTable {
    private final ElfRelocationsTable<Integer> relocationsTable;

    public RelocationsTable(ElfRelocationsTable<Integer> relocationsTable) {
        this.relocationsTable = relocationsTable;
    }


    public int size() {
        return relocationsTable.size();
    }

    public Elf32Relocation get(int index) {
        // TODO: Handle null
        return new Elf32Relocation(relocationsTable.get(index));
    }

    public Collection<Elf32Relocation> relocations() {
        return relocationsTable.relocations().stream()
                .map(Elf32Relocation::new)
                .collect(Collectors.toList());
    }
}

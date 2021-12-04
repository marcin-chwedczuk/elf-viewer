package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolType.SECTION;

public class SymbolTable {
    private final ElfSymbolTable<Integer> symbolTable;

    public SymbolTable(ElfSymbolTable<Integer> symbolTable)
    {

        this.symbolTable = symbolTable;
    }

    public int size() {
        return symbolTable.size();
    }

    public Elf32Symbol get(SymbolTableIndex index) {
        ElfSymbol<Integer> symbol = symbolTable.get(index);
        return new Elf32Symbol(symbol);
   }

    public Optional<Elf32Symbol> slowlyFindSymbolByName(String name) {
        return symbolTable.slowlyFindSymbolByName(name)
                .map(Elf32Symbol::new);
    }

    public Collection<SymbolTableEntry> symbols() {
        return symbolTable.symbols().stream()
                .map(SymbolTableEntry::new)
                .collect(Collectors.toList());
   }
}

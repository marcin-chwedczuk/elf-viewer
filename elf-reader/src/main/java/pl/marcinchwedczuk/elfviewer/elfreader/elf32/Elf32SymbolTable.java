package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Elf32SymbolTable {
    private final ElfSymbolTable<Integer> symbolTable;

    public Elf32SymbolTable(ElfSymbolTable<Integer> symbolTable)
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

    public Collection<Elf32SymbolTableEntry> symbols() {
        return symbolTable.symbols().stream()
                .map(Elf32SymbolTableEntry::new)
                .collect(Collectors.toList());
   }
}

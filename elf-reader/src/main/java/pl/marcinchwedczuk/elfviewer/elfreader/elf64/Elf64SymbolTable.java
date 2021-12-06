package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Elf64SymbolTable {
    private final ElfSymbolTable<Long> symbolTable;

    public Elf64SymbolTable(ElfSymbolTable<Long> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public int size() {
        return symbolTable.size();
    }

    public Elf64Symbol get(SymbolTableIndex index) {
        ElfSymbol<Long> symbol = symbolTable.get(index);
        return new Elf64Symbol(symbol);
    }

    public Optional<Elf64Symbol> slowlyFindSymbolByName(String name) {
        return symbolTable.slowlyFindSymbolByName(name)
                .map(Elf64Symbol::new);
    }

    public Collection<Elf64SymbolTableEntry> symbols() {
        return symbolTable.symbols().stream()
                .map(Elf64SymbolTableEntry::new)
                .collect(toList());
    }
}

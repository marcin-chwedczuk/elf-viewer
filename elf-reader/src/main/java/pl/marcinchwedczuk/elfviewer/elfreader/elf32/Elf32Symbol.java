package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;

public class Elf32Symbol {
    private final ElfSymbol<Integer> symbol;

    public Elf32Symbol(ElfSymbol<Integer> symbol) {
        this.symbol = symbol;
    }


    public StringTableIndex nameIndex() {
        return symbol.nameIndex();
    }


    public String name() {
        return symbol.name();
    }

    public Elf32Address value() {
        return new Elf32Address(symbol.value().value());
    }


    public int size() {
        return symbol.size();
    }


    public byte info() {
        return symbol.info();
    }

    public Elf32SymbolBinding binding() {
        return symbol.binding();
    }

    public Elf32SymbolType symbolType() {
        return symbol.symbolType();
    }

    public byte other() {
        return symbol.other();
    }

    public Elf32SymbolVisibility visibility() {
        return symbol.visibility();
    }

    public SectionHeaderIndex index() {
        return symbol.index();
    }

    @Override
    public String toString() {
        return symbol.toString();
    }
}

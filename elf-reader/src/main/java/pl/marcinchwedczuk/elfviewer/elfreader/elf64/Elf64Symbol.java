package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolBinding;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolVisibility;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;

public class Elf64Symbol {
    private final ElfSymbol<Long> symbol;

    public Elf64Symbol(ElfSymbol<Long> symbol) {
        this.symbol = symbol;
    }

    public StringTableIndex nameIndex() {
        return symbol.nameIndex();
    }

    public String name() {
        return symbol.name();
    }

    public Elf64Address value() {
        return new Elf64Address(symbol.value());
    }

    public long size() {
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

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfGnuHashTable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Elf32GnuHashTable {
    private final ElfGnuHashTable<Integer> table;

    public Elf32GnuHashTable(ElfGnuHashTable<Integer> table) {
        this.table = table;
    }

    public static int gnuHash(String s) {
        return ElfGnuHashTable.gnuHash(s);
    }

    public Optional<Elf32Symbol> findSymbol(String symbolName) {
        return table.findSymbol(symbolName).map(Elf32Symbol::new);
    }

    @Override
    public String toString() {
        return table.toString();
    }

    public SymbolTable symbolTable() {
        return new SymbolTable(table.symbolTable());
    }

    public int nBuckets() {
        return table.nBuckets();
    }

    public int startSymbolIndex() {
        return table.startSymbolIndex();
    }

    public int maskWords() {
        return table.maskWords();
    }

    public int shift2() {
        return table.shift2();
    }

    public int[] bloomFilter() {
        return table.bloomFilter();
    }

    public int[] buckets() {
        return table.buckets();
    }

    public int[] hashValues() {
        return table.hashValues();
    }
}

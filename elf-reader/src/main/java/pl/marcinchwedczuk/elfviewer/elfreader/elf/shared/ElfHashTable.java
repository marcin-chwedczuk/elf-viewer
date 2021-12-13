package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;

import java.util.Optional;

public class ElfHashTable<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    private final int nbucket;
    private final int nchain;
    private final SymbolTableIndex bucket[];
    private final SymbolTableIndex chain[];

    private final ElfSymbolTable<NATIVE_WORD> associatedSymbolTable;

    public ElfHashTable(int nbucket,
                        int nchain,
                        SymbolTableIndex[] bucket,
                        SymbolTableIndex[] chain,
                        ElfSymbolTable<NATIVE_WORD> associatedSymbolTable) {
        // TODO: Add arg checks
        this.nbucket = nbucket;
        this.nchain = nchain;
        this.bucket = bucket;
        this.chain = chain;
        this.associatedSymbolTable = associatedSymbolTable;
    }

    public int nbucket() {
        return nbucket;
    }

    public int nchain() {
        return nchain;
    }

    public SymbolTableIndex[] bucket() {
        return bucket;
    }

    public SymbolTableIndex[] chain() {
        return chain;
    }

    public ElfSymbolTable<NATIVE_WORD> associatedSymbolTable() {
        return associatedSymbolTable;
    }

    public Optional<ElfSymbol<NATIVE_WORD>> findSymbol(String symbolName) {
        int hash = elfHash(symbolName);
        int bucketNo = Math.toIntExact(Long.remainderUnsigned(hash, nbucket));

        SymbolTableIndex curr = bucket[bucketNo];
        while (!curr.isUndefined()) {
            ElfSymbol<NATIVE_WORD> symbol = associatedSymbolTable.get(curr);
            if (symbolName.equals(symbol.name()))
                return Optional.of(symbol);

            // Chain entries are in sync with symbol table entries
            curr = chain[curr.intValue()];
        }

        return Optional.empty();
    }

    public static int elfHash(String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + (0xff & str.charAt(i));

            int x = hash & 0xf0000000;
            if (x != 0) {
                hash ^= (x >>> 24);
            }

            hash &= ~x;
        }

        return hash;
    }
}

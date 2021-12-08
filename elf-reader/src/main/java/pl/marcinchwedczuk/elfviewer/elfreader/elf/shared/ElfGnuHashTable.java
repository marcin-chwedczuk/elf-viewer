package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

// See: https://www.gabriel.urdhr.fr/2015/09/28/elf-file-format/
// See: https://blogs.oracle.com/solaris/post/gnu-hash-elf-sections
public class ElfGnuHashTable<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final NativeWord<NATIVE_WORD> nativeWord;
    private final ElfSymbolTable<NATIVE_WORD> dynsym;

    private final int nbuckets;

    /**
     * Index of the first accessible symbol in .dynsym.
     */
    private final int startSymbolIndex;

    /**
     * Number of elements in the Bloom Filter.
     */
    private final int maskWords;

    /**
     * Shift count for the Bloom Filter.
     */
    private final int shift2;

    // TODO: Add ElfApi annotations
    // Has size maskWords
    private final NATIVE_WORD[] bloomFilter;

    private final int[] buckets;

    // Has size dynsym.size() - symbolIndex
    private final int[] hashValues;

    public ElfGnuHashTable(NativeWord<NATIVE_WORD> nativeWord, ElfSymbolTable<NATIVE_WORD> dynsym,
                           int nbuckets,
                           int startSymbolIndex,
                           int maskWords,
                           int shift2,
                           NATIVE_WORD[] bloomFilter,
                           int[] buckets,
                           int[] hashValues) {
        this.nativeWord = requireNonNull(nativeWord);
        this.dynsym = requireNonNull(dynsym);
        this.nbuckets = nbuckets;
        this.startSymbolIndex = startSymbolIndex;
        this.maskWords = maskWords;
        this.shift2 = shift2;
        this.bloomFilter = bloomFilter;
        this.buckets = buckets;
        this.hashValues = hashValues;
    }

    @ElfApi("dl_new_hash")
    public static int gnuHash(String s)
    {
        int h = 5381;

        // Not 100% accurate as we use UTF-16 characters
        for (char c : s.toCharArray()) {
            h = h*33 + c;
        }

        return h;
    }

    public static boolean isHashChainEnd(int hash) {
        // Last bit of hash is used as marker
        return (hash & 1) != 0;
    }

    public Optional<ElfSymbol<NATIVE_WORD>> findSymbol(String symbolName) {
        int h1 = gnuHash(symbolName);
        int h2 = h1 >> shift2;

        int c = nativeWord.size() * 8; // sizeof(int) * BITS_PER_BYTE
        int n = umod((h1 / c), maskWords); // bloom filter word
        long bitmask = (1L << umod(h1, c))  // bits within bloom filter word
                | (1L << umod(h2, c));

        if (!nativeWord.hasBitsSet(bloomFilter[n], bitmask)) {
            // Symbol not in hashtable
            return Optional.empty();
        }

        n = buckets[umod(h1, nbuckets)];
        if (n == 0) {
            // 0 means undefined symbol is referenced,
            // undefined symbol represents NULL
            return Optional.empty();
        }

        // Walk the hash table bucket's chain
        int symIndex = n;

        // Remove last bit, in hashValues last bit is used as stop bit
        h1 &= ~1;
        while (true) {
            ElfSymbol<NATIVE_WORD> symbol = dynsym.get(new SymbolTableIndex(symIndex));
            int symHash = hashValues[symIndex - startSymbolIndex];

            // First check hash, left this to be more faithful to
            // the original algorithm
            if (h1 == (symHash & ~1) &&
                    symbol.name().equals(symbolName))
                return Optional.of(symbol);

            if ((symHash & 1) != 0) {
                // Stop bit -> this is the last entry in the chain
                return Optional.empty();
            }

            symIndex++;
        }
    }

    /**
     * Java operates on signed numbers.
     * Thus for hash functions % operator can
     * return negative values.
     * This mod function treads arguments as unsigned.
     */
    private static int umod(int a, int b) {
        return Integer.remainderUnsigned(a, b);
    }

    @Override
    public String toString() {
        return String.format("nbuckets=%d index=%d maskWords=%d shift2=%d",
                nbuckets, startSymbolIndex, maskWords, shift2);
    }

    public ElfSymbolTable<NATIVE_WORD> symbolTable() {
        return dynsym;
    }

    public int nBuckets() {
        return nbuckets;
    }

    public int startSymbolIndex() {
        return startSymbolIndex;
    }

    public int maskWords() {
        return maskWords;
    }

    public int shift2() {
        return shift2;
    }

    public NATIVE_WORD[] bloomFilter() {
        return bloomFilter;
    }

    public int[] buckets() {
        return buckets;
    }

    public int[] hashValues() {
        return hashValues;
    }
}

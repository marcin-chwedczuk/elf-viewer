package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

// See: https://www.gabriel.urdhr.fr/2015/09/28/elf-file-format/
// See: https://blogs.oracle.com/solaris/post/gnu-hash-elf-sections
public class Elf32GnuHash {
    private final SymbolTable dynsym;

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

    // Has size maskWords
    private final int[] bloomFilter;

    private final int[] buckets;

    // Has size dynsym.size() - symbolIndex
    private final int[] hashValues;

    public Elf32GnuHash(SymbolTable dynsym,
                        int nbuckets,
                        int startSymbolIndex,
                        int maskWords,
                        int shift2,
                        int[] bloomFilter,
                        int[] buckets,
                        int[] hashValues) {
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
    private static int gnuHash(String s)
    {
        int h = 5381;

        // Not 100% accurate as we use UTF-16 characters
        for (char c : s.toCharArray()) {
            h = h*33 + c;
        }

        return h;
    }

    public Optional<Elf32Symbol> findSymbol(String symbolName) {
        int h1 = gnuHash(symbolName);
        int h2 = h1 >> shift2;

        int c = 4 * 8; // sizeof(int) * BITS_PER_BYTE
        int n = umod((h1 / c), maskWords); // bloom filter world
        int bitmask = (1 << umod(h1, c))  // bits within bloom filter world
                | (1 << umod(h2, c));

        if ((bloomFilter[n] & bitmask) != bitmask) {
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
            Elf32Symbol symbol = dynsym.get(new SymbolTableIndex(symIndex));
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
        // TODO: Optimize
        long ua = Integer.toUnsignedLong(a);
        long ub = Integer.toUnsignedLong(b);
        return (int)(ua % ub);
    }

    @Override
    public String toString() {
        return String.format("nbuckets=%d index=%d maskWords=%d shift2=%d",
                nbuckets, startSymbolIndex, maskWords, shift2);
    }
}

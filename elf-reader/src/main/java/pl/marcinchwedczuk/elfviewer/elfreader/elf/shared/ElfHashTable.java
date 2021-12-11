package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

public class ElfHashTable {
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

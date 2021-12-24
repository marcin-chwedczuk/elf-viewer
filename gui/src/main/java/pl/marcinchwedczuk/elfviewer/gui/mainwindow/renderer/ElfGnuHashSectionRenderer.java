package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfGnuHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuHashSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.GnuHashTableEntryDto;

import java.util.ArrayList;
import java.util.List;

public class ElfGnuHashSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfGnuHashSection<NATIVE_WORD> section;

    public ElfGnuHashSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                     ElfGnuHashSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("nbuckets\n---\n(bloom filter)", indexAccessor(0)),
                mkColumn("symndx\n---\n(buckets)", indexAccessor(1)),
                mkColumn("maskwords_bm\n---\n(hash values)", indexAccessor(2)),
                mkColumn("shift2\n---\n(symbol)", indexAccessor(3))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        ElfGnuHashTable<NATIVE_WORD> hashTable = section.gnuHashTable();

        List<String[]> result = new ArrayList<>();
        result.add(new String[] {
                dec(hashTable.nBuckets()),
                dec(hashTable.startSymbolIndex()),
                dec(hashTable.maskWords()),
                dec(hashTable.shift2())
        });
        result.add(new String[] { "---", "---", "---", "---" });

        int bloomIndex = 0, bucketIndex = 0, hashIndex = 0;
        boolean nextBucket = true;
        while (true) {
            boolean added = false;
            boolean emptyBucket = false;

            String bloomCol = "", bucketCol = "", hashCol = "", symbolCol = "";

            if (bloomIndex < hashTable.bloomFilter().length) {
                added = true;
                bloomCol = hex(hashTable.bloomFilter()[bloomIndex++]);
            }

            if (nextBucket && (bucketIndex < hashTable.buckets().length)) {
                added = true;
                // New hash chain started with this bucket
                int bucketValue = hashTable.buckets()[bucketIndex++];
                bucketCol = hex(bucketValue);
                hashIndex = bucketValue;
                nextBucket = (bucketValue == 0);
            }

            if (hashIndex > 0) { // 0 index means undefined symbol
                added = true;
                int hash = hashTable.hashValues()[hashIndex - hashTable.startSymbolIndex()];
                hashCol = hex(hash);

                // Fill symbol data
                ElfSymbol<NATIVE_WORD> symbol = hashTable.symbolTable().get(
                        new SymbolTableIndex(hashIndex));

                int gnuHash = ElfGnuHashTable.gnuHash(symbol.name());
                symbolCol = String.format("%s (hash: 0x%08x, bucket: %d)",
                        symbol.name(),
                        gnuHash,
                        Integer.toUnsignedLong(gnuHash) % hashTable.nBuckets());

                hashIndex++;

                if (ElfGnuHashTable.isHashChainEnd(hash)) {
                    // Chain ended let's move to the next bucket
                    nextBucket = true;
                    hashIndex = 0;
                }
            }

            if (!added) break;

            result.add(new String[] { bloomCol, bucketCol, hashCol, symbolCol });
        }

        return result;
    }
}

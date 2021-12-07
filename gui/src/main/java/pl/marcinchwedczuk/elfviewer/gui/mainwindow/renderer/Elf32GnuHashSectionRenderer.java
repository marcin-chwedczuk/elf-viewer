package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfGnuHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuHashSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32GnuHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Symbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32GnuHashSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.GnuHashTableEntryDto;

import java.util.ArrayList;
import java.util.List;

public class Elf32GnuHashSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<GnuHashTableEntryDto, NATIVE_WORD>
{
    private final ElfGnuHashSection<NATIVE_WORD> section;

    public Elf32GnuHashSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                       ElfGnuHashSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<GnuHashTableEntryDto, String>> defineColumns() {
        return List.of(
                mkColumn("nbuckets\n---\n(bloom filter)", GnuHashTableEntryDto::getCol1),
                mkColumn("symndx\n---\n(buckets)", GnuHashTableEntryDto::getCol2),
                mkColumn("maskwords_bm\n---\n(hash values)", GnuHashTableEntryDto::getCol3),
                mkColumn("shift2\n---\n(symbol)", GnuHashTableEntryDto::getCol4)
        );
    }

    @Override
    protected List<? extends GnuHashTableEntryDto> defineRows() {
        ElfGnuHashTable<NATIVE_WORD> hashTable = section.gnuHashTable();

        List<GnuHashTableEntryDto> result = new ArrayList<>();
        result.add(new GnuHashTableEntryDto(
                dec(hashTable.nBuckets()),
                dec(hashTable.startSymbolIndex()),
                dec(hashTable.maskWords()),
                dec(hashTable.shift2())));
        result.add(new GnuHashTableEntryDto("---", "---", "---", "---"));

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

                int gnuHash = Elf32GnuHashTable.gnuHash(symbol.name());
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

            result.add(new GnuHashTableEntryDto(bloomCol, bucketCol, hashCol, symbolCol));
            bloomIndex++;
        }

        return result;
    }
}

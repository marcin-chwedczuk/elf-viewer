package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32GnuHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Symbol;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32GnuHashSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.GnuHashTableEntryDto;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32GnuHashSectionRenderer extends BaseRenderer<GnuHashTableEntryDto> {
    private final Elf32GnuHashSection section;

    public Elf32GnuHashSectionRenderer(Elf32GnuHashSection section) {
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
        Elf32GnuHashTable hashTable = section.gnuHashTable();

        List<GnuHashTableEntryDto> result = new ArrayList<>();
        result.add(new GnuHashTableEntryDto(
                dec(hashTable.nBuckets()),
                dec(hashTable.startSymbolIndex()),
                dec(hashTable.maskWords()),
                dec(hashTable.shift2())));
        result.add(new GnuHashTableEntryDto("---", "---", "---", "---"));

        int index = 0;
        boolean nextBucket = true;
        int bucketIndex = 0;
        while (true) {
            boolean added = false;
            String bloomCol = "", bucketCol = "", hashCol = "";

            if (index < hashTable.bloomFilter().length) {
                added = true;
                bloomCol = hex(hashTable.bloomFilter()[index]);
            }

            if (nextBucket && (bucketIndex < hashTable.buckets().length)) {
                added = true;
                // New hash chain started with this bucket
                nextBucket = false;
                bucketCol = hex(hashTable.buckets()[bucketIndex]);
                bucketIndex += 1;
            }

            if (index < hashTable.hashValues().length) {
                added = true;
                int hash = hashTable.hashValues()[index];
                hashCol = hex(hash);

                if (Elf32GnuHashTable.isHashChainEnd(hash)) {
                    // Chain ended let's move to the next bucket
                    nextBucket = true;
                }
            }

            if (!added) break;

            // Symbol table indexes are in sync with hash indexes
            Elf32Symbol symbol = hashTable.symbolTable().get(
                        new SymbolTableIndex(index + hashTable.startSymbolIndex()));

            int gnuHash = Elf32GnuHashTable.gnuHash(symbol.name());
            result.add(new GnuHashTableEntryDto(
                    bloomCol, bucketCol, hashCol,
                    String.format("%s (hash: 0x%08x, bucket: %d)",
                            symbol.name(),
                            gnuHash,
                            Integer.toUnsignedLong(gnuHash) % hashTable.nBuckets())));

            index++;
        }

        return result;
    }
}

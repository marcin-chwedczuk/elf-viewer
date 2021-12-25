package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfHashSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfHashSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfHashSection<NATIVE_WORD> hashSection;

    public ElfHashSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                  StringProperty searchPhase,
                                  ElfHashSection<NATIVE_WORD> hashSection) {
        super(nativeWord, searchPhase);
        this.hashSection = hashSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Field Name", indexAccessor(0)),
                mkColumn("Value", indexAccessor(1), ALIGN_RIGHT)
        );
    }

    @Override
    protected List<String[]> defineRows() {
        ElfHashTable<NATIVE_WORD> hashTable = hashSection.hashTable();

        List<String[]> rows = new ArrayList<>();

        rows.add(new String[] {
                "nbucket", dec(hashTable.nbucket()) });
        rows.add(new String[] {
                "nchain", dec(hashTable.nchain()) });

        for (int i = 0; i < hashTable.bucket().length; i++) {
            rows.add(new String[] {
                    String.format("nbucket[%d]", i),
                    dec(hashTable.bucket()[i].intValue()) });
        }

        for (int i = 0; i < hashTable.chain().length; i++) {
            rows.add(new String[] {
                    String.format("nchain[%d]", i),
                    dec(hashTable.chain()[i].intValue()) });
        }

        return rows;
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

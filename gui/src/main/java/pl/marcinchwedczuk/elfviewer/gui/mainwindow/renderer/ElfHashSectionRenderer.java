package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfHashSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfHashSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<StructureFieldDto, NATIVE_WORD>
{
    private final ElfHashSection<NATIVE_WORD> hashSection;

    public ElfHashSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                  ElfHashSection<NATIVE_WORD> hashSection) {
        super(nativeWord);
        this.hashSection = hashSection;
    }

    @Override
    protected List<TableColumn<StructureFieldDto, String>> defineColumns() {
        return List.of(
                mkColumn("Field Name", StructureFieldDto::getFieldName),
                mkColumn("Value", StructureFieldDto::getRawValue, ALIGN_RIGHT)
        );
    }

    @Override
    protected List<? extends StructureFieldDto> defineRows() {
        ElfHashTable<NATIVE_WORD> hashTable = hashSection.hashTable();

        List<StructureFieldDto> rows = new ArrayList<>();

        rows.add(new StructureFieldDto(
                "nbucket", hashTable.nbucket()));
        rows.add(new StructureFieldDto(
                "nchain", hashTable.nchain()));

        for (int i = 0; i < hashTable.bucket().length; i++) {
            rows.add(new StructureFieldDto(
                    String.format("nbucket[%d]", i),
                    dec(hashTable.bucket()[i].intValue())));
        }

        for (int i = 0; i < hashTable.chain().length; i++) {
            rows.add(new StructureFieldDto(
                    String.format("nchain[%d]", i),
                    dec(hashTable.chain()[i].intValue())));
        }

        return rows;
    }
}

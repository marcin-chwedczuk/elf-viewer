package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfStringTableSection;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfStringTableSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfStringTableSection<NATIVE_WORD> section;

    public ElfStringTableSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                         ElfStringTableSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Index", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("Value", indexAccessor(1)),
                mkColumn("Length", indexAccessor(2), ALIGN_RIGHT)
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return section.stringTable().getContents().stream()
                .map(entry -> mkStrings(
                        hex(entry.getIndex().intValue()),
                        entry.getValue()))
                .collect(toList());
    }
}

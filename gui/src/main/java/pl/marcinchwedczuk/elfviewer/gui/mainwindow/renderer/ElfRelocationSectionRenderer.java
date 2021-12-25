package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfRelocationSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfRelocationSection<NATIVE_WORD> relocationSection;

    public ElfRelocationSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                        StringProperty searchPhase,
                                        ElfRelocationSection<NATIVE_WORD> relocationSection) {
        super(nativeWord, searchPhase);
        this.relocationSection = relocationSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        // TODO: Intel relocations
        return List.of(
                mkColumn("r_offset", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("r_info", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("R_TYPE", indexAccessor(2), ALIGN_RIGHT),
                mkColumn("R_SYM", indexAccessor(3), ALIGN_RIGHT)
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return relocationSection.relocations().stream()
                .map(rel -> mkStrings(
                        rel.offset().toString(),
                        hex(rel.info()),
                        hex(rel.type()),
                        hex(rel.symbol())))
                .collect(toList());
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

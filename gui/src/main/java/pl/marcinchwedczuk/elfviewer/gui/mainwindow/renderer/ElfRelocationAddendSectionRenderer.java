package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationAddendSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.RelocationAddendDto;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.RelocationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfRelocationAddendSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfRelocationAddendSection<NATIVE_WORD> relocationSection;

    public ElfRelocationAddendSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                              ElfRelocationAddendSection<NATIVE_WORD> relocationSection) {
        super(nativeWord);
        this.relocationSection = relocationSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        // TODO: Intel relocations
        return List.of(
                mkColumn("r_offset", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("r_info", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("R_TYPE", indexAccessor(2), ALIGN_RIGHT),
                mkColumn("R_SYM", indexAccessor(3), ALIGN_RIGHT),
                mkColumn("r_addend", indexAccessor(4), ALIGN_RIGHT)
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return relocationSection.relocations().stream()
                .map(rel -> mkStrings(
                        rel.offset().toString(),
                        hex(rel.info()),
                        hex(rel.type()),
                        hex(rel.symbol()),
                        hex(rel.addend())))
                .collect(toList());
    }
}

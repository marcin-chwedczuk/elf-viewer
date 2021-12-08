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

public class Elf32RelocationAddendSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<RelocationAddendDto, NATIVE_WORD>
{
    private final ElfRelocationAddendSection<NATIVE_WORD> relocationSection;

    public Elf32RelocationAddendSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                                ElfRelocationAddendSection<NATIVE_WORD> relocationSection) {
        super(nativeWord);
        this.relocationSection = relocationSection;
    }

    @Override
    protected List<TableColumn<RelocationAddendDto, String>> defineColumns() {
        // TODO: Intel relocations
        return List.of(
                mkColumn("r_offset", RelocationAddendDto::getOffset, ALIGN_RIGHT),
                mkColumn("r_info", RelocationAddendDto::getInfo, ALIGN_RIGHT),
                mkColumn("R_TYPE", RelocationAddendDto::getType, ALIGN_RIGHT),
                mkColumn("R_SYM", RelocationAddendDto::getSymbol, ALIGN_RIGHT),
                mkColumn("r_addend", RelocationAddendDto::getAddend, ALIGN_RIGHT)
        );
    }

    @Override
    protected List<? extends RelocationAddendDto> defineRows() {
        return relocationSection.relocations().stream()
                .map(rel -> new RelocationAddendDto(
                        rel.offset().toString(),
                        hex(rel.info()),
                        hex(rel.type()),
                        hex(rel.symbol()),
                        hex(rel.addend())))
                .collect(toList());
    }
}

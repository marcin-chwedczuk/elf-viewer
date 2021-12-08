package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.RelocationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfRelocationSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<RelocationDto, NATIVE_WORD>
{
    private final ElfRelocationSection<NATIVE_WORD> relocationSection;

    public ElfRelocationSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                        ElfRelocationSection<NATIVE_WORD> relocationSection) {
        super(nativeWord);
        this.relocationSection = relocationSection;
    }

    @Override
    protected List<TableColumn<RelocationDto, String>> defineColumns() {
        // TODO: Intel relocations
        return List.of(
                mkColumn("r_offset", RelocationDto::getOffset, ALIGN_RIGHT),
                mkColumn("r_info", RelocationDto::getInfo, ALIGN_RIGHT),
                mkColumn("R_TYPE", RelocationDto::getType, ALIGN_RIGHT),
                mkColumn("R_SYM", RelocationDto::getSymbol, ALIGN_RIGHT)
        );
    }

    @Override
    protected List<? extends RelocationDto> defineRows() {
        return relocationSection.relocations().stream()
                .map(rel -> new RelocationDto(
                        rel.offset().toString(),
                        hex(rel.info()),
                        hex(rel.type()),
                        hex(rel.symbol())))
                .collect(toList());
    }
}

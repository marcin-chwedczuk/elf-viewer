package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32RelocationSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.RelocationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32RelocationSectionRenderer extends BaseRenderer<RelocationDto> {
    private final Elf32RelocationSection relocationSection;

    public Elf32RelocationSectionRenderer(Elf32RelocationSection relocationSection) {
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

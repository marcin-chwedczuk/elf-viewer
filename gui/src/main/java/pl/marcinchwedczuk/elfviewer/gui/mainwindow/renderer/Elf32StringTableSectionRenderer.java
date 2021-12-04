package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32StringTableSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StringTableEntryDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32StringTableSectionRenderer extends BaseRenderer<StringTableEntryDto> {
    private final Elf32StringTableSection section;

    public Elf32StringTableSectionRenderer(Elf32StringTableSection section) {
        this.section = section;
    }

    @Override
    protected List<TableColumn<StringTableEntryDto, String>> defineColumns() {
        return List.of(
                mkColumn("Index", StringTableEntryDto::getFieldName, ALIGN_RIGHT),
                mkColumn("Value", StringTableEntryDto::getValue),
                mkColumn("Length", StringTableEntryDto::getValueLength, ALIGN_RIGHT)
        );
    }

    @Override
    protected List<? extends StringTableEntryDto> defineRows() {
        return section.stringTable().getContents().stream()
                .map(entry -> new StringTableEntryDto(
                        hex(entry.getIndex().intValue()),
                        entry.getValue()))
                .collect(toList());
    }
}

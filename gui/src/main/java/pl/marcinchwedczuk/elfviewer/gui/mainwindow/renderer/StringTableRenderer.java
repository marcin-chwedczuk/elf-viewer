package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTable;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StringTableEntryDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class StringTableRenderer extends BaseRenderer<StringTableEntryDto> {
    private final StringTable stringTable;

    public StringTableRenderer(StringTable stringTable) {
        this.stringTable = stringTable;
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
        return stringTable.getContents().stream()
                .map(entry -> new StringTableEntryDto(
                        hex(entry.getIndex().intValue()),
                        entry.getValue()))
                .collect(toList());
    }
}

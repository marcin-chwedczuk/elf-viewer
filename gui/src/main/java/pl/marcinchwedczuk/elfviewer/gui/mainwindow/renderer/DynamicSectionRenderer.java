package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32DynamicSection;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class DynamicSectionRenderer extends BaseRenderer<DynamicTagDto> {
    private final Elf32DynamicSection dynamicSection;

    public DynamicSectionRenderer(Elf32DynamicSection dynamicSection) {
        this.dynamicSection = dynamicSection;
    }

    @Override
    protected List<TableColumn<DynamicTagDto, String>> defineColumns() {
        return List.of(
                mkColumn("d_tag", DynamicTagDto::getType),
                mkColumn("d_val\nd_ptr", DynamicTagDto::getValue, ALIGN_RIGHT)
        );
    }

    @Override
    protected List<? extends DynamicTagDto> defineRows() {
        return dynamicSection.dynamicTags().stream()
                .map(tag -> new DynamicTagDto(
                        tag.type().toString(),
                        hex(tag.value())))
                .collect(toList());
    }
}
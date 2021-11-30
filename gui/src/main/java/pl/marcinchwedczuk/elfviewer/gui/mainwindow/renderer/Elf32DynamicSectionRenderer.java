package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32DynamicSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.DynamicTagDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTagType.NEEDED;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32DynamicSectionRenderer extends BaseRenderer<DynamicTagDto> {
    private final Elf32DynamicSection dynamicSection;

    public Elf32DynamicSectionRenderer(Elf32DynamicSection dynamicSection) {
        this.dynamicSection = dynamicSection;
    }

    @Override
    protected List<TableColumn<DynamicTagDto, String>> defineColumns() {
        return List.of(
                mkColumn("d_tag", DynamicTagDto::getType),
                mkColumn("d_val\nd_ptr", DynamicTagDto::getValue, ALIGN_RIGHT),
                mkColumn("(comment)", DynamicTagDto::getComment)
        );
    }

    @Override
    protected List<? extends DynamicTagDto> defineRows() {
        return dynamicSection.dynamicTags().stream()
                .map(tag -> new DynamicTagDto(
                        tag.type().toString(),
                        hex(tag.value()),
                        generateComment(tag)))
                .collect(toList());
    }

    private String generateComment(Elf32DynamicTag tag) {
        if (tag.type().is(NEEDED)) {
            return dynamicSection.getDynamicLibraryName(tag)
                .map(name -> String.format("requires library: %s", name))
                .orElse("");
        }

        return "";
    }
}

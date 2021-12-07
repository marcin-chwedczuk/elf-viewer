package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfDynamicSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.DynamicTagDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTagType.NEEDED;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32DynamicSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<DynamicTagDto, NATIVE_WORD>
{
    private final ElfDynamicSection<NATIVE_WORD> dynamicSection;

    public Elf32DynamicSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                       ElfDynamicSection<NATIVE_WORD> dynamicSection) {
        super(nativeWord);
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
                        tag.type().apiName(),
                        hex(tag.value()),
                        generateComment(tag)))
                .collect(toList());
    }

    private String generateComment(ElfDynamicTag<NATIVE_WORD> tag) {
        if (tag.type().is(NEEDED)) {
            return dynamicSection.getDynamicLibraryName(tag)
                .map(name -> String.format("requires library: %s", name))
                .orElse("");
        }

        return "";
    }
}

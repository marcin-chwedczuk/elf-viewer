package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfDynamicSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTagType.NEEDED;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTagType.VERNEEDNUM;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfDynamicSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfDynamicSection<NATIVE_WORD> dynamicSection;

    public ElfDynamicSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                     ElfDynamicSection<NATIVE_WORD> dynamicSection) {
        super(nativeWord);
        this.dynamicSection = dynamicSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("d_tag", indexAccessor(0)),
                mkColumn("d_val\nd_ptr", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("(comment)", indexAccessor(2))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return dynamicSection.dynamicTags().stream()
                .map(tag -> new String[] {
                    tag.type().apiName(),
                    hex(tag.value()),
                    generateComment(tag)
                })
                .collect(toList());
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }

    private String generateComment(ElfDynamicTag<NATIVE_WORD> tag) {
        if (tag.type().is(NEEDED)) {
            return dynamicSection.getDynamicLibraryName(tag)
                .map(name -> String.format("requires library: %s", name))
                .orElse("");
        }

        if (tag.type().is(VERNEEDNUM)) {
            return "number of entries in .gnu.version_r section";
        }

        return "";
    }
}

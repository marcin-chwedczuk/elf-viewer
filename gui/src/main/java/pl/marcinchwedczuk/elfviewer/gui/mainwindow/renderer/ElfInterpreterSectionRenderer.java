package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInterpreterSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

public class ElfInterpreterSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfInterpreterSection interpreterSection;

    public ElfInterpreterSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                         ElfInterpreterSection interpreterSection) {
        super(nativeWord);
        this.interpreterSection = interpreterSection;
    }

    @Override
    protected List<TableColumn<String, String>> defineColumns() {
        return List.of(
                mkColumn("Interpreter", identity())
        );
    }

    @Override
    protected List<String> defineRows() {
        return List.of(
                interpreterSection.interpreterPath()
        );
    }

    @Override
    protected Predicate<String> createFilter(String searchPhrase) {
        return mkStringFilter(searchPhrase);
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInterpreterSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

public class NothingRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<Void, NATIVE_WORD>
{
    public NothingRenderer(NativeWord<NATIVE_WORD> nativeWord) {
        super(nativeWord);
    }

    @Override
    protected List<TableColumn<Void, String>> defineColumns() {
        return List.of();
    }

    @Override
    protected List<Void> defineRows() {
        return List.of();
    }

    @Override
    protected Predicate<Void> createFilter(String searchPhrase) {
        return (nothing) -> true;
    }
}

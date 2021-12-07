package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInterpreterSection;

import java.util.List;

import static java.util.function.Function.identity;

public class Elf32InterpreterSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfInterpreterSection interpreterSection;

    public Elf32InterpreterSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
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
    protected List<? extends String> defineRows() {
        return List.of(
                interpreterSection.interpreterPath()
        );
    }
}

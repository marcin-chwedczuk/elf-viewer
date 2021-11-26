package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32InterpreterSection;

import java.util.List;

import static java.util.function.Function.identity;

public class InterpreterSectionRenderer extends BaseRenderer<String> {
    private final Elf32InterpreterSection interpreterSection;

    public InterpreterSectionRenderer(Elf32InterpreterSection interpreterSection) {
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

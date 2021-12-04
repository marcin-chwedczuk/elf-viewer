package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;

import java.util.List;

import static java.util.function.Function.identity;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class Elf32SectionStringsViewRenderer extends BaseRenderer<String> {
    private final Elf32BasicSection section;

    public Elf32SectionStringsViewRenderer(Elf32BasicSection section) {
        if (!section.header().flags().hasFlag(STRINGS))
            throw new IllegalArgumentException("Section contents does not consist from strings.");

        this.section = section;
    }

    @Override
    protected List<TableColumn<String, String>> defineColumns() {
        return List.of(
                mkColumn("(value)", identity())
        );
    }

    @Override
    protected List<? extends String> defineRows() {
        return section.readContentsAsStrings();
    }
}

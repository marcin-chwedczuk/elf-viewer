package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;

import java.util.List;

import static java.util.function.Function.identity;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class ElfSectionStringsViewRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfSection<NATIVE_WORD> section;

    public ElfSectionStringsViewRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                         ElfSection<NATIVE_WORD> section) {
        super(nativeWord);
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

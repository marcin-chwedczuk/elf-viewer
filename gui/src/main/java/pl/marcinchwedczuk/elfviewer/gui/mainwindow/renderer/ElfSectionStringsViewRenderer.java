package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Function.identity;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionAttributes.STRINGS;

public class ElfSectionStringsViewRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfSection<NATIVE_WORD> section;

    public ElfSectionStringsViewRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                         StringProperty searchPhase,
                                         ElfSection<NATIVE_WORD> section) {
        super(nativeWord, searchPhase);
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
    protected List<String> defineRows() {
        return section.readContentsAsStrings();
    }

    @Override
    protected Predicate<String> createFilter(String searchPhrase) {
        String lowerCasePhrase = searchPhrase == null ? "" : searchPhrase.toLowerCase();
        return row -> row != null && row.toLowerCase().contains(lowerCasePhrase);
    }
}

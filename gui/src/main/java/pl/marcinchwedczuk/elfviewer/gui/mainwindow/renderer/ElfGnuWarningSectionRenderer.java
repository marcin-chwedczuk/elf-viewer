package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuWarningSection;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

public class ElfGnuWarningSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfGnuWarningSection warningSection;

    public ElfGnuWarningSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                        StringProperty searchPhase,
                                        ElfGnuWarningSection warningSection) {
        super(nativeWord, searchPhase);
        this.warningSection = warningSection;
    }

    @Override
    protected List<TableColumn<String, String>> defineColumns() {
        return List.of(
                mkColumn("Warning", identity())
        );
    }

    @Override
    protected List<String> defineRows() {
        return List.of(
                warningSection.warning()
        );
    }

    @Override
    protected Predicate<String> createFilter(String searchPhrase) {
        String lowerCasePhrase = searchPhrase == null ? "" : searchPhrase.toLowerCase();
        return row -> row != null && row.toLowerCase().contains(lowerCasePhrase);
    }
}

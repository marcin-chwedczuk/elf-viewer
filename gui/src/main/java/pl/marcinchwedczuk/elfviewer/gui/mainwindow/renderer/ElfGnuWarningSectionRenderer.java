package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuWarningSection;

import java.util.List;

import static java.util.function.Function.identity;

public class ElfGnuWarningSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String, NATIVE_WORD>
{
    private final ElfGnuWarningSection warningSection;

    public ElfGnuWarningSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                        ElfGnuWarningSection warningSection) {
        super(nativeWord);
        this.warningSection = warningSection;
    }

    @Override
    protected List<TableColumn<String, String>> defineColumns() {
        return List.of(
                mkColumn("Warning", identity())
        );
    }

    @Override
    protected List<? extends String> defineRows() {
        return List.of(
                warningSection.warning()
        );
    }
}

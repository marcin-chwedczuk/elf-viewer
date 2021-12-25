package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInvalidSection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.function.Predicate;

public class ElfInvalidSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfInvalidSection<NATIVE_WORD> invalidSection;

    public ElfInvalidSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                     ElfInvalidSection<NATIVE_WORD> invalidSection) {
        super(nativeWord);
        this.invalidSection = invalidSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Name", indexAccessor(0)),
                mkColumn("Value", indexAccessor(1))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        invalidSection.error().printStackTrace(new PrintStream(bos, true));
        String stackTrace = bos.toString();

        return List.of(
                mkStrings("Exception Class", invalidSection.error().getClass().getName()),
                mkStrings("Message", invalidSection.error().getMessage()),
                mkStrings("Stack Trace", stackTrace)
        );
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

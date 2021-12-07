package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInvalidSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class Elf32InvalidSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<StructureFieldDto, NATIVE_WORD>
{
    private final ElfInvalidSection<NATIVE_WORD> invalidSection;

    public Elf32InvalidSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                       ElfInvalidSection<NATIVE_WORD> invalidSection) {
        super(nativeWord);
        this.invalidSection = invalidSection;
    }

    @Override
    protected List<TableColumn<StructureFieldDto, String>> defineColumns() {
        return List.of(
                mkColumn("Name", StructureFieldDto::getFieldName),
                mkColumn("Value", StructureFieldDto::getRawValue)
        );
    }

    @Override
    protected List<? extends StructureFieldDto> defineRows() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        invalidSection.error().printStackTrace(new PrintStream(bos, true));
        String stackTrace = bos.toString();

        return List.of(
                new StructureFieldDto("Exception Class",
                    invalidSection.error().getClass().getName()),
                new StructureFieldDto("Message",
                        invalidSection.error().getMessage()),
                new StructureFieldDto("Stack Trace",
                        stackTrace)
        );
    }
}

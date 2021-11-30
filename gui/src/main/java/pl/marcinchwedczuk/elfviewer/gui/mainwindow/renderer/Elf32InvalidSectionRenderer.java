package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32InvalidSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class Elf32InvalidSectionRenderer extends BaseRenderer<StructureFieldDto> {
    private final Elf32InvalidSection invalidSection;

    public Elf32InvalidSectionRenderer(Elf32InvalidSection invalidSection) {
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

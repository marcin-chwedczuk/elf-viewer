package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32SegmentRenderer extends BaseRenderer<StructureFieldDto> {
    private final Elf32Segment segment;

    public Elf32SegmentRenderer(Elf32Segment segment) {
        this.segment = segment;
    }

    @Override
    protected List<TableColumn<StructureFieldDto, String>> defineColumns() {
        return List.of(
                mkColumn("Field Name", StructureFieldDto::getFieldName),
                mkColumn("Raw\nValue", StructureFieldDto::getRawValue, ALIGN_RIGHT),
                mkColumn("Parsed\nValue", StructureFieldDto::getParsedValue),
                mkColumn("Comment", StructureFieldDto::getComment)
        );
    }

    @Override
    protected List<? extends StructureFieldDto> defineRows() {
        Elf32ProgramHeader ph = segment.programHeader();

        return List.of(
                new StructureFieldDto("p_type",
                        hex(ph.type().value()),
                        ph.type().name(),
                        ""),

                new StructureFieldDto("p_offset", ph.fileOffset()),
                new StructureFieldDto("p_vaddr", ph.virtualAddress()),
                new StructureFieldDto("p_paddr", ph.physicalAddress()),
                new StructureFieldDto("p_filesz", ph.fileSize()),
                new StructureFieldDto("p_memsz", ph.memorySize()),

                new StructureFieldDto("p_flags",
                        hex(ph.flags().intValue()),
                        ph.flags().toString(),
                        ""),

                new StructureFieldDto("p_align", ph.alignment())
        );
    }
}

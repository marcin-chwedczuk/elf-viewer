package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32HeaderRenderer extends BaseRenderer<StructureFieldDto> {
    private final Elf32Header header;

    public Elf32HeaderRenderer(Elf32Header header) {
        this.header = header;
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
        return List.of(
                new StructureFieldDto("e_type",
                        hex(header.type().value()),
                        header.type().name(),
                        ""),

                new StructureFieldDto("e_machine",
                        hex(header.machine().value()),
                        header.machine().name(),
                        ""),

                new StructureFieldDto("e_version",
                        hex(header.version().value()),
                        header.version().name(),
                        ""),

                new StructureFieldDto("e_entry", header.entry()),
                new StructureFieldDto("e_phoff", header.programHeaderTableOffset()),
                new StructureFieldDto("e_shoff", header.sectionHeaderTableOffset()),

                new StructureFieldDto("e_flags",
                        hex(header.flags())),

                new StructureFieldDto("e_ehsize", header.headerSize()),

                new StructureFieldDto("e_phentsize", header.programHeaderSize()),
                new StructureFieldDto("e_phnum", header.numberOfProgramHeaders()),

                new StructureFieldDto("e_shentsize", header.sectionHeaderSize()),
                new StructureFieldDto("e_shnum", header.numberOfSectionHeaders()),

                new StructureFieldDto("e_shstrndx",
                        hex(header.sectionContainingSectionNames().intValue()))
        );
    }
}

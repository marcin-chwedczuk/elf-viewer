package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class SectionHeaderRenderer extends BaseRenderer<StructureFieldDto> {
    private final Elf32SectionHeader header;

    public SectionHeaderRenderer(Elf32SectionHeader header) {
        this.header = header;
    }

    @Override
    protected List<TableColumn<StructureFieldDto, String>> defineColumns() {
        return List.of(
                mkColumn("Field\nName", StructureFieldDto::getFieldName),
                mkColumn("Value\nRaw", StructureFieldDto::getRawValue, ALIGN_RIGHT),
                mkColumn("Value\nParsed", StructureFieldDto::getParsedValue),
                mkColumn("Comment", StructureFieldDto::getComment)
        );
    }

    @Override
    protected List<? extends StructureFieldDto> defineRows() {
        return List.of(
                new StructureFieldDto("sh_name",
                        hex(header.nameIndex().intValue()),
                        header.name(),
                        ""),

                new StructureFieldDto("sh_type",
                        hex(header.type().value()),
                        header.type().name(), // TODO: Api name
                        ""),

                new StructureFieldDto("sh_flags",
                        hex(header.flags().intValue()),
                        header.flags().toString(),
                        ""),

                new StructureFieldDto("sh_addr", header.inMemoryAddress()),
                new StructureFieldDto("sh_offset", header.fileOffset()),
                new StructureFieldDto("sh_size", header.size()),
                new StructureFieldDto("sh_link", hex(header.link())),
                new StructureFieldDto("sh_info", hex(header.info())),
                new StructureFieldDto("sh_addralign", header.addressAlignment()),
                new StructureFieldDto("sh_entsize", header.containedEntrySize())
        );
    }
}

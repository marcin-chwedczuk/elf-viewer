package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32SectionHeaderRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<StructureFieldDto, NATIVE_WORD>
{
    private final ElfSectionHeader<NATIVE_WORD> header;

    public Elf32SectionHeaderRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                      ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord);
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
                        header.type().apiName(),
                        ""),

                new StructureFieldDto("sh_flags",
                        hex(header.flags().intValue()),
                        header.flags().toString(),
                        ""),

                new StructureFieldDto("sh_addr", header.virtualAddress()),
                new StructureFieldDto("sh_offset", header.fileOffset()),
                new StructureFieldDto("sh_size", dec(header.size())),
                new StructureFieldDto("sh_link", hex(header.link())),
                new StructureFieldDto("sh_info", hex(header.info())),
                new StructureFieldDto("sh_addralign", dec(header.addressAlignment())),
                new StructureFieldDto("sh_entsize", dec(header.containedEntrySize()))
        );
    }
}

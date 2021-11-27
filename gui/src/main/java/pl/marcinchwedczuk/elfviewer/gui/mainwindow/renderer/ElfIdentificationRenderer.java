package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StringTableEntryDto;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.StructureFieldDto;

import java.util.Arrays;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfIdentificationRenderer extends BaseRenderer<StructureFieldDto> {
    private final ElfIdentification identification;

    public ElfIdentificationRenderer(ElfIdentification identification) {
        this.identification = identification;
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
                new StructureFieldDto(
                        "EI_MAG0..3",
                        ByteArrays.toHexString(identification.magicBytes(), ":"),
                        identification.printableMagicString(),
                        ""),

                new StructureFieldDto("EI_CLASS",
                        hex(identification.elfClass().value()),
                        identification.elfClass().name(),
                        ""),

                new StructureFieldDto("EI_DATA",
                        hex(identification.elfData().value()),
                        identification.elfData().name(),
                        ""),

                new StructureFieldDto("EI_VERSION",
                        // This field occur twice in ELF header.
                        // In identification bytes it is 1 byte long.
                        hex((byte)identification.elfVersion().value()),
                        identification.elfVersion().name(),
                        ""),

                new StructureFieldDto("EI_OSABI",
                        hex(identification.osAbi().value()),
                        identification.osAbi().name(),
                        ""),

                new StructureFieldDto("EI_ABIVERSION",
                        hex(identification.osAbiVersion()),
                        dec(identification.osAbiVersion()),
                        ""),

                new StructureFieldDto("EI_PAD",
                        ByteArrays.toHexString(identification.paddingBytes(), ":"))
        );
    }
}
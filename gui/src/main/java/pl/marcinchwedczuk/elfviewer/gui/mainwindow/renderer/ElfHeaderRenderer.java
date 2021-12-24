package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfHeaderRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfHeader<NATIVE_WORD> header;

    public ElfHeaderRenderer(NativeWord<NATIVE_WORD> nativeWord,
                             ElfHeader<NATIVE_WORD> header) {
        super(nativeWord);
        this.header = header;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Field Name", indexAccessor(0)),
                mkColumn("Raw\nValue", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("Parsed\nValue", indexAccessor(2)),
                mkColumn("Comment", indexAccessor(3))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return List.of(
                mkStrings("e_type",
                        hex(header.type().value()),
                        header.type().apiName(),
                        ""),

                mkStrings("e_machine",
                        hex(header.machine().value()),
                        header.machine().apiName(),
                        ""),

                mkStrings("e_version",
                        hex(header.version().value()),
                        header.version().apiName(),
                        ""),

                mkStrings("e_entry", header.entry().toString()),
                mkStrings("e_phoff", header.programHeaderTableOffset().toString()),
                mkStrings("e_shoff", header.sectionHeaderTableOffset().toString()),

                mkStrings("e_flags", hex(header.flags())),

                mkStrings("e_ehsize", dec(header.headerSize())),

                mkStrings("e_phentsize", dec(header.programHeaderSize())),
                mkStrings("e_phnum", dec(header.numberOfProgramHeaders())),

                mkStrings("e_shentsize", dec(header.sectionHeaderSize())),
                mkStrings("e_shnum", dec(header.numberOfSectionHeaders())),

                mkStrings("e_shstrndx", hex(header.sectionContainingSectionNames().intValue()))
        );
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32SegmentRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<StructureFieldDto, NATIVE_WORD>
{
    private final ElfSegment<NATIVE_WORD> segment;

    public Elf32SegmentRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                ElfSegment<NATIVE_WORD> segment) {
        super(nativeWord);
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
        ElfProgramHeader<NATIVE_WORD> ph = segment.programHeader();

        return List.of(
                new StructureFieldDto("p_type",
                        hex(ph.type().value()),
                        ph.type().apiName(),
                        ""),

                new StructureFieldDto("p_offset", ph.fileOffset()),
                new StructureFieldDto("p_vaddr", ph.virtualAddress()),
                new StructureFieldDto("p_paddr", ph.physicalAddress()),
                // TODO: To human readable size e.g. 4kB
                new StructureFieldDto("p_filesz", dec(ph.fileSize())),
                new StructureFieldDto("p_memsz", dec(ph.memorySize())),

                new StructureFieldDto("p_flags",
                        hex(ph.flags().intValue()),
                        ph.flags().toString(),
                        ""),

                new StructureFieldDto("p_align", dec(ph.alignment()))
        );
    }
}

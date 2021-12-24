package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfSegmentRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfSegment<NATIVE_WORD> segment;

    public ElfSegmentRenderer(NativeWord<NATIVE_WORD> nativeWord,
                              ElfSegment<NATIVE_WORD> segment) {
        super(nativeWord);
        this.segment = segment;
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
        ElfProgramHeader<NATIVE_WORD> ph = segment.programHeader();

        return List.of(
                mkStrings("p_type",
                        hex(ph.type().value()),
                        ph.type().apiName(),
                        ""),

                mkStrings("p_offset", ph.fileOffset().toString()),
                mkStrings("p_vaddr", ph.virtualAddress().toString()),
                mkStrings("p_paddr", ph.physicalAddress().toString()),
                // TODO: To human readable size e.g. 4kB
                mkStrings("p_filesz", dec(ph.fileSize())),
                mkStrings("p_memsz", dec(ph.memorySize())),

                mkStrings("p_flags",
                        hex(ph.flags().intValue()),
                        ph.flags().toString(),
                        ""),

                mkStrings("p_align", dec(ph.alignment()))
        );
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuVersionSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInvalidSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.IndexValueDto;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.StructureFieldDto;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfGnuVersionSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfGnuVersionSection<NATIVE_WORD> section;

    public ElfGnuVersionSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                        ElfGnuVersionSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("(index)", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("(version)", indexAccessor(1))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return StreamUtils.zipWithIndex(section.symbolVersions())
                .map(svIndex -> new String[] {
                        dec(svIndex.index),
                        svIndex.value.apiName()
                })
                .collect(Collectors.toList());
    }
}

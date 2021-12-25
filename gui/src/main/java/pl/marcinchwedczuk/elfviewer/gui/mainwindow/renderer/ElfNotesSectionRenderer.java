package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfNotesSection;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfNotesSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfNotesSection<NATIVE_WORD> notesSection;

    public ElfNotesSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                   StringProperty searchPhase,
                                   ElfNotesSection<NATIVE_WORD> notesSection) {
        super(nativeWord, searchPhase);
        this.notesSection = notesSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("namesz", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("name", indexAccessor(1)),
                mkColumn("descsz", indexAccessor(2), ALIGN_RIGHT),
                mkColumn("desc", indexAccessor(3)),
                mkColumn("type", indexAccessor(4)),
                mkColumn("(type)", indexAccessor(5)),
                mkColumn("(comment)", indexAccessor(6))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return notesSection.notes().stream()
                .map(note -> toNoteRow(note))
                .collect(toList());
    }

    private String[] toNoteRow(ElfNote note) {
        return mkStrings(
                dec(note.nameLength()),
                note.name(),
                dec(note.descriptorLength()),
                ByteArrays.toHexString(note.descriptor(), ":"),
                hex(note.type()),
                (note instanceof ElfNoteGnu)
                    ? ((ElfNoteGnu)note).gnuType().apiName()
                    : "",
                comment(note)
        );
    }

    private String comment(ElfNote note) {
        if (note instanceof ElfNoteGnuABITag) {
            return String.format(
                    "Min supported Linux kernel version: %s",
                    ((ElfNoteGnuABITag)note).minSupportedKernelVersion());
        } else if (note instanceof ElfNoteGnuBuildId) {
            return String.format(
                    "Build ID: %s", ((ElfNoteGnuBuildId)note).buildId());
        } else if (note instanceof ElfNoteGnuBuildAttribute) {
            ElfNoteGnuBuildAttribute buildAttribute = (ElfNoteGnuBuildAttribute) note;
            return String.format("%s: %s",
                    buildAttribute.buildAttributeName(),
                    buildAttribute.buildAttributeValue());
        } else {
            return "";
        }
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

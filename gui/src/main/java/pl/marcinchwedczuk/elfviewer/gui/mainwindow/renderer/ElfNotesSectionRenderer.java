package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfNotesSection;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.NoteDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfNotesSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<NoteDto, NATIVE_WORD>
{
    private final ElfNotesSection<NATIVE_WORD> notesSection;

    public ElfNotesSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                   ElfNotesSection<NATIVE_WORD> notesSection) {
        super(nativeWord);
        this.notesSection = notesSection;
    }

    @Override
    protected List<TableColumn<NoteDto, String>> defineColumns() {
        return List.of(
                mkColumn("namesz", NoteDto::getNameLength, ALIGN_RIGHT),
                mkColumn("name", NoteDto::getName),
                mkColumn("descsz", NoteDto::getDescriptorLength, ALIGN_RIGHT),
                mkColumn("desc", NoteDto::getDescriptor),
                mkColumn("type", NoteDto::getType),
                mkColumn("(type)", NoteDto::getParsedType),
                mkColumn("(comment)", NoteDto::getComment)
        );
    }

    @Override
    protected List<? extends NoteDto> defineRows() {
        return notesSection.notes().stream()
                .map(note -> toNoteDto(note))
                .collect(toList());
    }

    private NoteDto toNoteDto(ElfNote note) {
        return new NoteDto(
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
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuABITag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuBuildId;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32NotesSection;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.NoteDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class NotesSectionRenderer extends BaseRenderer<NoteDto> {
    private final Elf32NotesSection notesSection;

    public NotesSectionRenderer(Elf32NotesSection notesSection) {
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

    private NoteDto toNoteDto(Elf32Note note) {
        return new NoteDto(
                dec(note.nameLength()),
                note.name(),
                dec(note.descriptorLength()),
                ByteArrays.toHexString(note.descriptor(), ":"),
                hex(note.type()),
                (note instanceof Elf32NoteGnu)
                    ? ((Elf32NoteGnu)note).gnuType().toString()
                    : "",
                comment(note)
        );
    }

    private String comment(Elf32Note note) {
        if (note instanceof Elf32NoteGnuABITag) {
            return String.format(
                    "Min supported Linux kernel version: %s",
                    ((Elf32NoteGnuABITag)note).minSupportedKernelVersion());
        } else if (note instanceof Elf32NoteGnuBuildId) {
            return String.format(
                    "Build ID: %s", ((Elf32NoteGnuBuildId)note).buildId());
        } else {
            return "";
        }
    }
}

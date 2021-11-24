package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuABITag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuBuildId;

public class GuiNote {
    public final int nameLength;
    public final String name;
    public final int descriptorLength;
    public final byte[] descriptor;
    public final int type;

    public final String parsedType;
    public final String comment;

    public GuiNote(Elf32Note elfNote) {
        this.nameLength = elfNote.nameLength();
        this.name = elfNote.name();
        this.descriptorLength = elfNote.descriptorLength();
        this.descriptor = elfNote.descriptor();
        this.type = elfNote.type();

        if (elfNote instanceof Elf32NoteGnuABITag) {
            this.parsedType = ((Elf32NoteGnu)elfNote).gnuType().toString();
            this.comment = String.format(
                    "Min supported Linux kernel version: %s",
                    ((Elf32NoteGnuABITag)elfNote).minSupportedKernelVersion());
        } else if (elfNote instanceof Elf32NoteGnuBuildId) {
            this.parsedType = ((Elf32NoteGnu)elfNote).gnuType().toString();
            this.comment = String.format(
                    "Build ID: %s", ((Elf32NoteGnuBuildId)elfNote).buildId());
        } else {
            this.parsedType = "";
            this.comment = "";
        }
    }
}

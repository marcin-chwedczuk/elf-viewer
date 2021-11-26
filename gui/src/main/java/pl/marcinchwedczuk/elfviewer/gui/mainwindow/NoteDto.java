package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

public class NoteDto {
    public final String nameLength;
    public final String name;
    public final String descriptorLength;
    public final String descriptor;
    public final String type;

    public final String parsedType;
    public final String comment;

    public NoteDto(String nameLength,
                   String name,
                   String descriptorLength,
                   String descriptor,
                   String type,
                   String parsedType,
                   String comment) {
        this.nameLength = nameLength;
        this.name = name;
        this.descriptorLength = descriptorLength;
        this.descriptor = descriptor;
        this.type = type;
        this.parsedType = parsedType;
        this.comment = comment;
    }

    /*
    public NoteDto(Elf32Note elfNote) {
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
    */

    public String getNameLength() {
        return nameLength;
    }

    public String getName() {
        return name;
    }

    public String getDescriptorLength() {
        return descriptorLength;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getType() {
        return type;
    }

    public String getParsedType() {
        return parsedType;
    }

    public String getComment() {
        return comment;
    }
}

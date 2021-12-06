package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNote;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf32_Nhdr")
public class Elf32Note {
    private final ElfNote note;

    public Elf32Note(ElfNote note) {
        this.note = note;
    }

    public int nameLength() {
        return note.nameLength();
    }

    public String name() {
        return note.name();
    }

    public int descriptorLength() {
        return note.descriptorLength();
    }

    public byte[] descriptor() {
        return note.descriptor();
    }

    public int type() {
        return note.type();
    }

    @Override
    public String toString() {
       return note.toString();
    }
}

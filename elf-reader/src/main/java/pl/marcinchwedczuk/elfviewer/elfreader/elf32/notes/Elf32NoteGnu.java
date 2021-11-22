package pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteTypeGnu.GNU_ABI_TAG;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteTypeGnu.GNU_BUILD_ID;

public class Elf32NoteGnu extends Elf32Note {
    public static Elf32NoteGnu createGnuNote(int nameLength,
                                             String name,
                                             int descriptorLength,
                                             byte[] descriptor,
                                             int type)
    {
        if (!"GNU".equals(name)) {
            throw new IllegalArgumentException(
                    "Invalid GNU note name, expecting 'GNU' but got '" + name + "'.");
        }

        Elf32NoteTypeGnu gnuType = Elf32NoteTypeGnu.fromValue(type);
        if (gnuType.is(GNU_ABI_TAG)) {
            return new Elf32NoteGnuABITag(nameLength, name, descriptorLength, descriptor, gnuType);
        } if (gnuType.is(GNU_BUILD_ID)) {
            return new Elf32NoteGnuBuildId(nameLength, name, descriptorLength, descriptor, gnuType);
        } else {
            return new Elf32NoteGnu(nameLength, name, descriptorLength, descriptor, gnuType);
        }
    }

    public Elf32NoteGnu(int nameLength,
                        String name,
                        int descriptorLength,
                        byte[] descriptor,
                        Elf32NoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type.value());
    }

    public Elf32NoteTypeGnu gnuType() {
        return Elf32NoteTypeGnu.fromValue(type());
    }
}

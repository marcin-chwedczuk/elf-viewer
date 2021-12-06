package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;


import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_ABI_TAG;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_BUILD_ID;

public class ElfNoteGnu extends ElfNote {
    public static ElfNoteGnu createGnuNote(int nameLength,
                                           String name,
                                           int descriptorLength,
                                           byte[] descriptor,
                                           int type)
    {
        if (!"GNU".equals(name)) {
            throw new IllegalArgumentException(
                    "Invalid GNU note name, expecting 'GNU' but got '" + name + "'.");
        }

        ElfNoteTypeGnu gnuType = ElfNoteTypeGnu.fromValue(type);
        if (gnuType.is(GNU_ABI_TAG)) {
            return new ElfNoteGnuABITag(nameLength, name, descriptorLength, descriptor, gnuType);
        } if (gnuType.is(GNU_BUILD_ID)) {
            return new ElfNoteGnuBuildId(nameLength, name, descriptorLength, descriptor, gnuType);
        } else {
            return new ElfNoteGnu(nameLength, name, descriptorLength, descriptor, gnuType);
        }
    }

    public ElfNoteGnu(int nameLength,
                      String name,
                      int descriptorLength,
                      byte[] descriptor,
                      ElfNoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type.value());
    }

    public ElfNoteTypeGnu gnuType() {
        return ElfNoteTypeGnu.fromValue(type());
    }
}

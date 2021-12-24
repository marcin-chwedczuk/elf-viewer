package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;


import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_ABI_TAG;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_BUILD_ID;

public class ElfNoteGnu extends ElfNote {


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

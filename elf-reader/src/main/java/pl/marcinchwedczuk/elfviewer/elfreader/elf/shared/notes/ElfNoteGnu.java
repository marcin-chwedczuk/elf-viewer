package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;


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

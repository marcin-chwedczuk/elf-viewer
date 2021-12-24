package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

public class ElfNoteFactory {
    public ElfNote mkNote(int nameLength,
                          String name,
                          int descriptorLength,
                          byte[] descriptor,
                          int type) {
        if ("GNU".equals(name)) {
            return ElfNoteGnu.createGnuNote(
                    nameLength, name,
                    descriptorLength, descriptor,
                    type);
        } else {
            return new ElfNote(
                    nameLength, name,
                    descriptorLength, descriptor,
                    type);
        }
    }
}

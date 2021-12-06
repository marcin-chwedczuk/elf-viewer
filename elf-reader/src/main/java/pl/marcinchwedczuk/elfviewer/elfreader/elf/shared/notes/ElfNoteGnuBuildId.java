package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import java.math.BigInteger;

public class ElfNoteGnuBuildId extends ElfNoteGnu {

    public ElfNoteGnuBuildId(int nameLength,
                             String name,
                             int descriptorLength,
                             byte[] descriptor,
                             ElfNoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type);
    }

    public String buildId() {
        // Convert bytes to hex string
        String result = new BigInteger(1, descriptor()).toString(16);
        // TODO: Wrap into util class
        return result;
    }
}

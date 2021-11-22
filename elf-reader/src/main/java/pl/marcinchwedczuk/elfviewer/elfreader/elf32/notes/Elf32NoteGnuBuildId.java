package pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteTypeGnu.GNU_ABI_TAG;

public class Elf32NoteGnuBuildId extends Elf32NoteGnu {

    public Elf32NoteGnuBuildId(int nameLength,
                               String name,
                               int descriptorLength,
                               byte[] descriptor,
                               Elf32NoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type);
    }

    public String buildId() {
        // Convert bytes to hex string
        String result = new BigInteger(1, descriptor()).toString(16);
        // TODO: Wrap into util class
        return result;
    }
}

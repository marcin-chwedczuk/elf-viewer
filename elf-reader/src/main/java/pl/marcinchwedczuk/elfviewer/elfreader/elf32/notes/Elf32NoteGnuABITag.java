package pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.nio.ByteBuffer;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteTypeGnu.GNU_ABI_TAG;

public class Elf32NoteGnuABITag extends Elf32NoteGnu {
    public Elf32NoteGnuABITag(
            int nameLength,
            String name,
            int descriptorLength,
            byte[] descriptor,
            Elf32NoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type);

        if (type.isNot(GNU_ABI_TAG)) {
            throw new IllegalArgumentException("Invalid note type, expecting " + GNU_ABI_TAG + ".");
        }
    }

    /**
     * For details see:
     * @see <a href="https://refspecs.linuxbase.org/LSB_3.0.0/LSB-PDA/LSB-PDA/noteabitag.html">Relevant Linux LSB specification part</a>
     *
     * @return Minimal supported kernel version.
     */
    public String minSupportedKernelVersion() {
        // TODO: Take Endianness from ELF
        StructuredFile sf = new StructuredFile(
                new InMemoryFile(descriptor()),
                new LittleEndian(),
                Elf32Offset.ZERO);

        if (descriptorLength() != 16 || sf.readUnsignedInt() != 0) {
            throw new IllegalStateException("Invalid OS signature or descriptor.");
        }

        // see: https://refspecs.linuxbase.org/LSB_3.0.0/LSB-PDA/LSB-PDA/noteabitag.html
        return String.format("%d.%d.%d",
                sf.readUnsignedInt(),
                sf.readUnsignedInt(),
                sf.readUnsignedInt());
    }
}

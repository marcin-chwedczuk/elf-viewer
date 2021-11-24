package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Arrays;

@ElfApi("Elf32_Nhdr")
public class Elf32Note {
    @ElfApi("namesz")
    private int nameLength;

    @ElfApi("name")
    private String name;

    @ElfApi("descsz")
    private int descriptorLength;

    @ElfApi("desc")
    private byte[] descriptor;

    @ElfApi("type")
    private int type;

    public Elf32Note(int nameLength,
                     String name,
                     int descriptorLength,
                     byte[] descriptor,
                     int type) {
        this.nameLength = nameLength;
        this.name = name;
        this.descriptorLength = descriptorLength;
        this.descriptor = descriptor;
        this.type = type;
    }

    /**
     * The length of the name field in bytes.  The contents will
     *               immediately follow this note in memory.  The name is null
     *               terminated.  For example, if the name is "GNU", then
     *               n_namesz will be set to 4.
     */
    public int nameLength() {
        return nameLength;
    }

    public String name() {
        return name;
    }

    /**
     * The length of the descriptor field in bytes.  The contents
     *               will immediately follow the name field in memory.
     */
    public int descriptorLength() {
        return descriptorLength;
    }

    public byte[] descriptor() {
        return Arrays.copyOf(descriptor, descriptor.length);
    }

    public int type() {
        return type;
    }

    @Override
    public String toString() {
       return String.format("%4d %s %4d %s 0x%08x",
               nameLength, name, descriptorLength, Arrays.toString(descriptor), type);
    }
}

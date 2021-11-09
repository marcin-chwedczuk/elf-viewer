package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import static java.util.Objects.requireNonNull;

/**
 * The file format is designed to be portable among machines of various sizes,
 * without imposing the sizes of the largest machine on the smallest.
 *
 * Class ELFCLASS32 supports machines with files and virtual address spaces up to 4 gigabytes;
 * it uses the basic types defined above.
 *
 * Class ELFCLASS64 is incomplete and refers to the 64-bit architectures.
 * Its appearance here shows how the object file may change.
 * Other classes will be defined as necessary,
 * with different basic types and sizes for object file data.
 */
public enum ElfClass {

    /**
     * Invalid class
     */
    ELF_CLASS_NONE("ELFCLASSNONE", 0),

    /**
     * 32-bit objects
     */
    ELF_CLASS_32("ELFCLASS32", 1),

    /**
     * 64-bit objects
     */
    ELF_CLASS_64("ELFCLASS64", 2);

    public static ElfClass fromByte(byte b) {
        for (ElfClass value : ElfClass.values()) {
            if (value.value == b) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized byte " + b);
    }

    private String name;
    private int value;

    ElfClass(String name, int value) {
        this.name = name;
        this.value = value;
    }
}

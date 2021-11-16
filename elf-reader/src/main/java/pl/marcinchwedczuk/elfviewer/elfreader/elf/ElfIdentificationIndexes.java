package pl.marcinchwedczuk.elfviewer.elfreader.elf;

public interface ElfIdentificationIndexes {
    int EI_MAG0 = 0;
    int EI_MAG1 = 1;
    int EI_MAG2 = 2;
    int EI_MAG3 = 3;

    /**
     * File class.
     */
    int EI_CLASS = 4;

    /**
     * Data encoding.
     */
    int EI_DATA = 5;

    /**
     * File version.
     */
    int EI_VERSION = 6;

    /**
     * Start of padding bytes.
     */
    int EI_PAD = 7;

    /**
     * Size of e_ident[].
     */
    int EI_NIDENT = 16;
}

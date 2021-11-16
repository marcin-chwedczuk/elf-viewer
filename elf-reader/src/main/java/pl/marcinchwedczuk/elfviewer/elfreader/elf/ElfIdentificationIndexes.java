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
     * OS ABI identification.
     */
    int EI_OSABI = 7;

    /**
     * ABI version.
     */
    int EI_ABIVERSION = 8;

    /**
     * Start of padding bytes.
     */
    int EI_PAD = 9;

    /**
     * Size of e_ident[].
     */
    int EI_NIDENT = 16;
}

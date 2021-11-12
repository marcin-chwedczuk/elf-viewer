package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum Elf32SymbolType {
    /**
     * The symbol's type is not specified.
     */
    @ElfApi("STT_NOTYPE")
    NoType(0),

    /**
     * The symbol is associated with a data object, such as a variable, an array, and so on.
     */
    @ElfApi("STT_OBJECT")
    Object(1),

    /**
     * The symbol is associated with a function or other executable code.
     */
    @ElfApi("STT_FUNC")
    Function(2),

    /**
     * The symbol is associated with a section. Symbol table entries of this type
     * exist primarily for relocation and normally have STB_LOCAL binding.
     */
    @ElfApi("STT_SECTION")
    Section(3),

    /**
     * A file symbol has STB_LOCAL binding, its section index is SHN_ABS,
     * and it precedes the other STB_LOCAL symbols for the file, if it is present.
     */
    @ElfApi("STT_FILE")
    File(4),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STT_LOPROC")
    LoReservedProcessorSpecific(13),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STT_HIPROC")
    HiReservedProcessorSpecific(15);

    public static Elf32SymbolType fromSymbolInfo(byte info) {
        byte v = (byte)(info & 0x0f);

        for (Elf32SymbolType value : Elf32SymbolType.values()) {
            // No sign extension byte -> int
            if (value.value == (v & 0xff)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized byte" + v);
    }

    private final int value;

    Elf32SymbolType(int value) {
        this.value = value;
    }
}

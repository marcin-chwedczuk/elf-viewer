package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum Elf32SymbolBinding {
    /**
     * Local symbols are not visible outside the object file containing their definition.
     * Local symbols of the same name may exist in multiple files without interfering with each other.
     */
    @ElfApi("STB_LOCAL")
    Local(0),

    /**
     * Global symbols are visible to all object files being combined.
     * One file's definition of a global symbol will satisfy another file's
     * undefined reference to the same global symbol.
     */
    @ElfApi("STB_GLOBAL")
    Global(1),

    /**
     * Weak symbols resemble global symbols, but their definitions have lower precedence.
     */
    @ElfApi("STB_WEAK")
    Weak(2),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STB_LOPROC")
    LoReservedProcessorSpecific(13),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     */
    @ElfApi("STB_HIPROC")
    HiReservedProcessorSpecific(15);

    public static Elf32SymbolBinding fromByte(byte v) {
        for (Elf32SymbolBinding value : Elf32SymbolBinding.values()) {
            // No sign extension byte -> int
            if (value.value == (v & 0xff)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized byte" + v);
    }

    private int value;

    Elf32SymbolBinding(int value) {
        this.value = value;
    }
}

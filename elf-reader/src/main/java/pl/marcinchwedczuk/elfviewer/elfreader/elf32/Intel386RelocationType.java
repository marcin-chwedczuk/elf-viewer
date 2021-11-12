package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

// TODO: Add docs, see page 57 of pdf in ./docs directory
public enum Intel386RelocationType {
    @ElfApi("R_386_NONE")
    None(0),

    @ElfApi("R_386_32")
    R32(1),

    @ElfApi("R_386_PC32")
    PC32(2);

    public static Intel386RelocationType fromType(int v) {
        throw new RuntimeException("Not impl.");
    }

    private int value;

    Intel386RelocationType(int value) {
        this.value = value;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32Header {
    @ElfApi("e_ident")
    private final ElfIdentification identification;

    @ElfApi("e_type")
    private final ElfType type;

    public Elf32Header(ElfIdentification identification,
                       ElfType type) {
        this.identification = identification;
        this.type = type;
    }

    public ElfIdentification identification() {
        return identification;
    }

    public ElfType type() {
        return type;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32Header {
    @ElfApi("e_ident")
    private final ElfIdentification identification;

    @ElfApi("e_type")
    private final ElfType type;

    @ElfApi("e_machine")
    private final ElfMachine machine;

    @ElfApi("e_version")
    private final ElfVersion version;

    @ElfApi("e_entry")
    private final Elf32Address entry;

    public Elf32Header(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       Elf32Address entry) {
        this.identification = identification;
        this.type = type;
        this.machine = machine;
        this.version = version;
        this.entry = entry;
    }

    public ElfIdentification identification() {
        return identification;
    }

    public ElfType type() {
        return type;
    }

    public ElfMachine machine() {
        return machine;
    }

    public ElfVersion version() {
        return version;
    }

    public Elf32Address entry() {
        return entry;
    }
}

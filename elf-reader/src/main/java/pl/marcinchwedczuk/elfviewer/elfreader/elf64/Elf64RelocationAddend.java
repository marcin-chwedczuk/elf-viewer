package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf64_Rela")
public class Elf64RelocationAddend {
    // TODO: Bind relocations with symbol names

    private final ElfRelocationAddend<Long> relocation;

    public Elf64RelocationAddend(ElfRelocationAddend<Long> relocation) {
        this.relocation = relocation;
    }

    public Elf64Address offset() {
        return new Elf64Address(relocation.offset());
    }

    public long info() {
        return relocation.info();
    }

    public int symbol() {
        return relocation.symbol();
    }

    public int type() {
        return relocation.type();
    }

    public Intel386RelocationType intel386RelocationType() {
        return relocation.intel386RelocationType();
    }

    public long addend() { return relocation.addend(); }

    @Override
    public String toString() {
        return relocation.toString();
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf64_Rel")
public class Elf64Relocation {
    private final ElfRelocation<Long> relocation;

    public Elf64Relocation(ElfRelocation<Long> relocation) {
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


    @Override
    public String toString() {
        return relocation.toString();
    }
}

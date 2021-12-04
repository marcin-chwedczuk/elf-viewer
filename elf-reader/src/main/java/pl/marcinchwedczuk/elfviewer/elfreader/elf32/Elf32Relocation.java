package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Rel")
public class Elf32Relocation {
    private final ElfRelocation<Integer> relocation;

    public Elf32Relocation(ElfRelocation<Integer> relocation) {
        this.relocation = relocation;
    }

    public Elf32Address offset() {
        return new Elf32Address(relocation.offset().value());
    }

    public int info() {
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

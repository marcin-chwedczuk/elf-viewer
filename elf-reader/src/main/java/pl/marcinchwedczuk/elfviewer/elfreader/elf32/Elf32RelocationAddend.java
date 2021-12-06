package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Rela")
public class Elf32RelocationAddend {
    private final ElfRelocationAddend<Integer> relocation;

    public Elf32RelocationAddend(ElfRelocationAddend<Integer> relocation) {
        this.relocation = relocation;
    }

    public Elf64Address offset() {
        return new Elf64Address(relocation.offset().value());
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

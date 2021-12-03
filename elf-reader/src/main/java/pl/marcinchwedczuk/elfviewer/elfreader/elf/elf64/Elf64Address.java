package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf64_Addr")
public class Elf64Address extends ElfAddress<Long> {
    public Elf64Address(long address) {
        super(address);
    }

    @Override
    protected ElfAddress<Long> mkAddress(long value) {
        return new Elf64Address(value);
    }

    public long longValue() { return value(); }

    @Override
    public String toString() {
        return String.format("0x%016x", longValue());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf64_Addr")
public class Elf64Address extends ElfAddress<Long> {
    public static final Elf64Address ZERO = new Elf64Address(0);

    public Elf64Address(long address) {
        super(address);
    }

    public Elf64Address(ElfAddress<Long> address) {
        super(address.value());
    }

    @Override
    protected ElfAddress<Long> mkAddress(Long value) {
        return new Elf64Address(value);
    }

    public long longValue() { return value(); }

    @Override
    public String toString() {
        return String.format("0x%016x", longValue());
    }
}

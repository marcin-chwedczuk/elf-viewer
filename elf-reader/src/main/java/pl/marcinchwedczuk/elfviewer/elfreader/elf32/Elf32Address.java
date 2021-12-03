package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Addr")
public class Elf32Address extends ElfAddress<Integer> {
    public Elf32Address(int address) {
        super(address);
    }

    @Override
    protected ElfAddress<Integer> mkAddress(long value) {
        return new Elf32Address(Math.toIntExact(value));
    }

    public int intValue() { return value(); }

    @Override
    public Elf32Address plus(long bytesCount) {
        return (Elf32Address) super.plus(bytesCount);
    }

    @Override
    public String toString() {
        return String.format("0x%08x", intValue());
    }
}

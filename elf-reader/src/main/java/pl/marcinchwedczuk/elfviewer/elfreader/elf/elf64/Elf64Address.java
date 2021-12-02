package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf64_Addr")
public class Elf64Address extends ElfAddress<Elf64Address> {
    private final long address;

    public Elf64Address(long address) {
        this.address = address;
    }

    public long longValue() { return address; }

    @Override
    public boolean isNull() {
        return (address == 0);
    }

    @Override
    public boolean isAfter(Elf64Address address) {
        return Long.compareUnsigned(this.address, address.address) > 0;
    }
    @Override
    public boolean isBefore(Elf64Address address) {
        return Long.compareUnsigned(this.address, address.address) < 0;
    }

    @Override
    public Elf64Address plus(long bytesCount) {
        // TODO: Check overflow
        return new Elf64Address(this.address + Math.toIntExact(bytesCount));
    }

    @Override
    public long minus(Elf64Address addr) {
        // Normal subtract works for unsigned int as well
        return this.address - addr.address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf64Address that = (Elf64Address) o;
        return address == that.address;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return String.format("0x%016x", address);
    }

}

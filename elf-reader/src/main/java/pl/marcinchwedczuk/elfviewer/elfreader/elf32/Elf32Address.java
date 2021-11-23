package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

@ElfApi("Elf32_Addr")
public class Elf32Address {
    private final int address;

    public Elf32Address(int address) {
        this.address = address;
    }

    public int intValue() { return address; }

    public boolean isNull() {
        return (address == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32Address that = (Elf32Address) o;
        return address == that.address;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return String.format("0x%08x", address);
    }

    public boolean isAfter(Elf32Address addr) {
        return this.address > addr.address;
    }

    public boolean isBefore(Elf32Address addr) {
        return this.address < addr.address;
    }

    public Elf32Address plus(int bytes) {
        // TODO: Check overflow
        return new Elf32Address(this.address + bytes);
    }

    public boolean isAfterOrAt(Elf32Address addr) {
        return this.address >= addr.address;
    }

    public long minus(Elf32Address addr) {
        return this.address - addr.address;
    }
}

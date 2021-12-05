package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;

public class Elf64Offset extends ElfOffset<Long> {
    public static final Elf64Offset ZERO = new Elf64Offset(0);

    public Elf64Offset(long offset) {
        super(offset);
    }

    public Elf64Offset(ElfOffset<Long> offset) {
        super(offset.value());
    }

    @Override
    protected ElfOffset<Long> mkFileOffset(Long value) {
        return new Elf64Offset(value);
    }

    public long longValue() { return value(); }

    @Override
    public String toString() {
        return String.format("0x%016x", longValue());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;

import java.util.Objects;

public class Elf64Offset extends ElfOffset<Long> {
    public static final Elf64Offset ZERO = new Elf64Offset(0);

    public Elf64Offset(long offset) {
        super(offset);
    }

    @Override
    protected ElfOffset<Long> mkFileOffset(long value) {
        return new Elf64Offset(value);
    }

    public long longValue() { return value(); }

    @Override
    public String toString() {
        return String.format("0x%016x", longValue());
    }
}

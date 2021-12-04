package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf32_Off")
public class Elf32Offset extends ElfOffset<Integer> {
    public static final Elf32Offset ZERO = new Elf32Offset(0);

    public Elf32Offset(int offset) {
        super(offset);
    }

    @Override
    protected ElfOffset<Integer> mkFileOffset(Integer value) {
        return new Elf32Offset(Math.toIntExact(value));
    }

    public int intValue() { return value(); }

    @Override
    public Elf32Offset plus(long bytesCount) {
        return (Elf32Offset) super.plus(bytesCount);
    }

    @Override
    public String toString() {
        return String.format("0x%08x", intValue());
    }
}

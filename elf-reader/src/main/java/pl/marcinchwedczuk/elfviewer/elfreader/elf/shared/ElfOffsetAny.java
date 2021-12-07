package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import java.util.Objects;

public class ElfOffsetAny<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfOffset<NATIVE_WORD> {

    public ElfOffsetAny(NATIVE_WORD fileOffset) {
        super(fileOffset);
    }

    @Override
    protected ElfOffset<NATIVE_WORD> mkFileOffset(NATIVE_WORD value) {
        return new ElfOffsetAny<>(value);
    }

    @Override
    public String toString() {
        return Objects.toString(value());
    }
}

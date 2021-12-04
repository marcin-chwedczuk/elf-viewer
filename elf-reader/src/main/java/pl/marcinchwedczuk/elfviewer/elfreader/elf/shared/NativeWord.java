package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public abstract class NativeWord<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    // TODO: toHexString(...)
    public abstract ElfOffset<NATIVE_WORD> zeroOffset();

    public abstract NATIVE_WORD readFrom(StructuredFile<NATIVE_WORD> sf);
}

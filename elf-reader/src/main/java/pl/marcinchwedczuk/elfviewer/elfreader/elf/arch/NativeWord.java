package pl.marcinchwedczuk.elfviewer.elfreader.elf.arch;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public abstract class NativeWord<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    public abstract NativeWordType type();

    public abstract ElfOffset<NATIVE_WORD> zeroOffset();
    public abstract ElfAddress<NATIVE_WORD> zeroAddress();

    public abstract NATIVE_WORD readNativeWordFrom(StructuredFile<NATIVE_WORD> sf);

    public abstract String toHexString(NATIVE_WORD value);
    public abstract String toDecString(NATIVE_WORD value);

    public abstract ElfRelocation<NATIVE_WORD> mkRelocation(ElfAddress<NATIVE_WORD> offset, NATIVE_WORD info);
    public abstract ElfRelocationAddend<NATIVE_WORD> mkRelocationA(ElfAddress<NATIVE_WORD> offset,
                                                                   NATIVE_WORD info,
                                                                   NATIVE_WORD addend);

    public abstract int size();
    public abstract boolean hasBitsSet(NATIVE_WORD w, long value);

    public abstract NATIVE_WORD[] readArray(StructuredFile<NATIVE_WORD> sf, int nelements);
}

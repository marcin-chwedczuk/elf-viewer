package pl.marcinchwedczuk.elfviewer.elfreader.elf.arch;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
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
}

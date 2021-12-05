package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public class LongNativeWord extends NativeWord<Long> {
    @Override
    public ElfOffset<Long> zeroOffset() {
        return Elf64Offset.ZERO;
    }

    @Override
    public Long readFrom(StructuredFile<Long> sf) {
        return sf.readUnsignedLong();
    }
}

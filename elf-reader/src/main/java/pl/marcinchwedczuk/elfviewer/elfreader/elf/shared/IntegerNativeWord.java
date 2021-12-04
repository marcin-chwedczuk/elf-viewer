package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public class IntegerNativeWord extends NativeWord<Integer> {
    @Override
    public ElfOffset<Integer> zeroOffset() {
        return Elf32Offset.ZERO;
    }

    @Override
    public Integer readFrom(StructuredFile<Integer> sf) {
        return sf.readUnsignedInt();
    }
}

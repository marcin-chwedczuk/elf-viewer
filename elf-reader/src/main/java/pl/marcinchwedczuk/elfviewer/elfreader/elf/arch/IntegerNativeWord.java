package pl.marcinchwedczuk.elfviewer.elfreader.elf.arch;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

public class IntegerNativeWord extends NativeWord<Integer> {
    @Override
    public NativeWordType type() {
        return NativeWordType.INT_32_BITS;
    }

    @Override
    public ElfOffset<Integer> zeroOffset() {
        return Elf32Offset.ZERO;
    }

    @Override
    public ElfAddress<Integer> zeroAddress() {
        return new Elf32Address(0);
    }

    @Override
    public Integer readNativeWordFrom(StructuredFile<Integer> sf) {
        return sf.readUnsignedInt();
    }

    @Override
    public String toHexString(Integer value) {
        return String.format("0x%08x", value);
    }

    @Override
    public ElfRelocation<Integer> mkRelocation(ElfAddress<Integer> offset, Integer info) {
        return new ElfRelocation.ElfRelocation32(offset, info);
    }
}

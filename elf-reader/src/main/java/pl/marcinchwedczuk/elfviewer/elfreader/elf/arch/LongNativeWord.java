package pl.marcinchwedczuk.elfviewer.elfreader.elf.arch;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.stream.LongStream;

public class LongNativeWord extends NativeWord<Long> {
    @Override
    public NativeWordType type() {
        return NativeWordType.NATIVE_64;
    }

    @Override
    public ElfOffset<Long> zeroOffset() {
        return new ElfOffset<>(0L);
    }

    @Override
    public ElfAddress<Long> zeroAddress() {
        return new ElfAddress<>(0L);
    }

    @Override
    public Long readNativeWordFrom(StructuredFile<Long> sf) {
        return sf.readUnsignedLong();
    }

    @Override
    public String toHexString(Long value) {
        return String.format("0x%016x", value);
    }

    @Override
    public String toDecString(Long value) {
        return String.format("%d", value);
    }

    @Override
    public ElfRelocation<Long> mkRelocation(ElfAddress<Long> offset, Long info) {
        return new ElfRelocation.ElfRelocation64(offset, info);
    }

    @Override
    public ElfRelocationAddend<Long> mkRelocationA(ElfAddress<Long> offset,
                                                   Long info,
                                                   Long addend) {
        return new ElfRelocationAddend.ElfRelocationAddend64(offset, info, addend);
    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    public boolean hasBitsSet(Long w, long value) {
        return (((long)w) & value) == value;
    }

    @Override
    public Long[] readArray(StructuredFile<Long> sf, int nelements) {
        long[] tmp = sf.readLongArray(nelements);
        // TODO: not optimal
        return LongStream.of(tmp).boxed().toArray(Long[]::new);
    }

    @Override
    public Long divideExact(Long a, Long b) {
        long result = a / b;
        if ((result * b) != a)
            throw new IllegalArgumentException("Number " + a + " is not exactly divisible by " + b + ".");
        return result;
    }
}

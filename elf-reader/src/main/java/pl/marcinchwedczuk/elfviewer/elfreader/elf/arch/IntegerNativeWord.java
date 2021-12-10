package pl.marcinchwedczuk.elfviewer.elfreader.elf.arch;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.stream.IntStream;

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
    public String toDecString(Integer value) {
        return String.format("%d", value);
    }

    @Override
    public ElfRelocation<Integer> mkRelocation(ElfAddress<Integer> offset, Integer info) {
        return new ElfRelocation.ElfRelocation32(offset, info);
    }

    @Override
    public ElfRelocationAddend<Integer> mkRelocationA(ElfAddress<Integer> offset,
                                                      Integer info,
                                                      Integer addend) {
        return new ElfRelocationAddend.ElfRelocationAddend32(offset, info, addend);
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public boolean hasBitsSet(Integer w, long value) {
        int valueInt = Math.toIntExact(value);
        return (((int)w) & valueInt) == valueInt;
    }

    @Override
    public Integer[] readArray(StructuredFile<Integer> sf, int nelements) {
        int[] tmp = sf.readIntArray(nelements);
        return IntStream.of(tmp).boxed().toArray(Integer[]::new);
    }

    @Override
    public Integer divideExact(Integer a, Integer b) {
        int result = a / b;
        if ((result * b) != a)
            throw new IllegalArgumentException("Number " + a + " is not exactly divisible by " + b + ".");
        return result;
    }
}

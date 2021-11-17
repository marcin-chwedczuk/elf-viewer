package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Flag;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntFlags;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Mask;

import java.util.Collection;
import java.util.List;

public class Elf32SegmentFlags extends IntFlags<Elf32SegmentFlags> {
    @ElfApi("PF_X")
    public static Flag<Elf32SegmentFlags> Executable = flag("X", 1 << 0);

    @ElfApi("PF_W")
    public static Flag<Elf32SegmentFlags> Writable = flag("W", 1 << 1);

    @ElfApi("PF_R")
    public static Flag<Elf32SegmentFlags> Readable = flag("R", 1 << 2);

    @ElfApi("PF_MASKOS")
    public static Mask<Elf32SegmentFlags> OsSpecificMask = mask(0x0ff00000);

    @ElfApi("PF_MASKPROC")
    public static Mask<Elf32SegmentFlags> ProcessorSpecificMask = mask(0xf0000000);

    public Elf32SegmentFlags(int init) {
        super(init);
    }

    public Elf32SegmentFlags(Flag<Elf32SegmentFlags>... flags) {
        super(flags);
    }

    @Override
    protected Elf32SegmentFlags mkCopy(int newRaw) {
        return new Elf32SegmentFlags(newRaw);
    }

    @Override
    public Collection<Flag<Elf32SegmentFlags>> flags() {
        return List.of(Readable, Writable, Executable);
    }
}

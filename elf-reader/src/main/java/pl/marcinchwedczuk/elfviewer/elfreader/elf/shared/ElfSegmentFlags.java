package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Flag;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BitFlags;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Mask;

import java.util.Collection;
import java.util.List;

public class ElfSegmentFlags extends BitFlags<ElfSegmentFlags> {
    @ElfApi("PF_X")
    public static final Flag<ElfSegmentFlags> Executable = flag("X", 1 << 0);

    @ElfApi("PF_W")
    public static final Flag<ElfSegmentFlags> Writable = flag("W", 1 << 1);

    @ElfApi("PF_R")
    public static final Flag<ElfSegmentFlags> Readable = flag("R", 1 << 2);

    @ElfApi("PF_MASKOS")
    public static final Mask<ElfSegmentFlags> OsSpecificMask = mask(0x0ff00000);

    @ElfApi("PF_MASKPROC")
    public static final Mask<ElfSegmentFlags> ProcessorSpecificMask = mask(0xf0000000);

    public ElfSegmentFlags(int init) {
        super(init);
    }

    @SafeVarargs
    public ElfSegmentFlags(Flag<ElfSegmentFlags>... flags) {
        super(flags);
    }

    @Override
    protected ElfSegmentFlags mkCopy(long newRaw) {
        return new ElfSegmentFlags(Math.toIntExact(newRaw));
    }

    @Override
    public Collection<Flag<ElfSegmentFlags>> flags() {
        return List.of(Readable, Writable, Executable);
    }
}

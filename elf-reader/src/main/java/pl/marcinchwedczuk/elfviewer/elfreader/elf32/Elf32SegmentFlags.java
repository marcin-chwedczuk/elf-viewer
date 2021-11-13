package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

public class Elf32SegmentFlags {
    @ElfApi("PF_X")
    public static Elf32SegmentFlags Executable = new Elf32SegmentFlags(1 << 0);

    @ElfApi("PF_W")
    public static Elf32SegmentFlags Writable = new Elf32SegmentFlags(1 << 1);

    @ElfApi("PF_R")
    public static Elf32SegmentFlags Readable = new Elf32SegmentFlags(1 << 2);

    public static Elf32SegmentFlags MaskOsSpecific = new Elf32SegmentFlags(0x0ff00000);
    @ElfApi("PF_MASKPROC")
    public static Elf32SegmentFlags MaskProcessorSpecific = new Elf32SegmentFlags(0xf0000000);

    public static Elf32SegmentFlags of(Elf32SegmentFlags... flags) {
        int combined = 0;

        for (Elf32SegmentFlags flag : flags) {
            combined |= flag.flags;
        }

        return new Elf32SegmentFlags(combined);
    }

    private final int flags;

    public Elf32SegmentFlags(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(Elf32SegmentFlags flag) {
        return ((this.flags & flag.flags) == flag.flags);
    }

    public Elf32SegmentFlags clearFlag(Elf32SegmentFlags flag) {
        return new Elf32SegmentFlags(
                this.flags & ~flag.flags
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32SegmentFlags that = (Elf32SegmentFlags) o;
        return flags == that.flags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Elf32SegmentFlags curr = this;

        if (curr.hasFlag(Readable)) {
            curr = curr.clearFlag(Readable);
            sb.append("R");
        } else {
            sb.append("-");
        }

        if (curr.hasFlag(Writable)) {
            curr = curr.clearFlag(Writable);
            sb.append("W");
        } else {
            sb.append("-");
        }

        if (curr.hasFlag(Executable)) {
            curr = curr.clearFlag(Executable);
            sb.append("X");
        } else {
            sb.append("-");
        }

        if (curr.flags != 0) {
            sb.append("|").append(Integer.toHexString(curr.flags));
        }

        return sb.toString();
    }
}

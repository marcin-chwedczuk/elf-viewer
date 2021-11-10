package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import java.util.Objects;

public class SectionAttributes {
    public static SectionAttributes of(SectionAttributeFlags... flags) {
        int rawFlags = 0;

        for (SectionAttributeFlags flag : flags) {
            rawFlags |= flag.intValue();
        }

        return new SectionAttributes(rawFlags);
    }

    private final int flags;

    public SectionAttributes(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(SectionAttributeFlags flag) {
        return (flags & flag.intValue()) == flag.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionAttributes that = (SectionAttributes) o;
        return flags == that.flags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flags);
    }

    @Override
    public String toString() {
        StringBuilder flags = new StringBuilder();
        flags.append("[");

        for (SectionAttributeFlags flag : SectionAttributeFlags.values()) {
            if (hasFlag(flag)) {
                flags.append(flag).append("|");
            }
        }

        if (flags.charAt(flags.length()-1) == '|') {
            flags.deleteCharAt(flags.length()-1);
        }

        flags.append("]");
        return flags.toString();
    }
}

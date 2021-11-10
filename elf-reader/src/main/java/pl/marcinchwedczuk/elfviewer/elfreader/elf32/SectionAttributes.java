package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

public class SectionAttributes {
    private final int flags;

    public SectionAttributes(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(SectionAttributeFlags flag) {
        return (flags & flag.intValue()) == flag.intValue();
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

package pl.marcinchwedczuk.elfviewer.elfreader.elf;

/**
 * The value 1 signifies the original file format;
 * extensions will create new versions with higher numbers.
 * The value of EV_CURRENT, though given as 1 above,
 * will change as necessary to reflect the current version number.
 */
public enum ElfVersion {
    /**
     * Invalid version
     */
    EV_NONE("EV_NONE", 0),

    /**
     * Current version
     */
    EV_CURRENT("EV_CURRENT", 1);

    public static ElfVersion fromByte(byte b) {
        for (ElfVersion value : ElfVersion.values()) {
            if (value.value == b) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized byte " + b);
    }

    private String name;
    private int value;

    ElfVersion(String name, int value) {
        this.name = name;
        this.value = value;
    }
}

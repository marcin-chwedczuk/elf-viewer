package pl.marcinchwedczuk.elfviewer.elfreader.elf;

public enum ElfData {
    /**
     * Invalid data encoding
     */
    ELF_DATA_NONE("ELFDATANONE", 0),

    /**
     * Encoding ELFDATA2LSB specifies 2's complement values,
     * with the least significant byte occupying the lowest address.
     */
    ELF_DATA_LSB("ELFDATA2LSB", 1),

    /**
     * Encoding ELFDATA2MSB specifies 2's complement values,
     * with the most significant byte occupying the lowest address.
     */
    ELF_DATA_MSB("ELFDATA2MSB", 2);

    public static ElfData fromByte(byte b) {
        for (ElfData value : ElfData.values()) {
            if (value.value == b) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized byte " + b);
    }

    private String name;
    private int value;

    ElfData(String name, int value) {
        this.name = name;
        this.value = value;
    }
}

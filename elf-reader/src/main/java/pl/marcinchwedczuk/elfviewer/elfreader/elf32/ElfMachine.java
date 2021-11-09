package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum ElfMachine {
    @ElfApi("ET_NONE")
    None(0),

    @ElfApi("EM_M32")
    M32(1),

    @ElfApi("EM_SPARC")
    Sparc(2),

    @ElfApi("EM_386")
    Intel386(3),

    @ElfApi("EM_68K")
    Motorola68k(4),

    @ElfApi("EM_88K")
    Motorola88k(5),

    @ElfApi("EM_860")
    Intel80860(7),

    @ElfApi("EM_MIPS")
    MipsRs3k(8),

    @ElfApi("EM_MIPS_RS4_BE")
    MipsRs4k(10),

    @ElfApi("RESERVED")
    Reserved11(11),

    @ElfApi("RESERVED")
    Reserved12(12),

    @ElfApi("RESERVED")
    Reserved13(13),

    @ElfApi("RESERVED")
    Reserved14(14),

    @ElfApi("RESERVED")
    Reserved15(15),

    @ElfApi("RESERVED")
    Reserved16(16);

    public static ElfMachine fromUnsignedShort(short v) {
        for (ElfMachine value : ElfMachine.values()) {
            if (value.value == v) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized ushort" + v);
    }

    private short value;

    ElfMachine(int value) {
        this.value = (short)value;
    }
}

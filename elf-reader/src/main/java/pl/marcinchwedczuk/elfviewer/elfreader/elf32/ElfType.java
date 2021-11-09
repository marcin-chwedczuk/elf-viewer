package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum ElfType {
    @ElfApi("ET_NONE")
    None(0),

    @ElfApi("ET_REL")
    Relocatable(1),

    @ElfApi("ET_EXEC")
    Executable(2),

    @ElfApi("ET_DYN")
    SharedObject(3),

    @ElfApi("ET_CORE")
    Core(4),

    @ElfApi("ET_LOPROC")
    LoProc(0xff00),

    @ElfApi("ET_HIPROC")
    HiProc(0xffff);

    public static ElfType fromUnsignedShort(short v) {
        for (ElfType value : ElfType.values()) {
            if (value.value == v) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unrecognized ushort" + v);
    }

    private short value;

    ElfType(int value) {
        this.value = (short)value;
    }
}

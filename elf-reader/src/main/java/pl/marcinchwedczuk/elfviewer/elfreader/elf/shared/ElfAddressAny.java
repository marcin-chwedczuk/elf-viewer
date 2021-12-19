package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

// @ElfApi("Elf32_Addr")
public class ElfAddressAny<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfAddress<NATIVE_WORD> {
    public ElfAddressAny(NATIVE_WORD address) {
        super(address);
    }

    @Override
    protected ElfAddress<NATIVE_WORD> mkAddress(NATIVE_WORD value) {
        return new ElfAddressAny<NATIVE_WORD>(value);
    }

    @Override
    public String toString() {
        return "not impl. TODO";
    }

}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

// TODO: @ElfApi("Elf32_Dyn")
public class ElfDynamicTag<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >  {
    @ElfApi("d_tag")
    private final ElfDynamicTagType type;

    @ElfApi("d_val")
    // @ElfApi("d_ptr")
    private final NATIVE_WORD value;

    public ElfDynamicTag(ElfDynamicTagType type,
                         NATIVE_WORD value) {
        this.type = requireNonNull(type);
        this.value = value;
    }

    public ElfDynamicTagType type() { return type; }
    public NATIVE_WORD value() { return value; }
    public ElfAddress<NATIVE_WORD> address() { return new ElfAddress<>(value); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElfDynamicTag<?> that = (ElfDynamicTag<?>) o;
        return type == that.type && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return String.format("%20s %08d",
                type, value);
    }
}

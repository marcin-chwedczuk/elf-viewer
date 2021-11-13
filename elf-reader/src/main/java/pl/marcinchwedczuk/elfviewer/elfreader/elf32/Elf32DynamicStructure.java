package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ElfApi("Elf32_Dyn")
public class Elf32DynamicStructure {
    @ElfApi("d_tag")
    private final Elf32DynamicArrayTag tag;

    @ElfApi("d_val")
    private final Integer value;

    @ElfApi("d_ptr")
    private final Elf32Address ptr;

    public Elf32DynamicStructure(Elf32DynamicArrayTag tag,
                                 Integer value,
                                 Elf32Address ptr) {
        this.tag = requireNonNull(tag);
        this.value = value;
        this.ptr = ptr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32DynamicStructure that = (Elf32DynamicStructure) o;
        return tag == that.tag && Objects.equals(value, that.value) && Objects.equals(ptr, that.ptr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, value, ptr);
    }

    @Override
    public String toString() {
        return String.format("%20s %08d %s",
                tag, value, ptr);
    }
}

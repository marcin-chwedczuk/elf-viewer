package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ElfApi("Elf32_Dyn")
public class Elf32DynamicTag {
    @ElfApi("d_tag")
    private final Elf32DynamicTagType type;

    @ElfApi("d_val")
    // @ElfApi("d_ptr")
    private final Integer value;

    public Elf32DynamicTag(Elf32DynamicTagType type,
                           Integer value) {
        this.type = requireNonNull(type);
        this.value = value;
    }

    public Elf32DynamicTagType type() { return type; }
    public Integer value() { return value; }
    public Elf32Address address() { return new Elf32Address(value); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32DynamicTag that = (Elf32DynamicTag) o;
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

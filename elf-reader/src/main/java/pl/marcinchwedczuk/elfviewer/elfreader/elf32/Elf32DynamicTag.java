package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ElfApi("Elf32_Dyn")
public class Elf32DynamicTag {
    private ElfDynamicTag<Integer> dynamicTag;

    public ElfDynamicTag<Integer> unwrap() { return dynamicTag; }

    public Elf32DynamicTag(ElfDynamicTag<Integer> dynamicTag) {
        this.dynamicTag = dynamicTag;
    }

    public Elf32DynamicTagType type() { return dynamicTag.type(); }
    public Integer value() { return dynamicTag.value(); }
    public Elf32Address address() { return new Elf32Address(value()); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf32DynamicTag that = (Elf32DynamicTag) o;
        return dynamicTag.equals(that.dynamicTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dynamicTag);
    }

    @Override
    public String toString() {
        return dynamicTag.toString();
    }
}

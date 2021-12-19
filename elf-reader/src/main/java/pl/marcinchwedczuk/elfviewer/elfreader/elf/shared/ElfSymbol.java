package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class ElfSymbol<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("st_name")
    private final StringTableIndex nameIndex;

    private final String name;

    @ElfApi("st_value")
    private final ElfAddress<NATIVE_WORD> value;

    @ElfApi("st_size")
    private final NATIVE_WORD size;

    @ElfApi("st_info")
    private final byte info;

    @ElfApi("st_other")
    private final byte other;

    @ElfApi("st_shndx")
    private final SectionHeaderIndex index;

    public ElfSymbol(StringTableIndex nameIndex,
                     String name,
                     ElfAddress<NATIVE_WORD> value,
                     NATIVE_WORD size,
                     byte info,
                     byte other,
                     SectionHeaderIndex index) {
        this.nameIndex = nameIndex;
        this.name = name;
        this.value = value;
        this.size = size;
        this.info = info;
        this.other = other;
        this.index = index;
    }

    /**
     * This member holds an index into the object file's symbol
     * string table, which holds character representations of the
     * symbol names.  If the value is nonzero, it represents a
     * string table index that gives the symbol name.  Otherwise,
     * the symbol has no name.
     */
    public StringTableIndex nameIndex() {
        return nameIndex;
    }

    /**
     * @return Symbol name or {@code NULL}.
     */
    public String name() {
        return name;
    }

    /**
     * This member gives the value of the associated symbol.
     */
    public ElfAddress<NATIVE_WORD> value() {
        return value;
    }

    /**
     * Many symbols have associated sizes.  This member holds
     * zero if the symbol has no size or an unknown size.
     */
    public NATIVE_WORD size() {
        return size;
    }

    /**
     * This member specifies the symbol's type and binding
     * attributes.
     */
    public byte info() {
        return info;
    }

    public ElfSymbolBinding binding() {
        return ElfSymbolBinding.fromSymbolInfo(info);
    }

    public ElfSymbolType symbolType() {
        return ElfSymbolType.fromSymbolInfo(info);
    }

    public byte other() {
        return other;
    }

    public ElfSymbolVisibility visibility() {
        return ElfSymbolVisibility.fromSymbolOther(other);
    }

    /**
     * Every symbol table entry is "defined" in relation to some
     * section.  This member holds the relevant section header
     * table index.
     */
    public SectionHeaderIndex index() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("%4s %20s %s %4d %12s %12s %8d %s",
                nameIndex, name, value, size, binding(), symbolType(), Short.toUnsignedInt(other), index);
    }
}

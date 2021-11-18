package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32Symbol {
    @ElfApi("st_name")
    private final StringTableIndex nameIndex;

    private final String name;

    @ElfApi("st_value")
    private final Elf32Address value;

    @ElfApi("st_size")
    private final int size;

    @ElfApi("st_info")
    private final byte info;

    @ElfApi("st_other")
    private final byte other;

    @ElfApi("st_shndx")
    private final SectionHeaderIndex index;

    public Elf32Symbol(StringTableIndex nameIndex,
                       String name,
                       Elf32Address value,
                       int size,
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
    public Elf32Address value() {
        return value;
    }

    /**
     * Many symbols have associated sizes.  This member holds
     * zero if the symbol has no size or an unknown size.
     */
    public int size() {
        return size;
    }

    /**
     * This member specifies the symbol's type and binding
     * attributes.
     */
    public byte info() {
        return info;
    }

    public Elf32SymbolBinding binding() {
        return Elf32SymbolBinding.fromSymbolInfo(info);
    }

    public Elf32SymbolType symbolType() {
        return Elf32SymbolType.fromSymbolInfo(info);
    }

    public byte other() {
        return other;
    }

    public Elf32SymbolVisibility visibility() {
        return Elf32SymbolVisibility.fromSymbolOther(other);
    }

    public SectionHeaderIndex index() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("%4s %20s %s %4d %12s %12s %8d %s",
                nameIndex, name, value, size, binding(), symbolType(), Short.toUnsignedInt(other), index);
    }
}

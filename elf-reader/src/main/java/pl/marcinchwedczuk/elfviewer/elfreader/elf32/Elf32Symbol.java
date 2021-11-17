package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32Symbol {
    /**
     * This member holds an index into the object file's symbol string table,
     * which holds the character representations of the symbol names.
     */
    @ElfApi("st_name")
    private final StringTableIndex nameIndex;

    private final String name;

    // TODO: Define Union<T0, T1, T2> type for keeping unions

    /**
     * This member gives the value of the associated symbol.
     * Depending on the context, this may be an absolute value,
     * an address, and so on; details appear below.
     */
    @ElfApi("st_value")
    private final Elf32Address value;

    /**
     * Many symbols have associated sizes.
     * For example, a data object's size is the number of bytes contained in the object.
     * This member holds 0 if the symbol has no size or an unknown size.
     */
    @ElfApi("st_size")
    private final int size;

    /**
     * This member specifies the symbol's type and binding attributes.
     * A list of the values and meanings appears below.
     * The following code shows how to manipulate the values.
     */
    @ElfApi("st_info")
    private final byte info;

    /**
     * This member currently holds 0 and has no defined meaning.
     */
    @ElfApi("st_other")
    private final byte other;

    /**
     * The symbols in ELF object files convey specific information to the linker and loader.
     * See the operating system sections for a description of the actual linking model used in the system.
     * SHN_ABS      - The symbol has an absolute value that will not change because of relocation.
     * SHN_COMMON   -  The symbol labels a common block that has not yet been allocated.
     *  The symbol's value gives alignment constraints, similar to a section's sh_addralign member.
     *  That is, the link editor will allocate the storage for the symbol at an address that is
     *  a multiple of st_value. The symbol's size tells how many bytes are required.
     * SHN_UNDEF    - This section table index means the symbol is undefined.
     *  When the link editor combines this object file with another that defines the indicated symbol,
     *  this file's references to the symbol will be linked to the actual definition.
     */
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

    public StringTableIndex nameIndex() { return nameIndex; }
    public String name() { return name; }

    public Elf32Address value() { return value; }
    public int size() { return size; }

    public byte info() { return info; }
    public Elf32SymbolBinding binding() {
        return Elf32SymbolBinding.fromByte((byte)(info >>> 4));
    }
    public Elf32SymbolType symbolType() {
        return Elf32SymbolType.fromSymbolInfo(info);
    }

    public byte other() { return other; }
    public SectionHeaderIndex index() { return index; }

    @Override
    public String toString() {
        return String.format("%4s %20s %s %4d %12s %12s %8d %s",
                nameIndex, name, value, size, binding(), symbolType(), Short.toUnsignedInt(other), index);
    }
}

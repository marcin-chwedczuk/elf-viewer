package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import static java.util.Objects.requireNonNull;

// TODO: @ElfApi32 @ElfApi64
@ElfApi("Elf32_Vernaux")
public class ElfVersionNeededAuxiliary<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("vna_hash")
    private final int hash;

    @ElfApi("vna_flags")
    private final short flags;

    @ElfApi("vna_other")
    private final ElfSymbolVersion other;

    @ElfApi("vna_name")
    private final StringTableIndex nameIndex;
    private final String name;

    @ElfApi("vna_next")
    private final int offsetNext;

    public ElfVersionNeededAuxiliary(int hash,
                                     short flags,
                                     ElfSymbolVersion other,
                                     StringTableIndex nameIndex,
                                     String name,
                                     int offsetNext) {
        this.hash = hash;
        this.flags = flags;
        this.other = other;
        this.nameIndex = nameIndex;
        this.name = requireNonNull(name);
        this.offsetNext = offsetNext;
    }

    /**
     * The hash value of the version dependency name.
     * This value is generated using the same hashing function that
     * is described in Hash Table Section.
     */
    public int hash() {
        return hash;
    }

    public short flags() {
        return flags;
    }

    /**
     * Object file version identifier used in the .gnu.version symbol version array.
     * Bit number 15 controls whether or not the object is hidden;
     * if this bit is set, the object cannot be used and the static
     * linker will ignore the symbol's presence in the object.
     */
    // TODO: Handle hidden bit 15
    public ElfSymbolVersion other() {
        return other;
    }

    /**
     * The string table offset to a null-terminated string, giving the name of the version dependency.
     */
    public StringTableIndex nameIndex() {
        return nameIndex;
    }

    public String name() {
        return name;
    }

    /**
     * The byte offset from the start of this Elf32_Vernaux entry to the next Elf32_Vernaux entry.
     */
    public int offsetNext() {
        return offsetNext;
    }
}

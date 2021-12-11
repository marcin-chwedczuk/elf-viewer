package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.List;

public class ElfVersionDefinition<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("vd_version")
    private final ElfVersionDefinitionRevision version;

    @ElfApi("vd_flags")
    private final short flags;

    @ElfApi("vd_ndx")
    private final ElfSymbolVersion versionIndex;

    @ElfApi("vd_cnt")
    private final short numberOfAuxiliaryEntries;

    @ElfApi("vd_hash")
    private final int nameHash;

    @ElfApi("vd_aux")
    private final int offsetAuxiliary;

    @ElfApi("vd_next")
    private final int offsetNext;

    private final List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> auxiliaryEntries;

    public ElfVersionDefinition(ElfVersionDefinitionRevision version,
                                short flags,
                                ElfSymbolVersion versionIndex,
                                short numberOfAuxiliaryEntries,
                                int nameHash,
                                int offsetAuxiliary,
                                int offsetNext,
                                List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> auxiliaryEntries) {
        this.version = version;
        this.flags = flags;
        this.versionIndex = versionIndex;
        this.numberOfAuxiliaryEntries = numberOfAuxiliaryEntries;
        this.nameHash = nameHash;
        this.offsetAuxiliary = offsetAuxiliary;
        this.offsetNext = offsetNext;
        this.auxiliaryEntries = List.copyOf(auxiliaryEntries);
    }

    /**
     * Version revision. This field shall be set to 1.
     */
    public ElfVersionDefinitionRevision version() {
        return version;
    }

    /**
     * Version information flag bitmask.
     */
    public short flags() {
        return flags;
    }

    /**
     * Version index numeric value referencing the SHT_GNU_versym section.
     */
    public ElfSymbolVersion versionIndex() {
        return versionIndex;
    }

    /**
     * Number of associated verdaux array entries.
     */
    public short numberOfAuxiliaryEntries() {
        return numberOfAuxiliaryEntries;
    }

    /**
     * Version name hash value (ELF hash function).
     */
    public int nameHash() {
        return nameHash;
    }

    /**
     * Offset in bytes to a corresponding entry in an array of Elfxx_Verdaux structures.
     */
    public int offsetAuxiliary() {
        return offsetAuxiliary;
    }

    /**
     * Offset to the next verdef entry, in bytes.
     */
    public int offsetNext() {
        return offsetNext;
    }

    public List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> auxiliaryEntries() {
        return auxiliaryEntries;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

// TODO: @ElfApi32 @ElfApi64
// Elfxx_Verdaux
public class ElfVersionDefinitionAuxiliary<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    @ElfApi("vda_name")
    private final StringTableIndex nameIndex;
    private final String name;

    @ElfApi("vda_next")
    private final int offsetNext;

    public ElfVersionDefinitionAuxiliary(StringTableIndex nameIndex,
                                         String name,
                                         int offsetNext) {
        this.nameIndex = nameIndex;
        this.name = name;
        this.offsetNext = offsetNext;
    }

    public StringTableIndex nameIndex() {
        return nameIndex;
    }

    public String name() {
        return name;
    }

    public int offsetNext() {
        return offsetNext;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ElfVersionNeededEntry<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("vn_version")
    private final ElfVersionNeeded version;

    @ElfApi("vn_cnt")
    private final short numberOfAuxiliaryEntries;

    @ElfApi("vn_file")
    private final StringTableIndex fileNameIndex;
    private final String fileName;

    @ElfApi("vn_aux")
    private final int offsetAuxiliaryEntries;

    @ElfApi("vn_next")
    private final int offsetNextEntry;

    private final List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> auxiliaryEntries;

    public ElfVersionNeededEntry(ElfVersionNeeded version,
                                 short numberOfAuxiliaryEntries,
                                 StringTableIndex fileNameIndex,
                                 String fileName,
                                 int offsetAuxiliaryEntries,
                                 int offsetNextEntry,
                                 List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> auxiliaryEntries) {
        this.version = version;
        this.numberOfAuxiliaryEntries = numberOfAuxiliaryEntries;
        this.fileNameIndex = fileNameIndex;
        this.fileName = Objects.requireNonNull(fileName);
        this.offsetAuxiliaryEntries = offsetAuxiliaryEntries;
        this.offsetNextEntry = offsetNextEntry;
        this.auxiliaryEntries = List.copyOf(auxiliaryEntries);
    }

    /**
     * This member identifies the version of the structure
     */
    public ElfVersionNeeded version() {
        return version;
    }

    /**
     * The number of elements in the Elf32_Vernaux array.
     */
    public short numberOfAuxiliaryEntries() {
        return numberOfAuxiliaryEntries;
    }

    /**
     * The string table offset to a null-terminated string, providing the file name of a version dependency.
     * This name matches one of the .dynamic dependencies found in the file.
     */
    public StringTableIndex fileNameIndex() {
        return fileNameIndex;
    }
    public String fileName() { return fileName; }

    /**
     * The byte offset, from the start of this Elf32_Verneed entry, to the Elf32_Vernaux array of
     * version definitions that are required from the associated file dependency.
     * At least one version dependency must exist. Additional version dependencies can
     * be present, the number being indicated by the vn_cnt value.
     */
    public int offsetAuxiliaryEntries() {
        return offsetAuxiliaryEntries;
    }

    /**
     * The byte offset, from the start of this Elf32_Verneed entry, to the next Elf32_Verneed entry.
     */
    public int offsetNextEntry() {
        return offsetNextEntry;
    }

    public List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> auxiliaryEntries() {
        return auxiliaryEntries;
    }
}

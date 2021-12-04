package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ElfSectionHeader<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    @ElfApi("sh_name")
    private final StringTableIndex nameIndex;

    private final String name;

    @ElfApi("sh_type")
    private final ElfSectionType type;

    @ElfApi("sh_flags")
    // TODO: Move constants to interface, split into INtFlags and LongFlags
    private final SectionAttributes flags;

    @ElfApi("sh_addr")
    private final ElfAddress<NATIVE_WORD> inMemoryAddress;

    @ElfApi("sh_offset")
    private final ElfOffset<NATIVE_WORD> fileOffset;

    @ElfApi("sh_size")
    private final NATIVE_WORD sectionSize;

    @ElfApi("sh_link")
    private final int link;

    @ElfApi("sh_info")
    private final int info;

    @ElfApi("sh_addralign")
    private final NATIVE_WORD addressAlignment;

    @ElfApi("sh_entsize")
    private final NATIVE_WORD containedEntrySize;

    public ElfSectionHeader(StringTableIndex nameIndex,
                            String name,
                            ElfSectionType type,
                            SectionAttributes flags,
                            ElfAddress<NATIVE_WORD> inMemoryAddress,
                            ElfOffset<NATIVE_WORD> fileOffset,
                            NATIVE_WORD sectionSize,
                            int link,
                            int info,
                            NATIVE_WORD addressAlignment,
                            NATIVE_WORD containedEntrySize) {
        this.nameIndex = nameIndex;
        this.name = requireNonNull(name);
        this.type = requireNonNull(type);
        this.flags = requireNonNull(flags);
        this.inMemoryAddress = requireNonNull(inMemoryAddress);
        this.fileOffset = requireNonNull(fileOffset);
        this.sectionSize = sectionSize;
        this.link = link;
        this.info = info;
        this.addressAlignment = addressAlignment;
        this.containedEntrySize = containedEntrySize;
    }

    /**
     * @returns The name of the section.  Its value
     * is an index into the section header string table section,
     * giving the location of a null-terminated string.
     */
    public StringTableIndex nameIndex() {
        return nameIndex;
    }

    public String name() {
        return name;
    }

    public boolean hasName(String name) {
        return Objects.equals(this.name(), name);
    }

    public ElfSectionType type() {
        return type;
    }

    public SectionAttributes flags() {
        return flags;
    }

    /**
     * @return If this section appears in the memory image of a process,
     * this member holds the address at which the section's first
     * byte should reside.  Otherwise, the member contains zero.
     */
    public ElfAddress<NATIVE_WORD> virtualAddress() {
        return inMemoryAddress;
    }

    /**
     * @return This member's value holds the byte offset from the
     * beginning of the file to the first byte in the section.
     * One section type, SHT_NOBITS, occupies no space in the
     * file, and its sh_offset member locates the conceptual
     * placement in the file.
     */
    public ElfOffset<NATIVE_WORD> fileOffset() {
        return fileOffset;
    }

    /**
     * @return This member holds the section's size in bytes. Unless the
     * section type is SHT_NOBITS, the section occupies sh_size
     * bytes in the file.  A section of type SHT_NOBITS may have
     * a nonzero size, but it occupies no space in the file.
     */
    public NATIVE_WORD size() {
        return sectionSize;
    }

    /**
     * @return This member holds a section header table index link, whose
     * interpretation depends on the section type.
     */
    public int link() {
        return link;
    }

    /**
     * @return This member holds extra information, whose interpretation
     * depends on the section type.
     */
    public int info() {
        return info;
    }

    /**
     * Some sections have address alignment constraints.
     * If a section holds a doubleword, the system must ensure
     * doubleword alignment for the entire section.  That is, the
     * value of sh_addr must be congruent to zero, modulo the
     * value of sh_addralign.  Only zero and positive integral
     * powers of two are allowed.  The value 0 or 1 means that
     * the section has no alignment constraints.
     */
    public NATIVE_WORD addressAlignment() {
        return addressAlignment;
    }

    /**
     * Some sections hold a table of fixed-sized entries, such as
     * a symbol table.  For such a section, this member gives the
     * size in bytes for each entry.  This member contains zero
     * if the section does not hold a table of fixed-size
     * entries.
     */
    public NATIVE_WORD containedEntrySize() {
        return containedEntrySize;
    }

    /**
     * @return Offset of the first byte in ELF file located after this section end.
     */
    public ElfOffset<NATIVE_WORD> sectionEndOffsetInFile() {
        // TODO: Consider alignment
        return fileOffset.plus(sectionSize.longValue());
    }

    @Override
    public String toString() {
        return String.format(
                "%4s | %20s | %24s | %40s | %s | %s | 0x%08x | 0x%08x | %2d | %2d",
                nameIndex, name, type, flags, inMemoryAddress, fileOffset,
                link, info, addressAlignment, containedEntrySize);
    }

    public boolean hasNameStartingWith(String prefix) {
        requireNonNull(prefix);

        if (name == null) return false;
        return name.startsWith(prefix);
    }

    public ElfAddress<NATIVE_WORD> endVirtualAddress() {
        return virtualAddress().plus(size().longValue());
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class ElfHeader<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    @ElfApi("e_ident")
    private final ElfIdentification identification;

    @ElfApi("e_type")
    private final ElfType type;

    @ElfApi("e_machine")
    private final ElfMachine machine;

    @ElfApi("e_version")
    private final ElfVersion version;

    @ElfApi("e_entry")
    private final ElfAddress<NATIVE_WORD> entry;

    @ElfApi("e_phoff")
    private final ElfOffset<NATIVE_WORD> programHeaderTableOffset;

    @ElfApi("e_shoff")
    private final ElfOffset<NATIVE_WORD> sectionHeaderTableOffset;

    @ElfApi("e_flags")
    private int flags;

    @ElfApi("e_ehsize")
    private short elfHeaderSize;

    @ElfApi("e_phentsize")
    private short programHeaderSize;

    @ElfApi("e_phnum")
    private short programHeaderCount;

    @ElfApi("e_shentsize")
    private short sectionHeaderSize;

    @ElfApi("e_shnum")
    private short sectionHeaderCount;

    @ElfApi("e_shstrndx")
    private SectionHeaderIndex sectionNamesStringTableIndex;

    public ElfHeader(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       ElfAddress<NATIVE_WORD> entry,
                       ElfOffset<NATIVE_WORD> programHeaderTableOffset,
                       ElfOffset<NATIVE_WORD> sectionHeaderTableOffset,
                       int flags,
                       short elfHeaderSize,
                       short programHeaderSize,
                       short programHeaderCount,
                       short sectionHeaderSize,
                       short sectionHeaderCount,
                       SectionHeaderIndex sectionNamesStringTableIndex) {
        this.identification = identification;
        this.type = type;
        this.machine = machine;
        this.version = version;
        this.entry = entry;
        this.programHeaderTableOffset = programHeaderTableOffset;
        this.sectionHeaderTableOffset = sectionHeaderTableOffset;
        this.flags = flags;
        this.elfHeaderSize = elfHeaderSize;
        this.programHeaderSize = programHeaderSize;
        this.programHeaderCount = programHeaderCount;
        this.sectionHeaderSize = sectionHeaderSize;
        this.sectionHeaderCount = sectionHeaderCount;
        this.sectionNamesStringTableIndex = sectionNamesStringTableIndex;
    }

    public ElfIdentification identification() {
        return identification;
    }

    public ElfType type() {
        return type;
    }

    public ElfMachine machine() {
        return machine;
    }

    public ElfVersion version() {
        return version;
    }

    /**
     * @return Returns the virtual address to which the system
     * first transfers control, thus starting the process.  If
     * the file has no associated entry point, this member holds
     * zero.
     */
    public ElfAddress<NATIVE_WORD> entry() {
        return entry;
    }

    /**
     * @return Returns the program header table's file offset
     * in bytes.  If the file has no program header table, this
     * member holds zero.
     */
    public ElfOffset<NATIVE_WORD> programHeaderTableOffset() {
        return programHeaderTableOffset;
    }

    /**
     * @return Returns the section header table's file offset
     * in bytes.  If the file has no section header table, this
     * member holds zero.
     */
    public ElfOffset<NATIVE_WORD> sectionHeaderTableOffset() {
        return sectionHeaderTableOffset;
    }

    // TODO: Create specific wrapper after figuring out what these flags are about
    // TODO: There is a lot of flags: https://github.com/lattera/glibc/blob/master/elf/elf.h#L3137
    // each architecture has it's own flags e.g. ARM.
    public int flags() {
        return flags;
    }

    /**
     * @return ELF32 header size in bytes.
     */
    public int headerSize() {
        return elfHeaderSize & 0xffff;
    }

    /**
     * @return Returns the size in bytes of one entry in the
     * file's program header table; all entries are the same
     * size.
     */
    public int programHeaderSize() {
        return programHeaderSize & 0xffff;
    }

    /**
     * @return Return the number of entries in the program
     * header table.  Thus the product of e_phentsize and e_phnum
     * gives the table's size in bytes.  If a file has no program
     * header, e_phnum holds the value zero.
     * <p>
     * If the number of entries in the program header table is
     * larger than or equal to PN_XNUM (0xffff), this member
     * holds PN_XNUM (0xffff) and the real number of entries in
     * the program header table is held in the sh_info member of
     * the initial entry in section header table.  Otherwise, the
     * sh_info member of the initial entry contains the value
     * zero.
     */
    // TODO: properly handle case with PN_XNUM - add actualNumberOfProgHeaders()..
    // TODO: Javadoc formatting
    public int numberOfProgramHeaders() {
        return programHeaderCount & 0xffff;
    }

    /**
     * @return Returns a sections header's size in bytes.  A
     * section header is one entry in the section header table;
     * all entries are the same size.
     */
    public int sectionHeaderSize() {
        return sectionHeaderSize & 0xffff;
    }

    /**
     * @return This member holds the number of entries in the section
     * header table.  Thus the product of e_shentsize and e_shnum
     * gives the section header table's size in bytes.  If a file
     * has no section header table, e_shnum holds the value of
     * zero.
     * <p>
     * If the number of entries in the section header table is
     * larger than or equal to SHN_LORESERVE (0xff00), e_shnum
     * holds the value zero and the real number of entries in the
     * section header table is held in the sh_size member of the
     * initial entry in section header table.  Otherwise, the
     * sh_size member of the initial entry in the section header
     * table holds the value zero.
     */
    // TODO: Properly handle size overflow, add programHeadersTable of type TableHelper
    public int numberOfSectionHeaders() {
        return sectionHeaderCount & 0xffff;
    }

    /**
     * @return Returns the section header table index of the
     * entry associated with the section name string table.  If
     * the file has no section name string table, this member
     * holds the value SHN_UNDEF.
     * <p>
     * If the index of section name string table section is
     * larger than or equal to SHN_LORESERVE (0xff00), this
     * member holds SHN_XINDEX (0xffff) and the real index of the
     * section name string table section is held in the sh_link
     * member of the initial entry in section header table.
     * Otherwise, the sh_link member of the initial entry in
     * section header table contains the value zero.
     */
    // TODO: Properly handle size overflow
    public SectionHeaderIndex sectionContainingSectionNames() {
        return sectionNamesStringTableIndex;
    }

    public boolean isIntel386() {
        return identification.elfClass() == ElfClass.ELF_CLASS_32
                && identification.elfData() == ElfData.ELF_DATA_LSB
                && machine == ElfMachine.INTEL_386;
    }
}

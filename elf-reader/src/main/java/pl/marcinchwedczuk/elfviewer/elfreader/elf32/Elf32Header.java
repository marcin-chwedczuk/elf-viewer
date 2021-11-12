package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32Header {
    @ElfApi("e_ident")
    private final ElfIdentification identification;

    @ElfApi("e_type")
    private final ElfType type;

    @ElfApi("e_machine")
    private final ElfMachine machine;

    @ElfApi("e_version")
    private final ElfVersion version;

    @ElfApi("e_entry")
    private final Elf32Address entry;

    @ElfApi("e_phoff")
    private final Elf32Offset programHeaderTableOffset;

    @ElfApi("e_shoff")
    private final Elf32Offset sectionHeaderTableOffset;

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
    private SectionHeaderTableIndex sectionNamesStringTableIndex;

    public Elf32Header(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       Elf32Address entry,
                       Elf32Offset programHeaderTableOffset,
                       Elf32Offset sectionHeaderTableOffset,
                       int flags,
                       short elfHeaderSize,
                       short programHeaderSize,
                       short programHeaderCount,
                       short sectionHeaderSize,
                       short sectionHeaderCount,
                       SectionHeaderTableIndex sectionNamesStringTableIndex) {
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

    public Elf32Address entry() {
        return entry;
    }

    public Elf32Offset programHeaderTableOffset() {
        return programHeaderTableOffset;
    }

    public Elf32Offset sectionHeaderTableOffset() {
        return sectionHeaderTableOffset;
    }

    // TODO: Create specific wrapper after figuring out what these flags are about
    public int flags() {
        return flags;
    }

    public int headerSize() {
        return elfHeaderSize & 0xffff;
    }

    public int programHeaderSize() {
        return programHeaderSize & 0xffff;
    }

    public int numberOfProgramHeaders() {
        return programHeaderCount & 0xffff;
    }

    public int sectionHeaderSize() {
        return sectionHeaderSize & 0xffff;
    }

    public int numberOfSectionHeaders() {
        return sectionHeaderCount & 0xffff;
    }

    public SectionHeaderTableIndex sectionContainingSectionNames() {
        return sectionNamesStringTableIndex;
    }

    public boolean isIntel386() {
        return identification.elfClass() == ElfClass.ELF_CLASS_32
                && identification.elfData() == ElfData.ELF_DATA_LSB
                && machine == ElfMachine.Intel386;
    }
}

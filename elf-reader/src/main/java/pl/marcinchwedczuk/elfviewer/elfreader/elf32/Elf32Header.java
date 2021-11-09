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
    private short headerSize;

    @ElfApi("e_phentsize")
    private short programHeaderSize;

    @ElfApi("e_phnum")
    private short numberOfProgramHeaders;

    @ElfApi("e_shentsize")
    private short sectionHeaderSize;

    @ElfApi("e_shnum")
    private short numberOfSectionHeaders;

    @ElfApi("e_shstrndx")
    private SHTIndex sectionNameStringTableIndex;

    public Elf32Header(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       Elf32Address entry,
                       Elf32Offset programHeaderTableOffset,
                       Elf32Offset sectionHeaderTableOffset,
                       int flags,
                       short headerSize,
                       short programHeaderSize,
                       short numberOfProgramHeaders,
                       short sectionHeaderSize,
                       short numberOfSectionHeaders,
                       SHTIndex sectionNameStringTableIndex) {
        this.identification = identification;
        this.type = type;
        this.machine = machine;
        this.version = version;
        this.entry = entry;
        this.programHeaderTableOffset = programHeaderTableOffset;
        this.sectionHeaderTableOffset = sectionHeaderTableOffset;
        this.flags = flags;
        this.headerSize = headerSize;
        this.programHeaderSize = programHeaderSize;
        this.numberOfProgramHeaders = numberOfProgramHeaders;
        this.sectionHeaderSize = sectionHeaderSize;
        this.numberOfSectionHeaders = numberOfSectionHeaders;
        this.sectionNameStringTableIndex = sectionNameStringTableIndex;
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
        return headerSize & 0xffff;
    }

    public int programHeaderSize() {
        return programHeaderSize & 0xffff;
    }

    public int numberOfProgramHeaders() {
        return numberOfProgramHeaders & 0xffff;
    }

    public int sectionHeaderSize() {
        return sectionHeaderSize & 0xffff;
    }

    public int numberOfSectionHeaders() {
        return numberOfSectionHeaders & 0xffff;
    }

    public SHTIndex sectionNameStringTableIndex() {
        return sectionNameStringTableIndex;
    }
}

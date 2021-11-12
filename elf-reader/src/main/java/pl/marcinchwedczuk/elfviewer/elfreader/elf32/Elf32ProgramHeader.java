package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public class Elf32ProgramHeader {
    /**
     * This member tells what kind of segment this array element describes or
     * how to interpret the array element's information.
     * Type values and their meanings appear below.
     */
    @ElfApi("p_type")
    private final Elf32SegmentType type;

    /**
     * This member gives the offset from the beginning of the file
     * at which the first byte of the segment resides.
     */
    @ElfApi("p_offset")
    private final Elf32Offset fileOffset;

    /**
     * This member gives the virtual address at which the first byte
     * of the segment resides in memory.
     */
    @ElfApi("p_vaddr")
    private final Elf32Address virtualAddress;

    /**
     * On systems for which physical addressing is relevant,
     * this member is reserved for the segment's physical address.
     * This member requires operating system specific information,
     * which is described in the appendix at the end of Book III.
     */
    @ElfApi("p_paddr")
    private final Elf32Address physicalAddress;

    /**
     * This member gives the number of bytes in
     * the file image of the segment; it may be zero.
     */
    @ElfApi("p_filesz")
    private final int fileSize;
    /**
     * This member gives the number of bytes in
     * the memory image of the segment; it may be zero.
     */
    @ElfApi("p_memsz")
    private final int memorySize;

    // TODO: Figure out what values are allowed
    /**
     * This member gives flags relevant to the segment.
     * Defined flag values appear below.
     */
    @ElfApi("p_flags")
    private final Elf32SegmentFlags flags;

    /**
     * Loadable process segments must have congruent values for p_vaddr and p_offset,
     * modulo the page size. This member gives the value to which the segments
     * are aligned in memory and in the file.
     * Values 0 and 1 mean that no alignment is required.
     * Otherwise, p_align should be a positive, integral power of 2,
     * and p_addr should equal p_offset, modulo p_align.
     */
    @ElfApi("p_align")
    private final int alignment;

    public Elf32ProgramHeader(Elf32SegmentType type,
                              Elf32Offset fileOffset,
                              Elf32Address virtualAddress,
                              Elf32Address physicalAddress,
                              int fileSize,
                              int memorySize,
                              Elf32SegmentFlags flags,
                              int alignment) {
        this.type = type;
        this.fileOffset = fileOffset;
        this.virtualAddress = virtualAddress;
        this.physicalAddress = physicalAddress;
        this.fileSize = fileSize;
        this.memorySize = memorySize;
        this.flags = flags;
        this.alignment = alignment;
    }

    public Elf32SegmentType type() { return type; }
    public Elf32Offset fileOffset() { return fileOffset; }
    public Elf32Address virtualAddress() { return virtualAddress; }
    public Elf32Address physicalAddress() { return physicalAddress; }
    public int fileSize() { return fileSize; }
    public int memorySize() { return memorySize; }
    public Elf32SegmentFlags flags() { return flags; }
    public int alignment() { return alignment; }

    @Override
    public String toString() {
        return String.format(
                "%20s %s %s %s %4d %4d %s %2d",
                type, fileOffset, virtualAddress, physicalAddress,
                fileSize, memorySize, flags, alignment);
    }
}

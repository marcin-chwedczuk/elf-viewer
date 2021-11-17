package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

/**
 * An executable or shared object file's program header table is an
 * array of structures, each describing a segment or other
 * information the system needs to prepare the program for
 * execution.  An object file segment contains one or more sections.
 * Program headers are meaningful only for executable and shared
 * object files.
 */
public class Elf32ProgramHeader {
    @ElfApi("p_type")
    private final Elf32SegmentType type;

    @ElfApi("p_offset")
    private final Elf32Offset fileOffset;

    @ElfApi("p_vaddr")
    private final Elf32Address virtualAddress;

    @ElfApi("p_paddr")
    private final Elf32Address physicalAddress;

    @ElfApi("p_filesz")
    private final int fileSize;

    @ElfApi("p_memsz")
    private final int memorySize;

    // TODO: Figure out what values are allowed
    @ElfApi("p_flags")
    private final Elf32SegmentFlags flags;

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

    /**
     * @return Segment type which indicates what kind of
     * segment this array element describes or how to interpret
     * the array element's information.
     */
    public Elf32SegmentType type() {
        return type;
    }

    /**
     * @return The offset from the beginning of the
     * file at which the first byte of the segment resides.
     */
    public Elf32Offset fileOffset() {
        return fileOffset;
    }

    /**
     * @return The virtual address at which the first
     * byte of the segment resides in memory.
     */
    public Elf32Address virtualAddress() {
        return virtualAddress;
    }

    /**
     * @return On systems for which physical addressing is relevant, this
     * member is reserved for the segment's physical address.
     * Under BSD this member is not used and must be zero.
     */
    public Elf32Address physicalAddress() {
        return physicalAddress;
    }

    /**
     * @return The number of bytes in the file image of
     * the segment.  It may be zero.
     */
    public int fileSize() {
        return fileSize;
    }

    /**
     * @return The number of bytes in the memory image
     * of the segment.  It may be zero.
     */
    public int memorySize() {
        return memorySize;
    }

    /**
     * @return This member holds a bit mask of flags relevant to the
     * segment e.g. eXecutable, or Writable.
     */
    public Elf32SegmentFlags flags() {
        return flags;
    }

    /**
     * @return The value to which the segments are
     * aligned in memory and in the file.  Loadable process
     * segments must have congruent values for p_vaddr and
     * p_offset, modulo the page size.  Values of zero and one
     * mean no alignment is required.  Otherwise, p_align should
     * be a positive, integral power of two, and p_vaddr should
     * equal p_offset, modulo p_align.
     */
    public int alignment() {
        return alignment;
    }

    @Override
    public String toString() {
        return String.format(
                "%20s %s %s %s %4d %4d %s %2d",
                type, fileOffset, virtualAddress, physicalAddress,
                fileSize, memorySize, flags, alignment);
    }
}

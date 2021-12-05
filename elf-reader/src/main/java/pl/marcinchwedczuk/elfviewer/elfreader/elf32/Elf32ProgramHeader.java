package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;

public class Elf32ProgramHeader {
    private final ElfProgramHeader<Integer> programHeader;

    public Elf32ProgramHeader(ElfProgramHeader<Integer> programHeader) {
        this.programHeader = programHeader;
    }

    public Elf32SegmentType type() {
        return programHeader.type();
    }
    public Elf32Offset fileOffset() { return new Elf32Offset(programHeader.fileOffset()); }

    public Elf32Address virtualAddress() { return new Elf32Address(programHeader.virtualAddress()); }
    public Elf32Address physicalAddress() { return new Elf32Address(programHeader.physicalAddress()); }

    public int fileSize() { return programHeader.fileSize(); }
    public int memorySize() { return programHeader.memorySize(); }

    public Elf32SegmentFlags flags() {
        return programHeader.flags();
    }

    public int alignment() {
        return programHeader.alignment();
    }

    @Override
    public String toString() {
        return programHeader.toString();
    }

    public Elf32Address endVirtualAddress() {
        return new Elf32Address(programHeader.endVirtualAddress());
    }

    public Elf32Address endVirtualAddressInFile() {
        return new Elf32Address(programHeader.endVirtualAddressInFile());
    }

    public Elf32Offset endOffsetInFile() {
        return new Elf32Offset(programHeader.endOffsetInFile());
    }

    public boolean containsSection(Elf32SectionHeader section) {
        return programHeader.containsSection(section.unwrap());
    }
}

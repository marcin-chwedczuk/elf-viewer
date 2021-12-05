package pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SegmentFlags;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SegmentType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64SectionHeader;

public class Elf64ProgramHeader {
    private final ElfProgramHeader<Long> programHeader;

    public Elf64ProgramHeader(ElfProgramHeader<Long> programHeader) {
        this.programHeader = programHeader;
    }

    public Elf32SegmentType type() {
        return programHeader.type();
    }
    public Elf64Offset fileOffset() { return new Elf64Offset(programHeader.fileOffset()); }

    public Elf64Address virtualAddress() { return new Elf64Address(programHeader.virtualAddress()); }
    public Elf64Address physicalAddress() { return new Elf64Address(programHeader.physicalAddress()); }

    public long fileSize() { return programHeader.fileSize(); }
    public long memorySize() { return programHeader.memorySize(); }

    public Elf32SegmentFlags flags() {
        return programHeader.flags();
    }

    public long alignment() {
        return programHeader.alignment();
    }

    @Override
    public String toString() {
        return programHeader.toString();
    }

    public Elf64Address endVirtualAddress() {
        return new Elf64Address(programHeader.endVirtualAddress());
    }

    public Elf64Address endVirtualAddressInFile() {
        return new Elf64Address(programHeader.endVirtualAddressInFile());
    }

    public Elf64Offset endOffsetInFile() {
        return new Elf64Offset(programHeader.endOffsetInFile());
    }

    public boolean containsSection(Elf64SectionHeader section) {
        return programHeader.containsSection(section.unwrap());
    }
}

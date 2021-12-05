package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;

import static java.util.Objects.requireNonNull;

public class Elf64SectionHeader {
    public static Elf64SectionHeader safeWrap(ElfSectionHeader<Long> header) {
        return header == null ? null : new Elf64SectionHeader(header);
    }

    private final ElfSectionHeader<Long> sectionHeader;
    public ElfSectionHeader<Long> unwrap() { return sectionHeader; }

    public Elf64SectionHeader(ElfSectionHeader<Long> sectionHeader) {
        this.sectionHeader = requireNonNull(sectionHeader);
    }

    public StringTableIndex nameIndex() { return sectionHeader.nameIndex(); }
    public String name() { return sectionHeader.name(); }
    public boolean hasName(String name) { return sectionHeader.hasName(name); }
    public ElfSectionType type() {
        return sectionHeader.type();
    }
    public SectionAttributes flags() {
        return sectionHeader.flags();
    }

    public Elf64Address virtualAddress() {
        return new Elf64Address(sectionHeader.virtualAddress());
    }

    public Elf64Offset fileOffset() {
        return (Elf64Offset) sectionHeader.fileOffset();
    }

    public long size() {
        return sectionHeader.size();
    }
    public int link() {
        return sectionHeader.link();
    }
    public int info() {
        return sectionHeader.info();
    }
    public long addressAlignment() {
        return sectionHeader.addressAlignment();
    }

    public long containedEntrySize() { return sectionHeader.containedEntrySize(); }
    public Elf64Offset sectionEndOffsetInFile() { return (Elf64Offset)sectionHeader.sectionEndOffsetInFile(); }

    @Override
    public String toString() { return sectionHeader.toString(); }

    public boolean hasNameStartingWith(String prefix) { return sectionHeader.hasNameStartingWith(prefix); }
    public Elf64Address endVirtualAddress() { return (Elf64Address) sectionHeader.endVirtualAddress(); }
}

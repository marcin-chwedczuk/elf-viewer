package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;

import static java.util.Objects.requireNonNull;

public class Elf32SectionHeader {
    public static Elf32SectionHeader safeWrap(ElfSectionHeader<Integer> header) {
        return header == null ? null : new Elf32SectionHeader(header);
    }

    private final ElfSectionHeader<Integer> sectionHeader;
    public ElfSectionHeader<Integer> unwrap() { return sectionHeader; }

    public Elf32SectionHeader(ElfSectionHeader<Integer> sectionHeader) {
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

    public Elf32Address virtualAddress() {
        return (Elf32Address) sectionHeader.virtualAddress();
    }

    public Elf32Offset fileOffset() {
        return (Elf32Offset) sectionHeader.fileOffset();
    }

    public int size() {
        return sectionHeader.size();
    }
    public int link() {
        return sectionHeader.link();
    }
    public int info() {
        return sectionHeader.info();
    }
    public int addressAlignment() {
        return sectionHeader.addressAlignment();
    }

    public int containedEntrySize() { return sectionHeader.containedEntrySize(); }
    public Elf32Offset sectionEndOffsetInFile() { return (Elf32Offset)sectionHeader.sectionEndOffsetInFile(); }

    @Override
    public String toString() { return sectionHeader.toString(); }

    public boolean hasNameStartingWith(String prefix) { return sectionHeader.hasNameStartingWith(prefix); }
    public Elf32Address endVirtualAddress() { return (Elf32Address) sectionHeader.endVirtualAddress(); }
}

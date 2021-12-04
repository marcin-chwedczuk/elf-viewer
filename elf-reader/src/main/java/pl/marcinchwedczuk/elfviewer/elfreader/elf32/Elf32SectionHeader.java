package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;

public class Elf32SectionHeader extends ElfSectionHeader<Integer> {
    public Elf32SectionHeader(
            StringTableIndex nameIndex,
            String name,
            ElfSectionType type,
            SectionAttributes flags,
            Elf32Address inMemoryAddress,
            Elf32Offset fileOffset,
            int sectionSize,
            int link,
            int info,
            int addressAlignment,
            int containedEntrySize) {
        super(nameIndex,
                name,
                type,
                flags,
                inMemoryAddress,
                fileOffset,
                sectionSize,
                link,
                info,
                addressAlignment,
                containedEntrySize);
    }

    @Override
    public Elf32Address virtualAddress() {
        return (Elf32Address) super.virtualAddress();
    }

    @Override
    public Elf32Offset fileOffset() {
        return (Elf32Offset) super.fileOffset();
    }

    @Override
    public Elf32Offset sectionEndOffsetInFile() {
        return (Elf32Offset) super.sectionEndOffsetInFile();
    }

    @Override
    public Elf32Address endVirtualAddress() {
        return (Elf32Address) super.endVirtualAddress();
    }
}

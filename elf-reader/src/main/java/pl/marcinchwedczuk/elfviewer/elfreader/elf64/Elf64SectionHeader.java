package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;

public class Elf64SectionHeader extends ElfSectionHeader<Long> {
    public Elf64SectionHeader(StringTableIndex nameIndex,
                              String name,
                              ElfSectionType type,
                              SectionAttributes flags,
                              Elf64Address inMemoryAddress,
                              Elf64Offset fileOffset,
                              long sectionSize,
                              int link,
                              int info,
                              long addressAlignment,
                              long containedEntrySize) {
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
}

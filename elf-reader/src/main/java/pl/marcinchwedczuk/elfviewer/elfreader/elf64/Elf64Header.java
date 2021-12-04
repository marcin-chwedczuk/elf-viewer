package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfMachine;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;

public class Elf64Header extends ElfHeader<Long> {
    public Elf64Header(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       Elf64Address entry,
                       Elf64Offset programHeaderTableOffset,
                       Elf64Offset sectionHeaderTableOffset,
                       int flags,
                       short elfHeaderSize,
                       short programHeaderSize,
                       short programHeaderCount,
                       short sectionHeaderSize,
                       short sectionHeaderCount,
                       SectionHeaderIndex sectionNamesStringTableIndex) {
        super(identification,
                type,
                machine,
                version,
                entry,
                programHeaderTableOffset,
                sectionHeaderTableOffset,
                flags,
                elfHeaderSize,
                programHeaderSize,
                programHeaderCount,
                sectionHeaderSize,
                sectionHeaderCount,
                sectionNamesStringTableIndex);
    }
}

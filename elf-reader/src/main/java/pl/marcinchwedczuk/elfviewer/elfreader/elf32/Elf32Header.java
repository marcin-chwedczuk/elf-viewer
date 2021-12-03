package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfMachine;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

public class Elf32Header
        extends ElfHeader<Integer>
        implements Elf32Visitable {

    public Elf32Header(ElfIdentification identification,
                       ElfType type,
                       ElfMachine machine,
                       ElfVersion version,
                       Elf32Address entry,
                       Elf32Offset programHeaderTableOffset,
                       Elf32Offset sectionHeaderTableOffset,
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

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(identification());
        visitor.exit(identification());

        visitor.enter(this);
        visitor.exit(this);
    }

    @Override
    public Elf32Address entry() {
        return (Elf32Address) super.entry();
    }

    @Override
    public Elf32Offset programHeaderTableOffset() {
        return (Elf32Offset) super.programHeaderTableOffset();
    }

    @Override
    public Elf32Offset sectionHeaderTableOffset() {
        return (Elf32Offset) super.sectionHeaderTableOffset();
    }
}

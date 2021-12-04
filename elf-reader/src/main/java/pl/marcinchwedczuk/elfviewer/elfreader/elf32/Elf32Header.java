package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Address;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Elf32Header
        implements Elf32Visitable {

    private final ElfHeader<Integer> header;

    public Elf32Header(ElfHeader<Integer> header) {
        this.header = requireNonNull(header);
    }

    public ElfIdentification identification() {
        return header.identification();
    }

    public ElfType type() {
        return header.type();
    }

    public ElfMachine machine() {
        return header.machine();
    }

    public ElfVersion version() {
        return header.version();
    }

    public Elf32Address entry() {
        return (Elf32Address) header.entry();
    }

    public Elf32Offset programHeaderTableOffset() {
        return (Elf32Offset) header.programHeaderTableOffset();
    }

    public Elf32Offset sectionHeaderTableOffset() {
        return (Elf32Offset) header.sectionHeaderTableOffset();
    }

    public int flags() {
        return header.flags();
    }

    public int headerSize() {
        return header.headerSize();
    }

    public int programHeaderSize() {
        return header.programHeaderSize();
    }

    public int numberOfProgramHeaders() { return header.numberOfProgramHeaders(); }

    public int sectionHeaderSize() { return header.sectionHeaderSize(); }

    public int numberOfSectionHeaders() { return header.numberOfSectionHeaders(); }

    public SectionHeaderIndex sectionContainingSectionNames() {
        return header.sectionContainingSectionNames();
    }

    public boolean isIntel386() { return header.isIntel386(); }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(identification());
        visitor.exit(identification());

        visitor.enter(this);
        visitor.exit(this);
    }

}

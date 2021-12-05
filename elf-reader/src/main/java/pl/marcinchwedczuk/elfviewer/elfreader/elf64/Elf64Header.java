package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfMachine;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;

import static java.util.Objects.requireNonNull;

public class Elf64Header {
    private final ElfHeader<Long> header;

    public Elf64Header(ElfHeader<Long> header) {
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

    public Elf64Address entry() {
        return new Elf64Address(header.entry());
    }

    public Elf64Offset programHeaderTableOffset() {
        return new Elf64Offset(header.programHeaderTableOffset());
    }

    public Elf64Offset sectionHeaderTableOffset() {
        return new Elf64Offset(header.sectionHeaderTableOffset());
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

}

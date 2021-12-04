package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;

public class Elf64SectionFactory extends ElfSectionFactory<Long> {
    @Override
    protected ElfSection<Long> mkInterpreterSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkStringTableSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkSymbolTableSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkDynamicSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkRelocationsSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkNotesSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkGnuHashSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return mkSection(file, header);
    }

    @Override
    protected ElfSection<Long> mkSection(ElfFile<Long> file, ElfSectionHeader<Long> header) {
        return new Elf64Section((Elf64File) file, (Elf64SectionHeader) header);
    }

    @Override
    protected ElfSection<Long> mkInvalidSection(ElfFile<Long> file, ElfSectionHeader<Long> header, Exception error) {
        return mkSection(file, header);
    }
}

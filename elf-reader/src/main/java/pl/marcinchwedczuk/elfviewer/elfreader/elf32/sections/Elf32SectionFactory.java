package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;

import java.util.List;

public class Elf32SectionFactory extends ElfSectionFactory<Integer> {
    // Shadow base class method
    @SuppressWarnings("unchecked")
    public List<Elf32Section> createSections(Elf32File elfFile) {
        return (List<Elf32Section>) super.createSections(elfFile);
    }

    @Override
    protected ElfSection<Integer> mkInterpreterSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32InterpreterSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkStringTableSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32StringTableSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkSymbolTableSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32SymbolTableSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkDynamicSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32DynamicSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkRelocationsSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32RelocationSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkNotesSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32NotesSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkGnuHashSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32GnuHashSection((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header) {
        return new Elf32Section((Elf32File) file, (Elf32SectionHeader) header);
    }

    @Override
    protected ElfSection<Integer> mkInvalidSection(ElfFile<Integer> file, ElfSectionHeader<Integer> header, Exception error) {
        return new Elf32InvalidSection((Elf32File) file, (Elf32SectionHeader) header, error);
    }
}

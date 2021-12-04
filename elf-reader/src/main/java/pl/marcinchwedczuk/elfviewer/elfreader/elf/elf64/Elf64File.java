package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

import java.util.List;
import java.util.Optional;

public class Elf64File extends ElfFile<Long> {
    public Elf64File(AbstractFile storage,
                     Endianness endianness,
                     ElfHeader<Long> header,
                     List<? extends ElfSectionHeader<Long>> elfSectionHeaders) {
        super(storage, endianness, header, elfSectionHeaders,
                new Elf64SectionFactory());
    }

    @Override
    public Elf64Header header() {
        return (Elf64Header) super.header();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Elf64Section> sectionWithName(String name) {
        return (Optional<Elf64Section>) super.sectionWithName(name);
    }
}

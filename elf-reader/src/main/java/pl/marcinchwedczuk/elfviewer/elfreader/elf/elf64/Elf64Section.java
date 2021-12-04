package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile64;

public class Elf64Section extends ElfSection<Long> {
    public Elf64Section(Elf64File elfFile,
                        Elf64SectionHeader header) {
        super(elfFile, header);
    }

    @Override
    public Elf64File elfFile() {
        return (Elf64File) super.elfFile();
    }

    @Override
    public Elf64SectionHeader header() {
        return (Elf64SectionHeader) super.header();
    }

    @Override
    protected StructuredFile<Long> mkStructuredFile(ElfFile<Long> file, ElfOffset<Long> offset) {
        return new StructuredFile64((Elf64File) file, (Elf64Offset) offset);
    }
}

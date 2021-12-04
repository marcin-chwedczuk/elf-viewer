package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;

public class Elf32Section extends ElfSection<Integer> implements Elf32Visitable {
    public Elf32Section(Elf32File elfFile,
                        Elf32SectionHeader header) {
        super(elfFile, header);
    }

    @Override
    protected StructuredFile<Integer> mkStructuredFile(ElfFile<Integer> file, ElfOffset<Integer> offset) {
        return new StructuredFile32((Elf32File) file, (Elf32Offset) offset);
    }

    @Override
    public Elf32File elfFile() {
        return (Elf32File) super.elfFile();
    }

    @Override
    public Elf32SectionHeader header() {
        return (Elf32SectionHeader) super.header();
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

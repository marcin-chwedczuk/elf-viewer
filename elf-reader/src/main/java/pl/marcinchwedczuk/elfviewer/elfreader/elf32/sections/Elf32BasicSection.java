package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class Elf32BasicSection implements Elf32Visitable {
    private final ElfSection<Integer> section;

    public Elf32BasicSection(ElfSection<Integer> section) {
        this.section = requireNonNull(section);
    }

    public Elf32File elfFile() { return new Elf32File(section.elfFile(), null); }
    public Elf32SectionHeader header() { return new Elf32SectionHeader(section.header()); }

    public String name() { return section.name(); }
    public FileView contents() { return section.contents(); }
    public boolean containsStrings() { return section.containsStrings(); }
    public List<String> readContentsAsStrings() { return section.readContentsAsStrings(); }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

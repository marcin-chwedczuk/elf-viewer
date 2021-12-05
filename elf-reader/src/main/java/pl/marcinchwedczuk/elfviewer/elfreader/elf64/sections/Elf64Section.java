package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class Elf64Section {
    private final ElfSection<Long> section;

    public Elf64Section(ElfSection<Long> section) {
        this.section = requireNonNull(section);
    }

    public Elf64File elfFile() {
        return new Elf64File(section.elfFile());
    }

    public Elf64SectionHeader header() {
        return new Elf64SectionHeader(section.header());
    }

    public String name() {
        return section.name();
    }

    public FileView contents() {
        return section.contents();
    }

    public boolean containsStrings() {
        return section.containsStrings();
    }

    public List<String> readContentsAsStrings() {
        return section.readContentsAsStrings();
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Elf32Section {
    private final Elf32File elfFile;
    private final Elf32SectionHeader header;

    public Elf32Section(Elf32File elfFile,
                        Elf32SectionHeader header) {
        this.elfFile = requireNonNull(elfFile);
        this.header = requireNonNull(header);
    }

    // TODO: FileView sectionContents()

    public Elf32File elfFile() { return elfFile; }
    public Elf32SectionHeader header() { return header; }

    public String name() { return header.name(); }

    public FileView contents() {
        return new FileView(
                elfFile.storage,
                header.offsetInFile(),
                header.sectionSize());
    }
}

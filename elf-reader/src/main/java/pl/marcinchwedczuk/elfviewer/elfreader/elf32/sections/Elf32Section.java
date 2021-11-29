package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Element;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class Elf32Section extends Elf32Element {
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
                header.fileOffset(),
                header.size());
    }

    public boolean containsStrings() {
        return header.flags().hasFlag(STRINGS);
    }

    public List<String> readContentsAsStrings() {
        // TODO: Add asserts
        if (!header.flags().hasFlag(STRINGS))
            throw new ElfReaderException("Section " + name() + " does not contain strings.");

        // TODO: Move to using contents
        StructuredFile sf = new StructuredFile(
                elfFile,
                header.fileOffset());

        // TODO: Handle reading past section end - StructuredFile should support
        // start and end offsets
        List<String> result = new ArrayList<>();
        while (sf.currentPositionInFile().isBefore(header.sectionEndOffsetInFile())) {
            String s = sf.readStringNullTerminated();
            result.add(s);
        }

        return result;
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

}

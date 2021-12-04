package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public abstract class ElfSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    private final ElfFile<NATIVE_WORD> elfFile;
    private final ElfSectionHeader<NATIVE_WORD> header;

    public ElfSection(ElfFile<NATIVE_WORD> elfFile,
                        ElfSectionHeader<NATIVE_WORD> header) {
        this.elfFile = requireNonNull(elfFile);
        this.header = requireNonNull(header);
    }

    protected abstract StructuredFile<NATIVE_WORD> mkStructuredFile(
            ElfFile<NATIVE_WORD> file,
            ElfOffset<NATIVE_WORD> offset);

    // TODO: FileView sectionContents()

    public ElfFile<NATIVE_WORD> elfFile() { return elfFile; }
    public ElfSectionHeader<NATIVE_WORD> header() { return header; }

    public String name() { return header.name(); }

    public FileView contents() {
        return new FileView(
                elfFile.storage(),
                header.fileOffset(),
                header.size().longValue());
    }

    public boolean containsStrings() {
        return header.flags().hasFlag(STRINGS);
    }

    public List<String> readContentsAsStrings() {
        // TODO: Add asserts
        if (!header.flags().hasFlag(STRINGS))
            throw new ElfReaderException("Section " + name() + " does not contain strings.");

        // TODO: Move to using contents
        StructuredFile<NATIVE_WORD> sf = mkStructuredFile(
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

}

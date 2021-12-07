package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfElement;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class ElfSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
    extends ElfElement<NATIVE_WORD>
{

    protected final NativeWord<NATIVE_WORD> nativeWord;
    protected final StructuredFileFactory<NATIVE_WORD> structuredFileFactory;
    private final ElfFile<NATIVE_WORD> elfFile;
    private final ElfSectionHeader<NATIVE_WORD> header;

    public ElfSection(
            NativeWord<NATIVE_WORD> nativeWord,
            StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
            ElfFile<NATIVE_WORD> elfFile,
                        ElfSectionHeader<NATIVE_WORD> header) {
        this.nativeWord = requireNonNull(nativeWord);
        this.structuredFileFactory = requireNonNull(structuredFileFactory);
        this.elfFile = requireNonNull(elfFile);
        this.header = requireNonNull(header);
    }

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
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
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
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public class ElfSectionFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    private final NativeWord<NATIVE_WORD> nativeWord;
    private final StructuredFileFactory<NATIVE_WORD> structuredFileFactory;

    public ElfSectionFactory(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory) {
        this.nativeWord = nativeWord;
        this.structuredFileFactory = structuredFileFactory;
    }

    public List<ElfSection<NATIVE_WORD>> createSections(ElfFile<NATIVE_WORD> elfFile)
    {
        List<ElfSection<NATIVE_WORD>> sections = new ArrayList<>();

        for (ElfSectionHeader<NATIVE_WORD> header : elfFile.sectionHeaders()) {
            ElfSection<NATIVE_WORD> section = createSection(elfFile, header);
            sections.add(section);
        }

        return sections;
    }

    private ElfSection<NATIVE_WORD> createSection(ElfFile<NATIVE_WORD> elfFile,
                                                  ElfSectionHeader<NATIVE_WORD> header) {
        try {
            if (header.type().is(PROGBITS)
                    && header.hasName(ElfSectionNames.INTERP)) {
                return new ElfInterpreterSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(STRING_TABLE)) {
                return new ElfStringTableSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().isOneOf(SYMBOL_TABLE, DYNAMIC_SYMBOLS)) {
                return new ElfSymbolTableSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(DYNAMIC)) {
                return new ElfDynamicSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(REL)) {
                return new ElfRelocationSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(RELA)) {
                return new ElfRelocationAddendSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(NOTE)) {
                return new ElfNotesSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(GNU_HASH)) {
                return new ElfGnuHashSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(GNU_VERSYM)) {
                return new ElfGnuVersionSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(GNU_VERNEED)) {
                return new ElfGnuVersionRequirementsSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(GNU_VERDEF)) {
                return new ElfGnuVersionDefinitionsSection<>(nativeWord, structuredFileFactory, elfFile, header);
            } else if (header.type().is(HASH)) {
                return new ElfHashSection<>(nativeWord, structuredFileFactory, elfFile, header);
            }

            return new ElfSection<>(nativeWord, structuredFileFactory, elfFile, header);
        } catch (Exception error) {
            // TODO: This exception type is too broad...
            // We should catch RuntimeExceptions + IO exceptions maybe?
            return new ElfInvalidSection<>(nativeWord, structuredFileFactory, elfFile, header, error);
        }
    }
}

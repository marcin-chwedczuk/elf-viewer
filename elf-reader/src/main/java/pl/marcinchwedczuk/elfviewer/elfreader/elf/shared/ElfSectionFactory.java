package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public abstract class ElfSectionFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {

    public List<? extends ElfSection<NATIVE_WORD>> createSections(ElfFile<NATIVE_WORD> elfFile)
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
                return mkInterpreterSection(elfFile, header);
            } else if (header.type().is(STRING_TABLE)) {
                return mkStringTableSection(elfFile, header);
            } else if (header.type().isOneOf(SYMBOL_TABLE, DYNAMIC_SYMBOLS)) {
                return mkSymbolTableSection(elfFile, header);
            } else if (header.type().is(DYNAMIC)) {
                return mkDynamicSection(elfFile, header);
            } else if (header.type().is(REL)) {
                return mkRelocationsSection(elfFile, header);
            } else if (header.type().is(NOTE)) {
                return mkNotesSection(elfFile, header);
            } else if (header.type().is(GNU_HASH)) {
                return mkGnuHashSection(elfFile, header);
            }

            return mkSection(elfFile, header);
        } catch (Exception error) {
            // TODO: This exception type is too broad...
            // We should catch RuntimeExceptions + IO exceptions maybe?
            return mkInvalidSection(elfFile, header, error);
        }
    }

    protected abstract ElfSection<NATIVE_WORD> mkInterpreterSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkStringTableSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkSymbolTableSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkDynamicSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkRelocationsSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkNotesSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkGnuHashSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header);
    protected abstract ElfSection<NATIVE_WORD> mkInvalidSection(ElfFile<NATIVE_WORD> file, ElfSectionHeader<NATIVE_WORD> header, Exception error);
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public class Elf32SectionFactory {
    private final Elf32File elfFile;

    public Elf32SectionFactory(Elf32File elfFile) {
        this.elfFile = requireNonNull(elfFile);
    }

    public List<Elf32Section> createSections() {
        List<Elf32Section> sections = new ArrayList<>();

        for (Elf32SectionHeader sh : elfFile.sectionHeaders) {
            Elf32Section section = createSection(sh);
            sections.add(section);
        }

        return sections;
    }

    private Elf32Section createSection(Elf32SectionHeader sh) {
        try {
            if (sh.type().is(PROGBITS)
                    && sh.hasName(ElfSectionNames.INTERP)) {
                return new Elf32InterpreterSection(elfFile, sh);
            } else if (sh.type().is(STRING_TABLE)) {
                return new Elf32StringTableSection(elfFile, sh);
            } else if (sh.type().isOneOf(SYMBOL_TABLE, DYNAMIC_SYMBOLS)) {
                return new Elf32SymbolTableSection(elfFile, sh);
            } else if (sh.type().is(DYNAMIC)) {
                return new Elf32DynamicSection(elfFile, sh);
            }

            return new Elf32Section(elfFile, sh);
        } catch (Exception error) {
            // TODO: This exception type is too broad...
            // We should catch RuntimeExceptions + IO exceptions maybe?
            return new Elf32InvalidSection(elfFile, sh, error);
        }
    }
}

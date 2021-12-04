package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;

import java.util.List;
import java.util.stream.Collectors;

public class Elf32SectionFactory {

    public List<Elf32BasicSection> wrap(List<? extends ElfSection<Integer>> sections) {
        return sections.stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    public Elf32BasicSection wrap(ElfSection<Integer> section) {
        if (section instanceof ElfDynamicSection<?>) {
            return new Elf32DynamicSection((ElfDynamicSection<Integer>) section);
        }
        if (section instanceof ElfInterpreterSection<?>) {
            return new Elf32InterpreterSection((ElfInterpreterSection<Integer>) section);
        }
        if (section instanceof ElfInvalidSection<?>) {
            return new Elf32InvalidSection((ElfInvalidSection<Integer>) section);
        }
        if (section instanceof ElfNotesSection<?>) {
            return new Elf32NotesSection((ElfNotesSection<Integer>) section);
        }
        if (section instanceof ElfRelocationSection<?>) {
            return new Elf32RelocationSection((ElfRelocationSection<Integer>) section);
        }
        if (section instanceof ElfStringTableSection<?>) {
            return new Elf32StringTableSection((ElfStringTableSection<Integer>) section);
        }
        if (section instanceof ElfSymbolTableSection<?>) {
            return new Elf32SymbolTableSection((ElfSymbolTableSection<Integer>) section);
        }
        if (section instanceof ElfGnuHashSection<?>) {
            return new Elf32GnuHashSection((ElfGnuHashSection<Integer>) section);
        }

        return new Elf32BasicSection(section);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;

import java.util.List;
import java.util.stream.Collectors;

public class Elf64SectionFactory {

    public List<Elf64Section> wrap(List<? extends ElfSection<Long>> sections) {
        return sections.stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    public Elf64Section wrap(ElfSection<Long> section) {
        /*
        if (section instanceof ElfDynamicSection<?>) {
            return new Elf64DynamicSection((ElfDynamicSection<Long>) section);
        }
        if (section instanceof ElfInterpreterSection<?>) {
            return new Elf64InterpreterSection((ElfInterpreterSection<Long>) section);
        }
        if (section instanceof ElfInvalidSection<?>) {
            return new Elf64InvalidSection((ElfInvalidSection<Long>) section);
        }
        if (section instanceof ElfNotesSection<?>) {
            return new Elf64NotesSection((ElfNotesSection<Long>) section);
        }
        if (section instanceof ElfRelocationSection<?>) {
            return new Elf64RelocationSection((ElfRelocationSection<Long>) section);
        }
        if (section instanceof ElfStringTableSection<?>) {
            return new Elf64StringTableSection((ElfStringTableSection<Long>) section);
        }
        if (section instanceof ElfSymbolTableSection<?>) {
            return new Elf64SymbolTableSection((ElfSymbolTableSection<Long>) section);
        }
        if (section instanceof ElfGnuHashSection<?>) {
            return new Elf64GnuHashSection((ElfGnuHashSection<Long>) section);
        }
*/
        return new Elf64Section(section);
    }
}

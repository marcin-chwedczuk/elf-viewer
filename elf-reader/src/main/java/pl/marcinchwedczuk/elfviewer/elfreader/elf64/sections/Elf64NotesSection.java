package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNote;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfNotesSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

import java.util.List;

public class Elf64NotesSection extends Elf64Section {
    private final ElfNotesSection<Long> section;

    public Elf64NotesSection(ElfNotesSection<Long> section) {
        super(section);
        this.section = section;
    }

    public List<ElfNote> notes() {
        return section.notes();
    }
}

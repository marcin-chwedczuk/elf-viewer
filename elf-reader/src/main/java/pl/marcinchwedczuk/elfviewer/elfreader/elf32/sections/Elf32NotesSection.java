package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfNotesSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;

import java.util.ArrayList;
import java.util.List;

public class Elf32NotesSection extends Elf32BasicSection {
    private final ElfNotesSection<Integer> section;

    public Elf32NotesSection(ElfNotesSection<Integer> section) {
        super(section);
        this.section = section;
    }


    public List<Elf32Note> notes() {
        return section.notes();
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

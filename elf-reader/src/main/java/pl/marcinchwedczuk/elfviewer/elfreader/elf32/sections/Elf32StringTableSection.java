package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfStringTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

import static java.util.Objects.requireNonNull;

public class Elf32StringTableSection extends Elf32Section {
    private final ElfStringTableSection<Integer> section;
    public Elf32StringTableSection(ElfStringTableSection<Integer> section) {
        super(section);
        this.section = section;
    }

    public Elf32StringTable stringTable() {
        return new Elf32StringTable(section.stringTable());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

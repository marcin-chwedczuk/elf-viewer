package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInvalidSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

public class Elf32InvalidSection extends Elf32Section {
    private final ElfInvalidSection<Integer> section;

    public Elf32InvalidSection(ElfInvalidSection<Integer> section1) {
        super(section1);
        this.section = section1;
    }

    public Exception error() {
        return section.error();
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInterpreterSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

public class Elf32InterpreterSection extends Elf32Section {
    private final ElfInterpreterSection<Integer> section;

    public Elf32InterpreterSection(ElfInterpreterSection<Integer> section) {
        super(section);
        this.section = section;
    }

    public String interpreterPath() {
        return section.interpreterPath();
    }


    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

}

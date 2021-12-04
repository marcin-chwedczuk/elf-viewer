package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfInterpreterSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory32;

public class Elf32InterpreterSection extends Elf32BasicSection {
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

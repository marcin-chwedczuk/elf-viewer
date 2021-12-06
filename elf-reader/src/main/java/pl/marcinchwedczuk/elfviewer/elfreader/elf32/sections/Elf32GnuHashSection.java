package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuHashSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

public class Elf32GnuHashSection extends Elf32Section {
    private final ElfGnuHashSection<Integer> section;

    public Elf32GnuHashSection(ElfGnuHashSection<Integer> section1) {
        super(section1);
        this.section = section1;
    }

    public Elf32GnuHashTable gnuHashTable() {
        return new Elf32GnuHashTable(section.gnuHashTable());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

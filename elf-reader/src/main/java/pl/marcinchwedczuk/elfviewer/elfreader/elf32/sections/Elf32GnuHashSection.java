package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuHashSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.GNU_HASH;

public class Elf32GnuHashSection extends Elf32BasicSection {
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

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Relocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.RelocationsTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elf32RelocationSection extends Elf32BasicSection {
    private final ElfRelocationSection<Integer> section;

    public Elf32RelocationSection(ElfRelocationSection<Integer> section) {
        super(section);
        this.section = section;
    }


    public List<Elf32Relocation> relocations() {
        return section.relocations().stream()
                .map(Elf32Relocation::new)
                .collect(Collectors.toList());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationAddendSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Relocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32RelocationAddend;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class Elf32RelocationAddendSection extends Elf32Section {
    private final ElfRelocationAddendSection<Integer> section;

    public Elf32RelocationAddendSection(ElfRelocationAddendSection<Integer> section) {
        super(section);
        this.section = section;
    }

    public List<Elf32RelocationAddend> relocations() {
        return section.relocations().stream()
                .map(Elf32RelocationAddend::new)
                .collect(Collectors.toList());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

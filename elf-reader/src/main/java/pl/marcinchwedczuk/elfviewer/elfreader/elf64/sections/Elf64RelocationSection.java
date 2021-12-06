package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Relocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Relocation;

import java.util.List;
import java.util.stream.Collectors;

public class Elf64RelocationSection extends Elf64Section {
    private final ElfRelocationSection<Long> section;

    public Elf64RelocationSection(ElfRelocationSection<Long> section) {
        super(section);
        this.section = section;
    }

    public List<Elf64Relocation> relocations() {
        return section.relocations().stream()
                .map(Elf64Relocation::new)
                .collect(Collectors.toList());
    }

}

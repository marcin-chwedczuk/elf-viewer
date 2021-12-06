package pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationAddendSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfRelocationSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64Relocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.Elf64RelocationAddend;

import java.util.List;
import java.util.stream.Collectors;

public class Elf64RelocationAddendSection extends Elf64Section {
    private final ElfRelocationAddendSection<Long> section;

    public Elf64RelocationAddendSection(ElfRelocationAddendSection<Long> section) {
        super(section);
        this.section = section;
    }

    public List<Elf64RelocationAddend> relocations() {
        return section.relocations().stream()
                .map(Elf64RelocationAddend::new)
                .collect(Collectors.toList());
    }

}

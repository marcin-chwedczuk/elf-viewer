package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfDynamicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Elf32DynamicSection extends Elf32BasicSection {
    private final ElfDynamicSection<Integer> section;

    public Elf32DynamicSection(ElfDynamicSection<Integer> section) {
        super(section);
        this.section = section;
    }

    public List<Elf32DynamicTag> dynamicTags() {
        return section.dynamicTags()
                .stream().map(Elf32DynamicTag::new)
                .collect(Collectors.toList());
   }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

    public Optional<String> getDynamicLibraryName(Elf32DynamicTag neededTag) {
        return section.getDynamicLibraryName(neededTag.unwrap());
    }
}

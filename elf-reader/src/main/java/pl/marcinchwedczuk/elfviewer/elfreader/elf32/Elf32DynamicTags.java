package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTags;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Elf32DynamicTags {
    private final ElfDynamicTags<Integer> dynamicTags;

    public Elf32DynamicTags(ElfDynamicTags<Integer> dynamicTags) {
        this.dynamicTags = dynamicTags;
    }


    public List<Elf32DynamicTag> getTags() {
        return dynamicTags.getTags().stream()
                .map(Elf32DynamicTag::new)
                .collect(Collectors.toList());
    }
}

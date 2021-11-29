package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;
import java.util.Optional;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class Elf32DynamicSection extends Elf32Section {
    public Elf32DynamicSection(Elf32File elfFile,
                               Elf32SectionHeader header) {
        super(elfFile, header);
        Args.checkSectionType(header, DYNAMIC);
   }

   public List<Elf32DynamicTag> dynamicTags() {
        Elf32DynamicTags dynamicTags = new Elf32DynamicTags(
                elfFile(),
                header());

        return dynamicTags.getTags();
   }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

    public Optional<String> getDynamicLibraryName(Elf32DynamicTag neededTag) {
        Args.checkDynamicTagType(neededTag, Elf32DynamicTagType.NEEDED);

        Optional<Elf32DynamicTag> maybeStrTab = dynamicTags().stream()
                .filter(t -> t.type().is(Elf32DynamicTagType.STRTAB))
                .findFirst();

        return maybeStrTab
                .flatMap(strTab -> {
                    Elf32Address inMemAddress = strTab.address();
                    return elfFile().sectionContainingAddress(inMemAddress);
                })
                .filter(section -> section.header().type().is(ElfSectionType.STRING_TABLE))
                .map(section -> ((Elf32StringTableSection)section).stringTable())
                .map(st -> st.getStringAtIndex(new StringTableIndex(neededTag.value())));
    }
}

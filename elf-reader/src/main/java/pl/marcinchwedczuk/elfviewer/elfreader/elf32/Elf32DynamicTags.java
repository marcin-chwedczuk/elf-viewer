package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTagType.NULL;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;

public class Elf32DynamicTags {
    private final Elf32File elfFile;
    private final Elf32SectionHeader dynamicSection;
    private final TableHelper tableHelper;

    public Elf32DynamicTags(Elf32File elfFile,
                            Elf32SectionHeader dynamicSection) {
        requireNonNull(elfFile);
        requireNonNull(dynamicSection);

        Args.checkSectionType(dynamicSection, DYNAMIC);

        this.elfFile = elfFile;
        this.dynamicSection = dynamicSection;
        this.tableHelper = TableHelper.forSectionEntries(dynamicSection);
    }

    public List<Elf32DynamicTag> getTags() {
        Elf32Offset startOffset = dynamicSection.fileOffset();
        StructuredFile sf = new StructuredFile(elfFile, startOffset);

        List<Elf32DynamicTag> result = new ArrayList<>();

        for (int i = 0; i < tableHelper.tableSize(); i++) {
            Elf32DynamicTagType tag =
                    Elf32DynamicTagType.fromValue(sf.readUnsignedInt());

            // NULL tag marks end of the dynamic section
            if (tag.is(NULL)) break;

            int value = sf.readUnsignedInt();
            result.add(new Elf32DynamicTag(tag, value));
        }

        return result;
    }
}

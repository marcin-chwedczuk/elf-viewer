package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;
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

    public List<Elf32DynamicStructure> getTags() {
        Elf32Offset startOffset = dynamicSection.offsetInFile();
        StructuredFile sf = new StructuredFile(elfFile, startOffset);


        // Section ends with NULL entry
        List<Elf32DynamicStructure> result = new ArrayList<>();

        Set<Elf32DynamicArrayTag> tagsWithValue = Set.of(
                Elf32DynamicArrayTag.NEEDED,
                Elf32DynamicArrayTag.PLT_REL_SZ,
                Elf32DynamicArrayTag.RELA_SZ,
                Elf32DynamicArrayTag.RELA_ENT,
                Elf32DynamicArrayTag.STRSZ,
                Elf32DynamicArrayTag.SYMENT,
                Elf32DynamicArrayTag.SONAME,
                Elf32DynamicArrayTag.RPATH,
                Elf32DynamicArrayTag.RELSZ,
                Elf32DynamicArrayTag.RELENT,
                Elf32DynamicArrayTag.PLTREL,
                Elf32DynamicArrayTag.INIT_ARRAYSZ,
                Elf32DynamicArrayTag.FINI_ARRAYSZ
        );
        Set<Elf32DynamicArrayTag> tagsWithPtr = Set.of(
                Elf32DynamicArrayTag.PLT_GOT,
                Elf32DynamicArrayTag.HASH,
                Elf32DynamicArrayTag.STR_TAB,
                Elf32DynamicArrayTag.SYM_TAB,
                Elf32DynamicArrayTag.RELA,
                Elf32DynamicArrayTag.INIT,
                Elf32DynamicArrayTag.FINI,
                Elf32DynamicArrayTag.REL,
                Elf32DynamicArrayTag.DEBUG,
                Elf32DynamicArrayTag.JMPREL,
                Elf32DynamicArrayTag.INIT_ARRAY,
                Elf32DynamicArrayTag.FINI_ARRAY,
                Elf32DynamicArrayTag.GNU_HASH,
                Elf32DynamicArrayTag.VERNEED,
                Elf32DynamicArrayTag.VERSYM
        );

        for (int i = 0; i < tableHelper.tableSize(); i++) {
            Elf32DynamicArrayTag tag =
                    Elf32DynamicArrayTag.fromUnsignedInt(sf.readUnsignedInt());

            if (tagsWithValue.contains(tag)) {
                int value = sf.readUnsignedInt();
                result.add(new Elf32DynamicStructure(tag, value, null));
            } else if (tagsWithPtr.contains(tag)) {
                Elf32Address addr = sf.readAddress();
                result.add(new Elf32DynamicStructure(tag, null, addr));
            } else {
                // TODO: verify...
                sf.readUnsignedInt(); // drop data
                result.add(new Elf32DynamicStructure(tag, null, null));
            }
        }

        return result;
    }
}

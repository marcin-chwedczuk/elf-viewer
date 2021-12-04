package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTagType.NULL;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;

public class ElfDynamicTags<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final StructuredFileFactory<NATIVE_WORD> structuredFileFactory;

    private final ElfFile<NATIVE_WORD> elfFile;
    private final ElfSectionHeader<NATIVE_WORD> dynamicSection;
    private final TableHelper tableHelper;
    private final NativeWord<NATIVE_WORD> nativeWord;

    public ElfDynamicTags(NativeWord<NATIVE_WORD> nativeWord,
                          StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
            ElfFile<NATIVE_WORD> elfFile,
                            ElfSectionHeader<NATIVE_WORD> dynamicSection) {
        this.nativeWord = nativeWord;
        requireNonNull(elfFile);
        requireNonNull(dynamicSection);

        Args.checkSectionType(dynamicSection, DYNAMIC);

        this.structuredFileFactory = requireNonNull(structuredFileFactory);
        this.elfFile = elfFile;
        this.dynamicSection = dynamicSection;
        this.tableHelper = TableHelper.forSectionEntries(dynamicSection);
    }

    public List<ElfDynamicTag<NATIVE_WORD>> getTags() {
        ElfOffset<NATIVE_WORD> startOffset = dynamicSection.fileOffset();
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(elfFile, startOffset);

        List<ElfDynamicTag<NATIVE_WORD>> result = new ArrayList<>();

        for (int i = 0; i < tableHelper.tableSize(); i++) {
            Elf32DynamicTagType tag =
                    Elf32DynamicTagType.fromValue(sf.readUnsignedInt());

            // NULL tag marks end of the dynamic section
            if (tag.is(NULL)) break;

            NATIVE_WORD value = nativeWord.readFrom(sf);
            result.add(new ElfDynamicTag<NATIVE_WORD>(tag, value));
        }

        return result;
    }
}

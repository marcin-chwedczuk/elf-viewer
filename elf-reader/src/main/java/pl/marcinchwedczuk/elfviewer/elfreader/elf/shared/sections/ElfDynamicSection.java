package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;
import java.util.Optional;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;

public class ElfDynamicSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfDynamicSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                             ElfFile<NATIVE_WORD> elfFile,
                             ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, DYNAMIC);
   }

   public List<ElfDynamicTag<NATIVE_WORD>> dynamicTags() {
       ElfDynamicTags<NATIVE_WORD> dynamicTags = new ElfDynamicTags<NATIVE_WORD>(
               nativeWord,
               structuredFileFactory,
                elfFile(),
                header());

        return dynamicTags.getTags();
   }

    public Optional<String> getDynamicLibraryName(ElfDynamicTag<NATIVE_WORD> neededTag) {
        Args.checkDynamicTagType(neededTag, Elf32DynamicTagType.NEEDED);

        Optional<ElfDynamicTag<NATIVE_WORD>> maybeStrTab = dynamicTags().stream()
                .filter(t -> t.type().is(Elf32DynamicTagType.STRTAB))
                .findFirst();

        return maybeStrTab
                .flatMap(strTab -> {
                    ElfAddress<NATIVE_WORD> inMemAddress = strTab.address();
                    return elfFile().sectionContainingAddress(inMemAddress);
                })
                .filter(section -> section.header().type().is(ElfSectionType.STRING_TABLE))
                .map(section -> ((ElfStringTableSection<NATIVE_WORD>)section).stringTable())
                // TODO: intValue overflow?
                .map(st -> st.getStringAtIndex(new StringTableIndex(neededTag.value().intValue())));
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

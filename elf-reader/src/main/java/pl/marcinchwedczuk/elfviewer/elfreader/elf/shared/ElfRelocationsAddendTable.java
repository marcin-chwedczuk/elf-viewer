package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.RELA;

public class ElfRelocationsAddendTable<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final ElfSectionHeader<NATIVE_WORD> section;
    private final ElfFile<NATIVE_WORD> elfFile;

    private final TableHelper<NATIVE_WORD> tableHelper;
    private final NativeWord<NATIVE_WORD> nativeWord;
    private final StructuredFileFactory<NATIVE_WORD> structuredFileFactory;

    public ElfRelocationsAddendTable(NativeWord<NATIVE_WORD> nativeWord,
                                     StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                                     ElfSectionHeader<NATIVE_WORD> section,
                                     ElfFile<NATIVE_WORD> elfFile) {
        this.nativeWord = nativeWord;
        this.structuredFileFactory = structuredFileFactory;
        requireNonNull(section);
        requireNonNull(elfFile);

        // TODO: Check this condition
        Args.checkSectionType(section, RELA);

        this.section = section;
        this.elfFile = elfFile;

        tableHelper = TableHelper.forSectionEntries(section);
    }

    public int size() {
        return tableHelper.tableSize();
    }

    public ElfRelocationAddend<NATIVE_WORD> get(int index) {
        ElfOffset<NATIVE_WORD> startOffset = tableHelper.offsetForEntry(index);
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(elfFile, startOffset);

        ElfAddress<NATIVE_WORD> offset = sf.readAddress();
        NATIVE_WORD info = nativeWord.readNativeWordFrom(sf);
        NATIVE_WORD addend = nativeWord.readNativeWordFrom(sf);

        return nativeWord.mkRelocationA(offset, info, addend);
    }

    public Collection<ElfRelocationAddend<NATIVE_WORD>> relocations() {
        List<ElfRelocationAddend<NATIVE_WORD>> result = new ArrayList<>(size());

        for (int i = 0; i < size(); i++) {
            result.add(get(i));
        }

        return result;
    }
}

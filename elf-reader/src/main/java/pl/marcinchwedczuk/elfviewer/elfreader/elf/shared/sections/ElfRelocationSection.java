package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocation;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfRelocationsTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.REL;

public class ElfRelocationSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfRelocationSection(NativeWord<NATIVE_WORD> nativeWord,
                                StructuredFileFactory<NATIVE_WORD> structuredFileFactory, ElfFile<NATIVE_WORD> elfFile, ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, REL);
    }

    public List<ElfRelocation<NATIVE_WORD>> relocations() {
        ElfRelocationsTable<NATIVE_WORD> relocationsTable =
                new ElfRelocationsTable<NATIVE_WORD>(nativeWord, structuredFileFactory, header(), elfFile());

        return new ArrayList<>(relocationsTable.relocations());
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

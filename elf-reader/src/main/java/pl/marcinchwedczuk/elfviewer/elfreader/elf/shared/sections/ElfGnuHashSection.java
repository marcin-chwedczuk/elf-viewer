package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32GnuHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.GNU_HASH;

public class ElfGnuHashSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {

    public ElfGnuHashSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                             ElfFile<NATIVE_WORD> elfFile,
                             ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, GNU_HASH);
    }

    public ElfGnuHashTable<NATIVE_WORD> gnuHashTable() {
        // TODO: ElfEither<L,R> for error handling
        List<ElfSection<NATIVE_WORD>> dynsym = elfFile().sectionsOfType(DYNAMIC_SYMBOLS);
        if (dynsym.size() != 1)
            return null; // TODO: Meaningful error, case None and case More than one

        ElfSymbolTable<NATIVE_WORD> symbolTable = ((ElfSymbolTableSection<NATIVE_WORD>)dynsym.get(0)).symbolTable();

        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                elfFile(),
                header().fileOffset());

        int nbuckets = sf.readUnsignedInt();
        int symbolIndex = sf.readUnsignedInt();
        int maskWords = sf.readUnsignedInt();
        int shift2 = sf.readUnsignedInt();

        // TODO: Check boundary of section
        int[] bloomFilter = sf.readIntArray(maskWords);
        int[] buckets = sf.readIntArray(nbuckets);
        int[] hashValues = sf.readIntArray(symbolTable.size() - symbolIndex);

        if (sf.currentPositionInFile()
                .isAfter(header().sectionEndOffsetInFile()))
            throw new IllegalStateException("Read past section end.");

        return new ElfGnuHashTable<>(
                symbolTable,
                nbuckets,
                symbolIndex,
                maskWords,
                shift2,
                bloomFilter,
                buckets,
                hashValues);
    }
}

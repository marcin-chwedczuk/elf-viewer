package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.Arrays;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public class ElfHashSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
        extends ElfSection<NATIVE_WORD>
{
    public ElfHashSection(NativeWord<NATIVE_WORD> nativeWord,
                          StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                             ElfFile<NATIVE_WORD> elfFile,
                             ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, HASH);
    }

    public ElfHashTable<NATIVE_WORD> hashTable() {
        int symbolSectionIndex = header().link();
        ElfSymbolTableSection<NATIVE_WORD> symbolSection = elfFile().sections()
                .get(symbolSectionIndex)
                .asSymbolTableSection();

        ElfSymbolTable<NATIVE_WORD> symbolTable = symbolSection.symbolTable();

        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                elfFile(),
                header().fileOffset());

        int nbucket = sf.readUnsignedInt();
        int nchain = sf.readUnsignedInt();

        SymbolTableIndex[] buckets = Arrays.stream(sf.readIntArray(nbucket))
                .mapToObj(SymbolTableIndex::new)
                .toArray(SymbolTableIndex[]::new);

        SymbolTableIndex[] chains = Arrays.stream(sf.readIntArray(nchain))
                .mapToObj(SymbolTableIndex::new)
                .toArray(SymbolTableIndex[]::new);

        return new ElfHashTable<>(
                nbucket,
                nchain,
                buckets,
                chains,
                symbolTable);
    }
}

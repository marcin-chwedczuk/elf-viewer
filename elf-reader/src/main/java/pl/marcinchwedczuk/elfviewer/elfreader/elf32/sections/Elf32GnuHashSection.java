package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.Optional;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.GNU_HASH;

public class Elf32GnuHashSection extends Elf32Section {
    public Elf32GnuHashSection(Elf32File elfFile, Elf32SectionHeader header) {
        super(elfFile, header);

        Args.checkSectionType(header, GNU_HASH);
    }

    public Elf32GnuHashTable gnuHashTable() {
        // TODO: ElfEither<L,R> for error handling
        Optional<Elf32Section> dynsym = elfFile().findSection(DYNAMIC_SYMBOLS);
        if (dynsym.isEmpty())
            return null; // TODO: Meaningful error

        SymbolTable symbolTable = ((Elf32SymbolTableSection)dynsym.get()).symbolTable();

        StructuredFile sf = new StructuredFile(
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

        return new Elf32GnuHashTable(
                symbolTable,
                nbuckets,
                symbolIndex,
                maskWords,
                shift2,
                bloomFilter,
                buckets,
                hashValues);
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

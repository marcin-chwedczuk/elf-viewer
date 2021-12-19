package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfSymbolVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SymbolTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.GNU_VERSION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.GNU_VERSYM;

public class ElfGnuVersionSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfGnuVersionSection(NativeWord<NATIVE_WORD> nativeWord,
                                StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                                ElfFile<NATIVE_WORD> elfFile,
                                ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        if (!header.type().is(GNU_VERSYM))
            throw new IllegalArgumentException("Invalid section name: " + header.name() + ".");
    }

    public List<ElfSymbolVersion> symbolVersions() {
        List<ElfSymbolVersion> result = new ArrayList<>();

        NATIVE_WORD numberOfEntries = nativeWord.divideExact(
                header().size(),
                header().containedEntrySize());

        SymbolTableIndex relatedSymbolTableIndex = new SymbolTableIndex(header().link());
        if (!relatedSymbolTableIndex.isUndefined()) {
            ElfSection<NATIVE_WORD> relatedSymbolTableSection =
                    elfFile().sections().get(relatedSymbolTableIndex.intValue());

            if (!(relatedSymbolTableSection instanceof ElfSymbolTableSection<?>)) {
                throw new ElfReaderException(String.format(
                        "Section '%s' is not properly linked (via link field) to a symbol table.", GNU_VERSION));
            }

            int numberOfSymbols = relatedSymbolTableSection.asSymbolTableSection()
                    .symbolTable()
                    .size();

            if (numberOfEntries.longValue() != numberOfSymbols) {
                throw new ElfReaderException(String.format(
                        "Section '%s' contains different number of symbols than associated symbol table '%s'. " +
                                "Number of symbol versions is %d and number of symbols is %d.",
                        GNU_VERSION, relatedSymbolTableSection.name(),
                        numberOfEntries.longValue(), numberOfSymbols));
            }
        }

        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                contents(), elfFile().endianness(), 0);

        for (int i = 0; i < numberOfEntries.longValue(); i++) {
            ElfSymbolVersion sv = ElfSymbolVersion.fromValue(sf.readUnsignedShort());
            result.add(sv);
        }

        return result;
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

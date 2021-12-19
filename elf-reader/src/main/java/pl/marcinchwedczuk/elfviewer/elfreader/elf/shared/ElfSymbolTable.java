package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolType.SECTION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.SYMBOL_TABLE;

public class ElfSymbolTable<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final ElfFile<NATIVE_WORD> elfFile;
    private final ElfSectionHeader<NATIVE_WORD> section;
    private final ElfStringTable<NATIVE_WORD> symbolNames;

    private final TableHelper<NATIVE_WORD> tableHelper;
    private final NativeWord<NATIVE_WORD> nativeWord;
    private final StructuredFileFactory<NATIVE_WORD> structuredFileFactory;

    public ElfSymbolTable(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                          ElfFile<NATIVE_WORD> elfFile,
                          ElfSectionHeader<NATIVE_WORD> section,
                          ElfStringTable<NATIVE_WORD> symbolNames) {
        this.nativeWord = nativeWord;
        this.structuredFileFactory = structuredFileFactory;
        requireNonNull(elfFile);
        requireNonNull(section);
        requireNonNull(symbolNames);

        // TODO: Check this condition
        Args.checkSectionType(section, SYMBOL_TABLE, DYNAMIC_SYMBOLS);

        this.elfFile = elfFile;
        this.section = section;
        this.symbolNames = symbolNames;

        tableHelper = TableHelper.forSectionEntries(section);
    }

    public int size() {
        return tableHelper.tableSize();
    }

    public ElfSymbol<NATIVE_WORD> get(SymbolTableIndex index) {
        ElfOffset<NATIVE_WORD> startOffset = tableHelper.offsetForEntry(index);
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(elfFile, startOffset);

        // Order of fields in 32 and 64 bit version is different due to alignment constraints
        switch (nativeWord.type()) {
            case NATIVE_32:
                return readSymbol32(sf);

            case NATIVE_64:
                return readSymbol64(sf);

            default:
                throw new AssertionError();
        }
    }

    private ElfSymbol<NATIVE_WORD> readSymbol32(StructuredFile<NATIVE_WORD> sf) {
        StringTableIndex nameIndex = new StringTableIndex(sf.readUnsignedInt());
        ElfAddress<NATIVE_WORD> value = sf.readAddress();
        NATIVE_WORD size = nativeWord.readNativeWordFrom(sf);
        byte info = sf.readByte();
        byte other = sf.readByte();
        SectionHeaderIndex symbolIndex = new SectionHeaderIndex(sf.readUnsignedShort());

        String name = isSectionSymbol(nameIndex, ElfSymbolType.fromSymbolInfo(info))
                // TODO: Add boundary check, validate logic with readelf source code
                ? elfFile.sectionHeaders().get(symbolIndex.intValue()).name()
                : symbolNames.getStringAtIndex(nameIndex);

        return new ElfSymbol<>(
                nameIndex,
                name,
                value,
                size,
                info,
                other,
                symbolIndex);
    }

    private ElfSymbol<NATIVE_WORD> readSymbol64(StructuredFile<NATIVE_WORD> sf) {
        StringTableIndex nameIndex = new StringTableIndex(sf.readUnsignedInt());
        byte info = sf.readByte();
        byte other = sf.readByte();
        SectionHeaderIndex symbolIndex = new SectionHeaderIndex(sf.readUnsignedShort());
        ElfAddress<NATIVE_WORD> value = sf.readAddress();
        NATIVE_WORD size = nativeWord.readNativeWordFrom(sf);

        String name = isSectionSymbol(nameIndex, ElfSymbolType.fromSymbolInfo(info))
                // TODO: Add boundary check, validate logic with readelf source code
                ? elfFile.sectionHeaders().get(symbolIndex.intValue()).name()
                : symbolNames.getStringAtIndex(nameIndex);

        return new ElfSymbol<>(
                nameIndex,
                name,
                value,
                size,
                info,
                other,
                symbolIndex);
    }

    public Optional<ElfSymbol<NATIVE_WORD>> slowlyFindSymbolByName(String name) {
        for (int i = 0; i < size(); i++) {
            ElfSymbol<NATIVE_WORD> symbol = get(new SymbolTableIndex(i));

            if (name.equals(symbol.name())) {
                return Optional.of(symbol);
            }
        }

        return Optional.empty();
    }

    private boolean isSectionSymbol(StringTableIndex nameIndex, ElfSymbolType type) {
        return (nameIndex.intValue() == 0) && type.is(SECTION);
    }

    public Collection<ElfSymbolTableEntry<NATIVE_WORD>> symbols() {
        List<ElfSymbolTableEntry<NATIVE_WORD>> result = new ArrayList<>();

        for (int i = 0; i < size(); i++) {
            SymbolTableIndex index = new SymbolTableIndex(i);
            ElfSymbol<NATIVE_WORD> symbol = get(index);

            // TODO: Handle special cases like ABSOLUTE
            ElfSectionHeader<NATIVE_WORD> relatedSection =
                    symbol.index().isSpecial()
                            ? null
                            : elfFile.sectionHeaders().get(symbol.index().intValue());

            result.add(new ElfSymbolTableEntry<>(
                    index,
                    get(index),
                    relatedSection));
        }

        return result;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.SYMBOL_TABLE;

public class SymbolTable {
    private final AbstractFile file;
    private final Endianness endianness;
    private final Elf32SectionHeader section;
    private final StringTable symbolNames;
    private final Elf32File elf32File;

    private final TableHelper tableHelper;

    public SymbolTable(Elf32File elf32File,
                       Elf32SectionHeader section,
                       StringTable symbolNames)
    {
        requireNonNull(elf32File);
        requireNonNull(section);
        requireNonNull(symbolNames);

        // TODO: Check this condition
        Args.checkSectionType(section, SYMBOL_TABLE, DYNAMIC_SYMBOLS);

        this.file = elf32File.storage;
        this.endianness = elf32File.endianness;
        this.section = section;
        this.symbolNames = symbolNames;
        this.elf32File = elf32File;

        tableHelper = TableHelper.forSectionEntries(section);
    }

    public int size() {
        return section.sectionSize() / section.containedEntrySize();
    }

    public Elf32Symbol get(SymbolTableIndex index) {
        Elf32Offset startOffset = tableHelper.offsetForEntry(index);
        StructuredFile symbolFile = new StructuredFile(file, endianness, startOffset);

        StringTableIndex nameIndex = new StringTableIndex(symbolFile.readUnsignedInt());

        Elf32Address value = symbolFile.readAddress();
        int size = symbolFile.readUnsignedInt();
        byte info = symbolFile.readByte();
        byte other = symbolFile.readByte();
        SectionHeaderIndex symbolIndex = new SectionHeaderIndex(symbolFile.readUnsignedShort());

        String name = isSectionSymbol(nameIndex, Elf32SymbolType.fromSymbolInfo(info))
                // TODO: Add boundary check, validate logic with readelf source code
                ? elf32File.sectionHeaders.get(symbolIndex.intValue()).name()
                : symbolNames.getStringAtIndex(nameIndex);

        return new Elf32Symbol(
                nameIndex,
                name,
                value,
                size,
                info,
                other,
                symbolIndex);
    }

    public Optional<Elf32Symbol> slowlyFindSymbolByName(String name) {
        for (int i = 0; i < size(); i++) {
            Elf32Symbol symbol = get(new SymbolTableIndex(i));

            if (name.equals(symbol.name())) {
                return Optional.of(symbol);
            }
        }

        return Optional.empty();
    }

    private boolean isSectionSymbol(StringTableIndex nameIndex, Elf32SymbolType type) {
        return (nameIndex.intValue() == 0 && type.equals(Elf32SymbolType.Section));
    }
}

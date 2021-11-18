package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolType.SECTION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.SYMBOL_TABLE;

public class SymbolTable {
    private final Elf32File elfFile;
    private final Elf32SectionHeader section;
    private final StringTable symbolNames;

    private final TableHelper tableHelper;

    public SymbolTable(Elf32File elfFile,
                       Elf32SectionHeader section,
                       StringTable symbolNames)
    {
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

    public Elf32Symbol get(SymbolTableIndex index) {
        Elf32Offset startOffset = tableHelper.offsetForEntry(index);
        StructuredFile sf = new StructuredFile(elfFile, startOffset);

        StringTableIndex nameIndex = new StringTableIndex(sf.readUnsignedInt());
        Elf32Address value = sf.readAddress();
        int size = sf.readUnsignedInt();
        byte info = sf.readByte();
        byte other = sf.readByte();
        SectionHeaderIndex symbolIndex = new SectionHeaderIndex(sf.readUnsignedShort());

        String name = isSectionSymbol(nameIndex, Elf32SymbolType.fromSymbolInfo(info))
                // TODO: Add boundary check, validate logic with readelf source code
                ? elfFile.sectionHeaders.get(symbolIndex.intValue()).name()
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
        return (nameIndex.intValue() == 0) && type.is(SECTION);
    }
}

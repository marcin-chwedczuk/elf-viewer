package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfSymbolVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionDefinition;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionDefinitionAuxiliary;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionDefinitionRevision;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.GNU_VERDEF;

public class ElfGnuVersionDefinitionsSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfGnuVersionDefinitionsSection(NativeWord<NATIVE_WORD> nativeWord,
                                           StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                                           ElfFile<NATIVE_WORD> elfFile,
                                           ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        if (!header.type().is(GNU_VERDEF))
            throw new IllegalArgumentException("Invalid section name: " + header.name() + ".");
    }


    public List<ElfVersionDefinition<NATIVE_WORD>> definitions() {
        List<ElfVersionDefinition<NATIVE_WORD>> result = new ArrayList<>();

        ElfStringTable<NATIVE_WORD> associatedStringTable = associatedStringTable();

        // TODO: Get number of entries from .dynamic section DT_VERNEEDNUM entry
        ElfOffset<NATIVE_WORD> currentOffset = nativeWord.zeroOffset();
        while (true) {
            StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                    contents(), elfFile().endianness(), currentOffset);

            ElfVersionDefinitionRevision version = ElfVersionDefinitionRevision.fromValue(sf.readUnsignedShort());
            short flags = sf.readUnsignedShort();
            ElfSymbolVersion versionIndex = ElfSymbolVersion.fromValue(sf.readUnsignedShort());
            short numberAE = sf.readUnsignedShort();
            int nameHash = sf.readUnsignedInt();
            int offsetAux = sf.readUnsignedInt();
            int offsetNext = sf.readUnsignedInt();

            List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> auxiliaryEntries =
                    readAuxiliaryEntries(currentOffset, offsetAux, associatedStringTable);

            result.add(new ElfVersionDefinition<NATIVE_WORD>(
                    version,
                    flags,
                    versionIndex,
                    numberAE,
                    nameHash,
                    offsetAux,
                    offsetNext,
                    auxiliaryEntries));

            if (offsetNext == 0) break;
            currentOffset = currentOffset.plus(offsetNext);
        }

        return result;
    }

    private List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> readAuxiliaryEntries(
            ElfOffset<NATIVE_WORD> currentOffset,
            int offsetAux,
            ElfStringTable<NATIVE_WORD> associatedStringTable)
    {
        if (offsetAux == 0) {
            return List.of();
        }

        List<ElfVersionDefinitionAuxiliary<NATIVE_WORD>> result = new ArrayList<>();

        currentOffset = currentOffset.plus(offsetAux);
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                contents(), elfFile().endianness(), currentOffset);
        while (true) {
            StringTableIndex nameIndex = new StringTableIndex(sf.readUnsignedInt());
            String name = associatedStringTable.getStringAtIndex(nameIndex);
            int nextOffset = sf.readUnsignedInt();

            result.add(new ElfVersionDefinitionAuxiliary<NATIVE_WORD>(
                    nameIndex,
                    name,
                    nextOffset));

            if (nextOffset == 0) break;
            currentOffset = currentOffset.plus(nextOffset);
        }

        return result;
    }

    private ElfStringTable<NATIVE_WORD> associatedStringTable() {
        ElfStringTableSection<NATIVE_WORD> stringTableSection = elfFile()
                .sections()
                .get(header().link())
                .asStringTableSection();

        return stringTableSection.stringTable();
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

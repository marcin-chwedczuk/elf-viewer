package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfSymbolVersion;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionNeededAuxiliaryEntry;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionNeededEntry;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionNeeded;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.StringTableIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.GNU_VERNEED;

/**
 * This section describes the version information used by
 * the undefined versioned symbols in the module.
 */
public class ElfGnuVersionRequirementsSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfGnuVersionRequirementsSection(NativeWord<NATIVE_WORD> nativeWord,
                                            StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                                            ElfFile<NATIVE_WORD> elfFile,
                                            ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        if (!header.type().is(GNU_VERNEED))
            throw new IllegalArgumentException("Invalid section type: " + header.type() + ".");
    }

    public List<ElfVersionNeededEntry<NATIVE_WORD>> requirements() {
        List<ElfVersionNeededEntry<NATIVE_WORD>> result = new ArrayList<>();

        ElfStringTable<NATIVE_WORD> associatedStringTable = associatedStringTable();

        // TODO: Get number of entries from .dynamic section DT_VERNEEDNUM entry
        ElfOffset<NATIVE_WORD> currentOffset = nativeWord.zeroOffset();
        while (true) {
            StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                    contents(), elfFile().endianness(), currentOffset);

            ElfVersionNeeded version = ElfVersionNeeded.fromValue(sf.readUnsignedShort());
            short numberAE = sf.readUnsignedShort();
            StringTableIndex fileNameIdx = new StringTableIndex(sf.readUnsignedInt());
            String fileName = associatedStringTable.getStringAtIndex(fileNameIdx);
            int offsetAux = sf.readUnsignedInt();
            int offsetNext = sf.readUnsignedInt();

            List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> auxiliaryEntries =
                    readAuxiliaryEntries(currentOffset, offsetAux, associatedStringTable);

            result.add(new ElfVersionNeededEntry<NATIVE_WORD>(
                    version,
                    numberAE,
                    fileNameIdx,
                    fileName,
                    offsetAux,
                    offsetNext, auxiliaryEntries));

            if (offsetNext == 0) break;
            currentOffset = currentOffset.plus(offsetNext);
        }

        return result;
    }

    private List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> readAuxiliaryEntries(
            ElfOffset<NATIVE_WORD> currentOffset,
            int offsetAux,
            ElfStringTable<NATIVE_WORD> associatedStringTable)
    {
        if (offsetAux == 0) {
            return List.of();
        }

        List<ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>> result = new ArrayList<>();

        currentOffset = currentOffset.plus(offsetAux);
        StructuredFile<NATIVE_WORD> sf = structuredFileFactory.mkStructuredFile(
                contents(), elfFile().endianness(), currentOffset);
        while (true) {
            int hash = sf.readUnsignedInt();
            short flags = sf.readUnsignedShort();
            ElfSymbolVersion other = ElfSymbolVersion.fromValue(sf.readUnsignedShort());
            StringTableIndex nameIndex = new StringTableIndex(sf.readUnsignedInt());
            String name = associatedStringTable.getStringAtIndex(nameIndex);
            int nextOffset = sf.readUnsignedInt();

            result.add(new ElfVersionNeededAuxiliaryEntry<NATIVE_WORD>(
                    hash,
                    flags,
                    other,
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

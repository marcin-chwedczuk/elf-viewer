package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.BigEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.NOTE;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.STRINGS;

public class ElfReader {
    private ElfReader() { }

    public static Elf32File readElf(AbstractFile file) {
        byte[] identificationBytes = file.read(0, ElfIdentificationIndexes.EI_NIDENT);
        ElfIdentification identification = ElfIdentification.parseBytes(identificationBytes);

        Endianness endianness =
                identification.elfData().is(ElfData.ELF_DATA_LSB) ? new LittleEndian() :
                identification.elfData().is(ElfData.ELF_DATA_MSB) ? new BigEndian() :
                throwElfReaderException("Unrecognised data encoding: %s.", identification.elfData());

        final int startOffset = ElfIdentificationIndexes.EI_NIDENT;
        // TODO: Add path for 64-bits
        Elf32Header header = readElf32Header(identification, new StructuredFile(file, endianness, startOffset));

        List<Elf32SectionHeader> sectionHeaders = readElf32SectionHeaders(
                file,
                endianness,
                header.sectionContainingSectionNames(),
                TableHelper.forSectionHeaders(header));

        List<Elf32ProgramHeader> programHeaders = readElf32ProgramHeaders(
                file,
                endianness,
                TableHelper.forProgramHeaders(header));

        return new Elf32File(
                file,
                endianness,
                header,
                sectionHeaders,
                programHeaders);
    }

    private static List<Elf32ProgramHeader> readElf32ProgramHeaders(
            AbstractFile file,
            Endianness endianness,
            TableHelper programHeadersTable)
    {
        List<Elf32ProgramHeader> headers = new ArrayList<>(programHeadersTable.tableSize());

        for (int i = 0; i < programHeadersTable.tableSize(); i++) {
            Elf32Offset offset = programHeadersTable.offsetForEntry(i);
            StructuredFile headerFile = new StructuredFile(file, endianness, offset);

            Elf32ProgramHeader sectionHeader = readElf32ProgramHeader(headerFile);
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static Elf32ProgramHeader readElf32ProgramHeader(StructuredFile headerFile) {
        Elf32SegmentType type = Elf32SegmentType.fromValue(headerFile.readUnsignedInt());
        Elf32Offset fileOffset = headerFile.readOffset();
        Elf32Address virtualAddress = headerFile.readAddress();
        Elf32Address physicalAddress = headerFile.readAddress();
        int fileSize = headerFile.readUnsignedInt();
        int memorySize = headerFile.readUnsignedInt();
        Elf32SegmentFlags flags = new Elf32SegmentFlags(headerFile.readUnsignedInt());
        int alignment = headerFile.readUnsignedInt();

        return new Elf32ProgramHeader(
                type,
                fileOffset,
                virtualAddress,
                physicalAddress,
                fileSize,
                memorySize,
                flags,
                alignment);
    }

    public static Elf32Header readElf32Header(ElfIdentification identification, StructuredFile elfHeaderFile) {
        ElfType type = ElfType.fromValue(elfHeaderFile.readUnsignedShort());
        ElfMachine machine = ElfMachine.fromValue(elfHeaderFile.readUnsignedShort());

        // TODO: Overflow check
        ElfVersion version = ElfVersion.fromValue((byte)elfHeaderFile.readUnsignedInt());

        Elf32Address entry = elfHeaderFile.readAddress();
        Elf32Offset programHeaderTableOffset = elfHeaderFile.readOffset();
        Elf32Offset sectionHeaderTableOffset = elfHeaderFile.readOffset();

        int flags = elfHeaderFile.readUnsignedInt();
        short elfHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderCount = elfHeaderFile.readUnsignedShort();
        short sectionHeaderSize = elfHeaderFile.readUnsignedShort();
        short sectionHeaderCount = elfHeaderFile.readUnsignedShort();
        short e_shstrndx = elfHeaderFile.readUnsignedShort();

        SectionHeaderIndex sectionNamesStringTableIndex = new SectionHeaderIndex(e_shstrndx);

        return new Elf32Header(
                identification,
                type,
                machine,
                version,
                entry,
                programHeaderTableOffset,
                sectionHeaderTableOffset,
                flags,
                elfHeaderSize,
                programHeaderSize,
                programHeaderCount,
                sectionHeaderSize,
                sectionHeaderCount,
                sectionNamesStringTableIndex);
    }

    private static List<Elf32SectionHeader> readElf32SectionHeaders(AbstractFile file,
                                                                    Endianness endianness,
                                                                    SectionHeaderIndex sectionHeaderTable,
                                                                    TableHelper sectionHeaders) {
        StringTable sectionNames = loadSectionsNameStringTable(
                file, endianness, sectionHeaderTable, sectionHeaders);

        List<Elf32SectionHeader> headers = new ArrayList<>(sectionHeaders.tableSize());

        for (int i = 0; i < sectionHeaders.tableSize(); i++) {
            Elf32Offset offset = sectionHeaders.offsetForEntry(new SectionHeaderIndex(i));
            StructuredFile headerFile = new StructuredFile(file, endianness, offset);

            Elf32SectionHeader sectionHeader = readElf32SectionHeader(headerFile, Optional.of(sectionNames));
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static StringTable loadSectionsNameStringTable(AbstractFile file,
                                                           Endianness endianness,
                                                           SectionHeaderIndex sectionNamesSection,
                                                           TableHelper sectionHeaderTable) {
        Elf32Offset offset = sectionHeaderTable.offsetForEntry(sectionNamesSection);
        StructuredFile headerFile = new StructuredFile(file, endianness, offset);
        Elf32SectionHeader sectionNamesStringTableSection = readElf32SectionHeader(
                headerFile, Optional.empty());
        return new StringTable(file, sectionNamesStringTableSection);
    }

    private static Elf32SectionHeader readElf32SectionHeader(
            StructuredFile headerFile,
            Optional<StringTable> maybeSectionNames)
    {
        StringTableIndex sectionNameIndex = new StringTableIndex(headerFile.readUnsignedInt());
        ElfSectionType type = ElfSectionType.fromValue(headerFile.readUnsignedInt());
        SectionAttributes flags = new SectionAttributes(headerFile.readUnsignedInt());
        Elf32Address inMemoryAddress = headerFile.readAddress();
        Elf32Offset offsetInFile = headerFile.readOffset();
        int sectionSize = headerFile.readUnsignedInt();
        int link = headerFile.readUnsignedInt();
        int info = headerFile.readUnsignedInt();
        int addressAlignment = headerFile.readUnsignedInt();
        int containedEntrySize = headerFile.readUnsignedInt();

        String sectionName = maybeSectionNames
                .map(st -> st.getStringAtIndex(sectionNameIndex))
                .orElse("(not-resolved)");

        return new Elf32SectionHeader(
                sectionNameIndex,
                sectionName,
                type,
                flags,
                inMemoryAddress,
                offsetInFile,
                sectionSize,
                link,
                info,
                addressAlignment,
                containedEntrySize);
    }


    public static List<Elf32Note> readNotes(Elf32File elfFile, String sectionName) {
        Elf32SectionHeader noteSection = elfFile.getSectionHeader(sectionName)
                .orElseThrow(() ->
                    new IllegalArgumentException("Section with name " + sectionName + " not found."));

        if (noteSection.type().isNot(NOTE))
            throw new IllegalArgumentException("Invalid section type.");

        Elf32Offset startOffset = noteSection.fileOffset();
        Elf32Offset endOffset = startOffset.plus(noteSection.size());

        List<Elf32Note> notes = new ArrayList<>();
        Elf32Offset curr = startOffset;
        while (curr.isBefore(endOffset)) {
            StructuredFile sf = new StructuredFile(elfFile, curr);

            int nameLen = sf.readUnsignedInt();
            int descLen = sf.readUnsignedInt();
            int type = sf.readUnsignedInt();

            String name = sf.readFixedSizeStringWithAlignment(nameLen, 4);
            byte[] descriptor = sf.readFixedSizeByteArrayWithAlignment(descLen, 4);

            if ("GNU".equals(name)) {
                notes.add(Elf32NoteGnu.createGnuNote(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            } else {
                notes.add(new Elf32Note(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            }

            curr = sf.currentPositionInFile();
        }

        return notes;
    }

    public static Optional<Elf32DynamicTags> readDynamicSection2(Elf32File elfFile) {
        return elfFile
                .getSectionHeader(ElfSectionNames.DYNAMIC)
                .map(sh -> new Elf32DynamicTags(elfFile, sh));
    }

    public static Elf32GnuHashTable readGnuHashSection(Elf32File file,
                                                       Elf32SectionHeader gnuHashSection,
                                                       SymbolTable dynsym) {
        // TODO: Check section types

        StructuredFile sf = new StructuredFile(file, gnuHashSection.fileOffset());

        int nbuckets = sf.readUnsignedInt();
        int symbolIndex = sf.readUnsignedInt();
        int maskWords = sf.readUnsignedInt();
        int shift2 = sf.readUnsignedInt();

        // TODO: Check boundary of section
        int[] bloomFilter = sf.readIntArray(maskWords);
        int[] buckets = sf.readIntArray(nbuckets);
        int[] hashValues = sf.readIntArray(dynsym.size() - symbolIndex);

        if (sf.currentPositionInFile()
                .isAfter(gnuHashSection.sectionEndOffsetInFile()))
            throw new IllegalStateException("Read past section end.");

        return new Elf32GnuHashTable(
                dynsym,
                nbuckets,
                symbolIndex,
                maskWords,
                shift2,
                bloomFilter,
                buckets,
                hashValues);
    }

    private static <T> T throwElfReaderException(String message) {
        throw new RuntimeException(String.format("%s", message));
    }
    private static <T> T throwElfReaderException(String format, Object... args) {
        throw new RuntimeException(String.format(format, args));
    }

    public static Collection<String> readStringsSection(
            Elf32File file,
            Elf32SectionHeader section) {
        // TODO: Add asserts

        if (!section.flags().hasFlag(STRINGS))
            throw new ElfReaderException("Section " + section.name() + " does not contain strings.");

        StructuredFile sf = new StructuredFile(
                file,
                section.fileOffset());

        // TODO: Handle reading past section end - StructuredFile should support
        // start and end offsets
        List<String> result = new ArrayList<>();
        while (sf.currentPositionInFile().isBefore(section.sectionEndOffsetInFile())) {
            String s = sf.readStringNullTerminated();
            result.add(s);
        }
        return result;
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.IntegerNativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.LongNativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSectionFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegmentFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.*;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.BigEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElfReader {
    private ElfReader() { }

    public static ElfFile<Integer> readElf32(AbstractFile file) {
        return readElf(file).asElf32();
    }

    public static ElfFile<Long> readElf64(AbstractFile file) {
        return readElf(file).asElf64();
    }

    public static ElfFile<?> readElf(AbstractFile file) {
        byte[] identificationBytes = file.read(0, ElfIdentificationIndexes.EI_NIDENT);
        ElfIdentification identification = ElfIdentification.parseBytes(identificationBytes);

        final int startOffset = ElfIdentificationIndexes.EI_NIDENT;

        ElfClass elfClass = identification.elfClass();
        if (elfClass.is(ElfClass.ELF_CLASS_32)) {
            return readElf32(file, identification, startOffset);
        } else if (elfClass.is(ElfClass.ELF_CLASS_64)) {
            return readElf64(file, identification, startOffset);
        } else {
            throw new ElfReaderException("Unrecognized ELF class: " + elfClass + ".");
        }
    }

    private static Endianness elfEndianness(ElfIdentification identification) {
        Endianness endianness =
                identification.elfData().is(ElfData.ELF_DATA_LSB) ? new LittleEndian() :
                identification.elfData().is(ElfData.ELF_DATA_MSB) ? new BigEndian() :
                throwElfReaderException("Unrecognised data encoding: %s.", identification.elfData());
        return endianness;
    }

    private static ElfFile<Long> readElf64(AbstractFile file,
                                       ElfIdentification identification,
                                       int startOffset) {
        Endianness endianness = elfEndianness(identification);

        ElfHeader<Long> header = readElf64Header(identification,
                new StructuredFile64(file, endianness, startOffset));

        List<ElfSectionHeader<Long>> sectionHeaders = readElf64SectionHeaders(
                file,
                endianness,
                header.sectionContainingSectionNames(),
                TableHelper.forSectionHeaders(header));

        List<ElfProgramHeader<Long>> programHeaders = readElf64ProgramHeaders(
                file,
                endianness,
                TableHelper.forProgramHeaders(header));

        return new ElfFile<>(new LongNativeWord(), file, endianness, header,
                sectionHeaders,
                new ElfSectionFactory<>(new LongNativeWord(), new StructuredFileFactory64()),
                programHeaders,
                new ElfSegmentFactory<>());
    }

    public static ElfHeader<Long> readElf64Header(
            ElfIdentification identification,
            StructuredFile64 elfHeaderFile) {
        ElfType type = ElfType.fromValue(elfHeaderFile.readUnsignedShort());
        ElfMachine machine = ElfMachine.fromValue(elfHeaderFile.readUnsignedShort());

        // TODO: Overflow check
        ElfVersion version = ElfVersion.fromValue((byte)elfHeaderFile.readUnsignedInt());

        Elf64Address entry = elfHeaderFile.readAddress64();
        Elf64Offset programHeaderTableOffset = elfHeaderFile.readOffset64();
        Elf64Offset sectionHeaderTableOffset = elfHeaderFile.readOffset64();

        int flags = elfHeaderFile.readUnsignedInt();
        short elfHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderCount = elfHeaderFile.readUnsignedShort();
        short sectionHeaderSize = elfHeaderFile.readUnsignedShort();
        short sectionHeaderCount = elfHeaderFile.readUnsignedShort();
        short e_shstrndx = elfHeaderFile.readUnsignedShort();

        SectionHeaderIndex sectionNamesStringTableIndex = new SectionHeaderIndex(e_shstrndx);

        return new ElfHeader<>(
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


    private static List<ElfSectionHeader<Long>> readElf64SectionHeaders(AbstractFile file,
                                                                    Endianness endianness,
                                                                    SectionHeaderIndex sectionHeaderTable,
                                                                    TableHelper<Long> sectionHeaders) {
        ElfStringTable<Long> sectionNames = loadSectionsNameStringTable64(
                file, endianness, sectionHeaderTable, sectionHeaders);

        List<ElfSectionHeader<Long>> headers = new ArrayList<>(sectionHeaders.tableSize());

        for (int i = 0; i < sectionHeaders.tableSize(); i++) {
            ElfOffset<Long> offset = sectionHeaders.offsetForEntry(new SectionHeaderIndex(i));
            StructuredFile64 headerFile = new StructuredFile64(file, endianness, offset);

            ElfSectionHeader<Long> sectionHeader = readElf64SectionHeader(headerFile, Optional.of(sectionNames));
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static ElfStringTable<Long> loadSectionsNameStringTable64(AbstractFile file,
                                                                  Endianness endianness,
                                                                  SectionHeaderIndex sectionNamesSection,
                                                                  TableHelper<Long> sectionHeaderTable) {
        ElfOffset<Long> offset = sectionHeaderTable.offsetForEntry(sectionNamesSection);
        StructuredFile64 headerFile = new StructuredFile64(file, endianness, offset);
        ElfSectionHeader<Long> sectionNamesStringTableSection = readElf64SectionHeader(
                headerFile, Optional.empty());
        return new ElfStringTable<>(file, sectionNamesStringTableSection);
    }

    private static ElfSectionHeader<Long> readElf64SectionHeader(
            StructuredFile64 headerFile,
            Optional<ElfStringTable<Long>> maybeSectionNames)
    {
        StringTableIndex sectionNameIndex = new StringTableIndex(headerFile.readUnsignedInt());
        ElfSectionType type = ElfSectionType.fromValue(headerFile.readUnsignedInt());
        SectionAttributes flags = new SectionAttributes(headerFile.readUnsignedLong());
        Elf64Address inMemoryAddress = headerFile.readAddress();
        Elf64Offset offsetInFile = headerFile.readOffset();
        long sectionSize = headerFile.readUnsignedLong();
        int link = headerFile.readUnsignedInt();
        int info = headerFile.readUnsignedInt();
        long addressAlignment = headerFile.readUnsignedLong();
        long containedEntrySize = headerFile.readUnsignedLong();

        String sectionName = maybeSectionNames
                .map(st -> st.getStringAtIndex(sectionNameIndex))
                .orElse("(not-resolved)");

        return new ElfSectionHeader<>(
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

    private static List<ElfProgramHeader<Long>> readElf64ProgramHeaders(
            AbstractFile file,
            Endianness endianness,
            TableHelper<Long> programHeadersTable)
    {
        List<ElfProgramHeader<Long>> headers = new ArrayList<>(programHeadersTable.tableSize());

        for (int i = 0; i < programHeadersTable.tableSize(); i++) {
            ElfOffset<Long> offset = programHeadersTable.offsetForEntry(i);
            StructuredFile64 headerFile = new StructuredFile64(file, endianness, offset);

            ElfProgramHeader<Long> sectionHeader = readElf64ProgramHeader(headerFile);
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static ElfProgramHeader<Long> readElf64ProgramHeader(StructuredFile64 headerFile) {
        ElfSegmentType type = ElfSegmentType.fromValue(headerFile.readUnsignedInt());
        ElfSegmentFlags flags = new ElfSegmentFlags(headerFile.readUnsignedInt());
        Elf64Offset fileOffset = headerFile.readOffset();
        Elf64Address virtualAddress = headerFile.readAddress();
        Elf64Address physicalAddress = headerFile.readAddress();
        long fileSize = headerFile.readUnsignedLong();
        long memorySize = headerFile.readUnsignedLong();
        long alignment = headerFile.readUnsignedLong();

        return new ElfProgramHeader<>(
                type,
                fileOffset,
                virtualAddress,
                physicalAddress,
                fileSize,
                memorySize,
                flags,
                alignment);
    }


    private static ElfFile<Integer> readElf32(AbstractFile file,
                                       ElfIdentification identification,
                                       int startOffset) {
        NativeWord<Integer> nativeWord = new IntegerNativeWord();
        StructuredFileFactory32 structuredFileFactory = new StructuredFileFactory32();

        Endianness endianness = elfEndianness(identification);
        ElfHeader<Integer> header = readElf32Header(identification,
                new StructuredFile32(file, endianness, startOffset));

        List<ElfSectionHeader<Integer>> sectionHeaders = readElf32SectionHeaders(
                file,
                endianness,
                header.sectionContainingSectionNames(),
                TableHelper.forSectionHeaders(header));

        List<ElfProgramHeader<Integer>> programHeaders = readElf32ProgramHeaders(
                file,
                endianness,
                TableHelper.forProgramHeaders(header));

        return new ElfFile<>(
                nativeWord, file,
                endianness,
                header,
                sectionHeaders,
                new ElfSectionFactory<>(nativeWord, structuredFileFactory),
                programHeaders,
                new ElfSegmentFactory<>());
    }

    private static List<ElfProgramHeader<Integer>> readElf32ProgramHeaders(
            AbstractFile file,
            Endianness endianness,
            TableHelper programHeadersTable)
    {
        List<ElfProgramHeader<Integer>> headers = new ArrayList<>(programHeadersTable.tableSize());

        for (int i = 0; i < programHeadersTable.tableSize(); i++) {
            ElfOffset<Integer> offset = programHeadersTable.offsetForEntry(i);
            StructuredFile32 headerFile = new StructuredFile32(file, endianness, offset);

            ElfProgramHeader<Integer> sectionHeader = readElf32ProgramHeader(headerFile);
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static ElfProgramHeader<Integer> readElf32ProgramHeader(StructuredFile32 headerFile) {
        ElfSegmentType type = ElfSegmentType.fromValue(headerFile.readUnsignedInt());
        Elf32Offset fileOffset = headerFile.readOffset();
        Elf32Address virtualAddress = headerFile.readAddress();
        Elf32Address physicalAddress = headerFile.readAddress();
        int fileSize = headerFile.readUnsignedInt();
        int memorySize = headerFile.readUnsignedInt();
        ElfSegmentFlags flags = new ElfSegmentFlags(headerFile.readUnsignedInt());
        int alignment = headerFile.readUnsignedInt();

        return new ElfProgramHeader<>(
                type,
                fileOffset,
                virtualAddress,
                physicalAddress,
                fileSize,
                memorySize,
                flags,
                alignment);
    }

    public static <NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
    ElfHeader<NATIVE_WORD> readElf32Header(ElfIdentification identification,
                                                     StructuredFile<NATIVE_WORD> elfHeaderFile) {
        ElfType type = ElfType.fromValue(elfHeaderFile.readUnsignedShort());
        ElfMachine machine = ElfMachine.fromValue(elfHeaderFile.readUnsignedShort());

        // TODO: Overflow check
        ElfVersion version = ElfVersion.fromValue((byte)elfHeaderFile.readUnsignedInt());

        ElfAddress<NATIVE_WORD> entry = elfHeaderFile.readAddress();
        ElfOffset<NATIVE_WORD> programHeaderTableOffset = elfHeaderFile.readOffset();
        ElfOffset<NATIVE_WORD> sectionHeaderTableOffset = elfHeaderFile.readOffset();

        int flags = elfHeaderFile.readUnsignedInt();
        short elfHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderSize = elfHeaderFile.readUnsignedShort();
        short programHeaderCount = elfHeaderFile.readUnsignedShort();
        short sectionHeaderSize = elfHeaderFile.readUnsignedShort();
        short sectionHeaderCount = elfHeaderFile.readUnsignedShort();
        short e_shstrndx = elfHeaderFile.readUnsignedShort();

        SectionHeaderIndex sectionNamesStringTableIndex = new SectionHeaderIndex(e_shstrndx);

        return new ElfHeader<>(
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

    private static List<ElfSectionHeader<Integer>> readElf32SectionHeaders(AbstractFile file,
                                                                    Endianness endianness,
                                                                    SectionHeaderIndex sectionHeaderTable,
                                                                    TableHelper<Integer> sectionHeaders) {
        ElfStringTable<Integer> sectionNames = loadSectionsNameStringTable(
                file, endianness, sectionHeaderTable, sectionHeaders);

        List<ElfSectionHeader<Integer>> headers = new ArrayList<>(sectionHeaders.tableSize());

        for (int i = 0; i < sectionHeaders.tableSize(); i++) {
            ElfOffset<Integer> offset = sectionHeaders.offsetForEntry(new SectionHeaderIndex(i));
            StructuredFile32 headerFile = new StructuredFile32(file, endianness, offset);

            ElfSectionHeader<Integer> sectionHeader = readElf32SectionHeader(headerFile, Optional.of(sectionNames));
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static ElfStringTable<Integer> loadSectionsNameStringTable(AbstractFile file,
                                                                Endianness endianness,
                                                                SectionHeaderIndex sectionNamesSection,
                                                                TableHelper sectionHeaderTable) {
        ElfOffset<Integer> offset = sectionHeaderTable.offsetForEntry(sectionNamesSection);
        StructuredFile32 headerFile = new StructuredFile32(file, endianness, offset);
        ElfSectionHeader<Integer> sectionNamesStringTableSection = readElf32SectionHeader(
                headerFile, Optional.empty());
        return new ElfStringTable<Integer>(file, sectionNamesStringTableSection);
    }

    private static ElfSectionHeader<Integer> readElf32SectionHeader(
            StructuredFile32 headerFile,
            Optional<ElfStringTable<Integer>> maybeSectionNames)
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

        return new ElfSectionHeader<Integer>(
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

    private static <T> T throwElfReaderException(String message) {
        throw new RuntimeException(String.format("%s", message));
    }
    private static <T> T throwElfReaderException(String format, Object... args) {
        throw new RuntimeException(String.format(format, args));
    }

}

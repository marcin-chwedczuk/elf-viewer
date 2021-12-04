package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SectionFactory;
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

    public static Elf32File readElf32(AbstractFile file) {
        // TODO: This is weak check
        return new Elf32File((ElfFile<Integer>) readElf(file), List.of());
    }

    public static ElfFile<?> readElf(AbstractFile file) {
        byte[] identificationBytes = file.read(0, ElfIdentificationIndexes.EI_NIDENT);
        ElfIdentification identification = ElfIdentification.parseBytes(identificationBytes);

        final int startOffset = ElfIdentificationIndexes.EI_NIDENT;

        ElfClass elfClass = identification.elfClass();
        if (elfClass.is(ElfClass.ELF_CLASS_32)) {
            return readElf32(file, identification, startOffset);
        } else if (elfClass.is(ElfClass.ELF_CLASS_64)) {
            throw new RuntimeException("TODO");
            // return readElf64(file, identification, startOffset);
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

    /*
    private static Elf64File readElf64(AbstractFile file,
                                       ElfIdentification identification,
                                       int startOffset) {
        Endianness endianness = elfEndianness(identification);

        Elf64Header header = readElf64Header(identification,
                new StructuredFile64(file, endianness, startOffset));

        List<Elf64SectionHeader> sectionHeaders = readElf64SectionHeaders(
                file,
                endianness,
                header.sectionContainingSectionNames(),
                TableHelper.forSectionHeaders(header));

        return new Elf64File(file, endianness, header, sectionHeaders);
    }

    public static Elf64Header readElf64Header(
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

        return new Elf64Header(
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


    private static List<Elf64SectionHeader> readElf64SectionHeaders(AbstractFile file,
                                                                    Endianness endianness,
                                                                    SectionHeaderIndex sectionHeaderTable,
                                                                    TableHelper<Long> sectionHeaders) {
        Elf64StringTable sectionNames = loadSectionsNameStringTable64(
                file, endianness, sectionHeaderTable, sectionHeaders);

        List<Elf64SectionHeader> headers = new ArrayList<>(sectionHeaders.tableSize());

        for (int i = 0; i < sectionHeaders.tableSize(); i++) {
            ElfOffset<Long> offset = sectionHeaders.offsetForEntry(new SectionHeaderIndex(i));
            StructuredFile64 headerFile = new StructuredFile64(file, endianness, offset);

            Elf64SectionHeader sectionHeader = readElf64SectionHeader(headerFile, Optional.of(sectionNames));
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static Elf64StringTable loadSectionsNameStringTable64(AbstractFile file,
                                                                  Endianness endianness,
                                                                  SectionHeaderIndex sectionNamesSection,
                                                                  TableHelper<Long> sectionHeaderTable) {
        ElfOffset<Long> offset = sectionHeaderTable.offsetForEntry(sectionNamesSection);
        StructuredFile64 headerFile = new StructuredFile64(file, endianness, offset);
        Elf64SectionHeader sectionNamesStringTableSection = readElf64SectionHeader(
                headerFile, Optional.empty());
        return new Elf64StringTable(file, sectionNamesStringTableSection);
    }

    private static Elf64SectionHeader readElf64SectionHeader(
            StructuredFile64 headerFile,
            Optional<Elf64StringTable> maybeSectionNames)
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

        return new Elf64SectionHeader(
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
     */


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

        List<Elf32ProgramHeader> programHeaders = readElf32ProgramHeaders(
                file,
                endianness,
                TableHelper.forProgramHeaders(header));

        return new ElfFile<Integer>(
                file,
                endianness,
                header,
                sectionHeaders,
                new ElfSectionFactory<>(nativeWord, structuredFileFactory));
    }

    private static List<Elf32ProgramHeader> readElf32ProgramHeaders(
            AbstractFile file,
            Endianness endianness,
            TableHelper programHeadersTable)
    {
        List<Elf32ProgramHeader> headers = new ArrayList<>(programHeadersTable.tableSize());

        for (int i = 0; i < programHeadersTable.tableSize(); i++) {
            ElfOffset<Integer> offset = programHeadersTable.offsetForEntry(i);
            StructuredFile32 headerFile = new StructuredFile32(file, endianness, offset);

            Elf32ProgramHeader sectionHeader = readElf32ProgramHeader(headerFile);
            headers.add(sectionHeader);
        }

        return headers;
    }

    private static Elf32ProgramHeader readElf32ProgramHeader(StructuredFile32 headerFile) {
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
                                                                    TableHelper sectionHeaders) {
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

package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.BigEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElfReader {
    private ElfReader() { }

    public static Elf32File readElf32(AbstractFile file) {
        return (Elf32File) readElf(file);
    }

    public static Elf64File readElf64(AbstractFile file) {
        // TODO: Better exception than cast class ...
        return (Elf64File) readElf(file);
    }

    public static ElfFile readElf(AbstractFile file) {
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

    private static Elf64File readElf64(AbstractFile file,
                                       ElfIdentification identification,
                                       int startOffset) {
        Endianness endianness = elfEndianness(identification);

        Elf64Header header = readElf64Header(identification,
                new StructuredFile(file, endianness, startOffset));

        return new Elf64File(header);
    }

    public static Elf64Header readElf64Header(
            ElfIdentification identification,
            StructuredFile elfHeaderFile) {
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

    private static Elf32File readElf32(AbstractFile file,
                                       ElfIdentification identification,
                                       int startOffset) {
        Endianness endianness = elfEndianness(identification);
        Elf32Header header = readElf32Header(identification,
                new StructuredFile(file, endianness, startOffset));

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
        Elf32Offset fileOffset = headerFile.readOffset32();
        Elf32Address virtualAddress = headerFile.readAddress32();
        Elf32Address physicalAddress = headerFile.readAddress32();
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

        Elf32Address entry = elfHeaderFile.readAddress32();
        Elf32Offset programHeaderTableOffset = elfHeaderFile.readOffset32();
        Elf32Offset sectionHeaderTableOffset = elfHeaderFile.readOffset32();

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
        Elf32Address inMemoryAddress = headerFile.readAddress32();
        Elf32Offset offsetInFile = headerFile.readOffset32();
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


    private static <T> T throwElfReaderException(String message) {
        throw new RuntimeException(String.format("%s", message));
    }
    private static <T> T throwElfReaderException(String format, Object... args) {
        throw new RuntimeException(String.format(format, args));
    }

}

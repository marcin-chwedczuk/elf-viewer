package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.BigEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;

import java.util.ArrayList;
import java.util.List;

public class ElfReader {
    private ElfReader() { }

    public static Elf32File readElf32(AbstractFile file) {
        byte[] identificationBytes = file.read(0, ElfIdentificationIndexes.EI_NIDENT);
        ElfIdentification identification = ElfIdentification.parseBytes(identificationBytes);

        Endianness endianness = null;
        switch(identification.elfData()) {
            case ELF_DATA_LSB: endianness = new LittleEndian(); break;
            case ELF_DATA_MSB: endianness = new BigEndian(); break;
            default:
                throw new RuntimeException("Invalid elf data: " + identification.elfData());
        }

        final int startOffset = ElfIdentificationIndexes.EI_NIDENT;
        Elf32Header header = readElf32Header(identification, new StructuredFile(file, endianness, startOffset));

        List<Elf32SectionHeader> sectionHeaders = readElf32SectionHeaders(
                file, endianness, header.sectionContainingSectionNames(),
                new TableHelper(
                        header.sectionHeaderTableOffset(),
                        header.sectionHeaderSize(),
                        header.numberOfSectionHeaders()));

        return new Elf32File(header, sectionHeaders);
    }

    public static Elf32Header readElf32Header(ElfIdentification identification, StructuredFile elfHeaderFile) {
        ElfType type = ElfType.fromUnsignedShort(elfHeaderFile.readUnsignedShort());
        ElfMachine machine = ElfMachine.fromUnsignedShort(elfHeaderFile.readUnsignedShort());

        // TODO: Overflow check
        ElfVersion version = ElfVersion.fromByte((byte)elfHeaderFile.readUnsignedInt());

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

        SHTIndex sectionNamesStringTableIndex = new SHTIndex(e_shstrndx);

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
                                                                    SHTIndex sectionContainingSectionNames,
                                                                    TableHelper tableHelper) {
        List<Elf32SectionHeader> headers = new ArrayList<>(tableHelper.tableSize());

        for (int i = 0; i < tableHelper.tableSize(); i++) {
            Elf32Offset offset = tableHelper.offsetForEntry(new SHTIndex(i));
            StructuredFile headerFile = new StructuredFile(file, endianness, offset);

            Elf32SectionHeader sectionHeader = readElf32SectionHeader(headerFile);
            headers.add(sectionHeader);
        }

        // Get section names
        Elf32SectionHeader sectionNamesSection = headers.get(sectionContainingSectionNames.intValue());
        StringTable stringTable = new StringTable(file, sectionNamesSection);

        for (Elf32SectionHeader sectionHeader: headers) {
            StringTableIndex sectionName = sectionHeader.nameIndex();
            sectionHeader.setSectionName(stringTable.getStringAtIndex(sectionName));
        }

        return headers;
    }

    private static Elf32SectionHeader readElf32SectionHeader(StructuredFile headerFile) {
        int sectionNameIndex = headerFile.readUnsignedInt();
        ElfSectionType type = ElfSectionType.fromUnsignedInt(headerFile.readUnsignedInt());
        SectionAttributes flags = new SectionAttributes(headerFile.readUnsignedInt());
        Elf32Address inMemoryAddress = headerFile.readAddress();
        Elf32Offset offsetInFile = headerFile.readOffset();
        int sectionSize = headerFile.readUnsignedInt();
        int link = headerFile.readUnsignedInt();
        int info = headerFile.readUnsignedInt();
        int addressAlignment = headerFile.readUnsignedInt();
        int containedEntrySize = headerFile.readUnsignedInt();

        return new Elf32SectionHeader(
                sectionNameIndex,
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

}

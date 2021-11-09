package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.endianness.BigEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.Endianness;
import pl.marcinchwedczuk.elfviewer.elfreader.endianness.LittleEndian;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

public class ElfReader {
    private ElfReader() { }

    public static Elf32Header read(AbstractFile file) {
        byte[] identificationBytes = file.read(0, ElfIdentificationIndexes.EI_NIDENT);
        ElfIdentification identification = ElfIdentification.parseBytes(identificationBytes);

        int offset = ElfIdentificationIndexes.EI_NIDENT;
        Endianness endianness = null;
        switch(identification.elfData()) {
            case ELF_DATA_LSB: endianness = new LittleEndian(); break;
            case ELF_DATA_MSB: endianness = new BigEndian(); break;
            default:
                throw new RuntimeException("Invalid elf data: " + identification.elfData());
        };

        short e_type = file.readUnsignedShort(endianness, offset);
        offset += 2;
        ElfType type = ElfType.fromUnsignedShort(e_type);

        short e_machine = file.readUnsignedShort(endianness, offset);
        offset += 2;
        ElfMachine machine = ElfMachine.fromUnsignedShort(e_machine);

        int e_version = file.readUnsignedInt(endianness, offset);
        offset += 4;
        // TODO: Overflow check
        ElfVersion version = ElfVersion.fromByte((byte)e_version);

        int e_entry = file.readUnsignedInt(endianness, offset);
        offset += 4;
        Elf32Address entry = new Elf32Address(e_entry);

        return new Elf32Header(
                identification,
                type,
                machine,
                version,
                entry);
    }
}

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

        return new Elf32Header(identification, type);
    }
}

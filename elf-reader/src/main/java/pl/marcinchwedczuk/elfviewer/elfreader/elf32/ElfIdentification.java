package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfIdentificationIndexes.*;

public class ElfIdentification {
    public static ElfIdentification parseBytes(byte[] bytes) {
        return new ElfIdentification(bytes);
    }

    public final byte[] originalBytes;

    private ElfIdentification(byte[] bytes) {
        if (bytes.length != EI_NIDENT)
            throw new IllegalArgumentException(String.format(
                    "Expected identification indexes to be %d bytes long, " +
                            "but was %d.", EI_NIDENT, bytes.length));

        this.originalBytes = Arrays.copyOf(bytes, EI_NIDENT);
    }

    public String magicString() {
        byte[] magicBytes = {
                originalBytes[EI_MAG0],
                originalBytes[EI_MAG1],
                originalBytes[EI_MAG2],
                originalBytes[EI_MAG3]
        };

        return new String(magicBytes, 0, magicBytes.length, StandardCharsets.US_ASCII);
    }

    public ElfClass elfClass() {
        return ElfClass.fromByte(originalBytes[EI_CLASS]);
    }

    public ElfData elfData() {
        return ElfData.fromByte(originalBytes[EI_DATA]);
    }

    public ElfVersion version() {
        return ElfVersion.fromByte(originalBytes[EI_VERSION]);
    }

    public byte[] paddingBytes() {
        return Arrays.copyOfRange(originalBytes, EI_PAD, originalBytes.length);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentificationIndexes.*;

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

    /**
     * @return ELF binary architecture, that is 32 or 64 bit.
     */
    public ElfClass elfClass() {
        return ElfClass.fromValue(originalBytes[EI_CLASS]);
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

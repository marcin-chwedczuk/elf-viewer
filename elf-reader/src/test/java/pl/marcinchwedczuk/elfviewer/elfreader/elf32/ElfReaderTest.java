package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ElfReaderTest {

    @Test
    public void elf32_identification() throws IOException {
        byte[] helloWorld32 = this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes();

        AbstractFile elfFile = new InMemoryFile(helloWorld32);

        Elf32Header header = ElfReader.read(elfFile);
        ElfIdentification identification = header.identification();

        assertThat(identification.magicString())
                .isEqualTo("\u007fELF");

        // 32 bit elf
        assertThat(identification.elfClass())
                .isEqualTo(ElfClass.ELF_CLASS_32);

        // little endian
        assertThat(identification.elfData())
                .isEqualTo(ElfData.ELF_DATA_LSB);

        // only valid value
        assertThat(identification.version())
                .isEqualTo(ElfVersion.EV_CURRENT);

        // elf type
        assertThat(header.type())
                .isEqualTo(ElfType.Executable);

        // elf machine - x86 (386)
        assertThat(header.machine())
                .isEqualTo(ElfMachine.Intel386);

        assertThat(header.version())
                .isEqualTo(ElfVersion.EV_CURRENT);

        assertThat(header.entry())
                .isEqualTo(new Elf32Address(0x8048310));
    }
}
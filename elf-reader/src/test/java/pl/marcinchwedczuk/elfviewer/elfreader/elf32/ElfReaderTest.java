package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ElfReaderTest {
    @Test
    public void elf32_header() throws IOException {
        byte[] helloWorld32 = this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes();

        AbstractFile elfFile = new InMemoryFile(helloWorld32);

        Elf32Header header = ElfReader.readElf32(elfFile).header;
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

        // program start address in memory
        assertThat(header.entry())
                .isEqualTo(new Elf32Address(0x8048310));

        // offset into ELF file
        assertThat(header.programHeaderTableOffset())
                .isEqualTo(new Elf32Offset(52));

        // offset into ELF file
        assertThat(header.sectionHeaderTableOffset())
                .isEqualTo(new Elf32Offset(6116));

        // flags
        assertThat(header.flags())
                .isEqualTo(0);

        // header size
        assertThat(header.headerSize())
                .isEqualTo(52);

        // program headers
        assertThat(header.programHeaderSize())
                .isEqualTo(32);
        assertThat(header.numberOfProgramHeaders())
                .isEqualTo(9);

        // section headers
        assertThat(header.sectionHeaderSize())
                .isEqualTo(40);
        assertThat(header.numberOfSectionHeaders())
                .isEqualTo(31);

        // Strings section
        assertThat(header.sectionContainingSectionNames())
                .isEqualTo(new SHTIndex(28));
    }


    @Test
    public void elf32_sections() throws IOException {
        byte[] helloWorld32 = this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes();

        AbstractFile elfFile = new InMemoryFile(helloWorld32);

        List<Elf32SectionHeader> sections = ElfReader.readElf32(elfFile).sectionHeaders;

        sections.forEach(section -> {
            System.out.println(section);
        });
    }
}
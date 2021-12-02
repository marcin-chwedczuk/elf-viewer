package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Address;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64.Elf64Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ElfReader_64Bits_Test {
    private final AbstractFile helloWorld64;

    public ElfReader_64Bits_Test() throws IOException {
        byte[] binaryBytes = this.getClass()
                .getResourceAsStream("hello-world-64")
                .readAllBytes();

        helloWorld64 = new InMemoryFile(binaryBytes);
    }

    @Test
    public void elf64_header() {
        Elf64Header header = ElfReader.readElf64(helloWorld64).header();
        ElfIdentification identification = header.identification();

        assertThat(identification.magicString())
                .isEqualTo("\u007fELF");

        assertThat(identification.printableMagicString())
                .isEqualTo(".ELF");

        // 32 bit elf
        assertThat(identification.elfClass())
                .isEqualTo(ElfClass.ELF_CLASS_64);

        // little endian
        assertThat(identification.elfData())
                .isEqualTo(ElfData.ELF_DATA_LSB);

        // only valid value
        assertThat(identification.elfVersion())
                .isEqualTo(ElfVersion.CURRENT);

        // Most systems sets this field to 0 instead of 3(LINUX)
        assertThat(identification.osAbi())
                .isEqualTo(ElfOsAbi.NONE);

        assertThat((int)identification.osAbiVersion())
                .isEqualTo(0);

        // elf type
        assertThat(header.type())
                .isEqualTo(ElfType.SHARED_OBJECT);

        // elf machine - x86 (386)
        assertThat(header.machine())
                .isEqualTo(ElfMachine.X86_64);

        assertThat(header.version())
                .isEqualTo(ElfVersion.CURRENT);

        // program start address in memory
        assertThat(header.entry())
                .isEqualTo(new Elf64Address(0x1050L));

        // offset into ELF file
        assertThat(header.programHeaderTableOffset())
                .isEqualTo(new Elf64Offset(64));

        // offset into ELF file
        assertThat(header.sectionHeaderTableOffset())
                .isEqualTo(new Elf64Offset(14744));

        // flags
        assertThat(header.flags())
                .isEqualTo(0);

        // header size
        assertThat(header.headerSize())
                .isEqualTo(64);

        // program headers
        assertThat(header.programHeaderSize())
                .isEqualTo(56);
        assertThat(header.numberOfProgramHeaders())
                .isEqualTo(11);

        // section headers
        assertThat(header.sectionHeaderSize())
                .isEqualTo(64);
        assertThat(header.numberOfSectionHeaders())
                .isEqualTo(30);

        // Strings section
        assertThat(header.sectionContainingSectionNames())
                .isEqualTo(new SectionHeaderIndex(29));
    }
}

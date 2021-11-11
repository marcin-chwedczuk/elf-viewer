package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributeFlags.Allocate;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributeFlags.Executable;

class ElfReaderTest {
    private final AbstractFile helloWorld32;

    public ElfReaderTest() throws IOException {
        byte[] binaryBytes = this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes();

        helloWorld32 = new InMemoryFile(binaryBytes);
    }

    @Test
    public void elf32_header() {
        Elf32Header header = ElfReader.readElf32(helloWorld32).header;
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
                .isEqualTo(new SectionHeaderTableIndex(28));
    }

    @Test
    public void elf32_sections() {
        List<Elf32SectionHeader> sections = ElfReader.readElf32(helloWorld32).sectionHeaders;
        Optional<Elf32SectionHeader> maybeTextSection = sections.stream()
                .filter(s -> s.name().equals(".text"))
                .findFirst();

        assertThat(maybeTextSection)
                .isPresent();

        Elf32SectionHeader textSection = maybeTextSection.get();

        assertThat(textSection.addressAlignment())
                .isEqualTo(16);

        assertThat(textSection.type())
                .isEqualTo(ElfSectionType.ProgBits);

        // Not applicable to this section
        assertThat(textSection.containedEntrySize())
                .isEqualTo(0);

        assertThat(textSection.flags())
                .isEqualTo(SectionAttributes.of(Allocate, Executable));

        assertThat(textSection.info())
                .isEqualTo(0);

        assertThat(textSection.link())
                .isEqualTo(0);

        assertThat(textSection.inMemoryAddress())
                .isEqualTo(new Elf32Address(0x08048310));

        assertThat(textSection.offsetInFile())
                .isEqualTo(new Elf32Offset(0x00000310));

        assertThat(textSection.sectionSize())
                .isEqualTo(0x00000192);
    }

    @Test
    void elf32_symbol_table() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32SectionHeader> maybeSymbolTableSection = elfFile.getSectionHeader(".symtab");
        assertThat(maybeSymbolTableSection)
                .isPresent();

        Optional<Elf32SectionHeader> maybeSymbolStringTable = elfFile.getSectionHeader(".strtab");
        assertThat(maybeSymbolStringTable)
                .isPresent();

        StringTable symbolNames = new StringTable(helloWorld32, maybeSymbolStringTable.get());

        Elf32SectionHeader symbolTableSection = maybeSymbolTableSection.get();
        SymbolTable symbols = new SymbolTable(
                helloWorld32,
                elfFile.endianness,
                symbolTableSection,
                symbolNames,
                elfFile);

        for (int i = 0; i < symbols.size(); i++) {
            Elf32Symbol sym = symbols.get(new SymbolTableIndex(i));
            System.out.println(sym);
        }
    }
}
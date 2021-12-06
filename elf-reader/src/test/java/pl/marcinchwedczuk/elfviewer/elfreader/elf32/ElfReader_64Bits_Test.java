package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SymbolTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.sections.Elf64SymbolTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolBinding.GLOBAL;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolType.FUNCTION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolVisibility.DEFAULT;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.ALLOCATE;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.EXECUTABLE;

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

    @Test
    public void elf64_section_header() {
        Elf64File elfFile = ElfReader.readElf64(helloWorld64);
        Optional<Elf64Section> maybeTextSection = elfFile.sectionWithName(ElfSectionNames.TEXT);

        assertThat(maybeTextSection)
                .isPresent();
        Elf64SectionHeader textSection = maybeTextSection
                .map(Elf64Section::header)
                .get();

        assertThat(textSection.addressAlignment())
                .isEqualTo(16);

        assertThat(textSection.type())
                .isEqualTo(PROGBITS);

        // TODO: Test this field using other section
        // Not applicable to this section
        assertThat(textSection.containedEntrySize())
                .isEqualTo(0);

        assertThat(textSection.flags())
                .isEqualTo(new SectionAttributes(ALLOCATE, EXECUTABLE));

        assertThat(textSection.info())
                .isEqualTo(0);

        assertThat(textSection.link())
                .isEqualTo(0);

        assertThat(textSection.virtualAddress())
                .isEqualTo(new Elf64Address(0x1050L));

        assertThat(textSection.fileOffset())
                .isEqualTo(new Elf64Offset(0x1050L));

        assertThat(textSection.size())
                .isEqualTo(0x181);
    }

    @Test
    void elf64_symbol_table() {
        Elf64File elfFile = ElfReader.readElf64(helloWorld64);

        Optional<Elf64Section> maybeSymtabSection =
                elfFile.sectionWithName(ElfSectionNames.SYMTAB);

        assertThat(maybeSymtabSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf64SymbolTableSection.class);
                });

        Elf64SymbolTable symbols = ((Elf64SymbolTableSection)maybeSymtabSection.get()).symbolTable();

        // 1. Check Section symbols have their names resolved
        Optional<Elf64Symbol> textSectionSymbol = symbols.slowlyFindSymbolByName(".text");
        assertThat(textSectionSymbol)
                .isPresent();

        // 2. Check symbol for 'main' is defined and all values are set
        Optional<Elf64Symbol> maybeMain = symbols.slowlyFindSymbolByName("main");
        assertThat(maybeMain)
                .isPresent()
                .hasValueSatisfying(main -> {
                    assertThat(main.binding())
                            .isEqualTo(GLOBAL);
                    assertThat(main.symbolType())
                            .isEqualTo(FUNCTION);

                    assertThat(main.other())
                            .isEqualTo((byte)0);
                    assertThat(main.visibility())
                            .isEqualTo(DEFAULT);

                    assertThat(main.size())
                            .isEqualTo(34);
                    assertThat(main.value())
                            .isEqualTo(new Elf64Address(0x0000000000001135L));

                    // Check section header index, it should point to .text section
                    assertThat(main.index())
                            .isEqualTo(new SectionHeaderIndex(14));
                    assertThat(elfFile.sectionHeaders().get(14).name())
                            .isEqualTo(".text");
                });
    }
}

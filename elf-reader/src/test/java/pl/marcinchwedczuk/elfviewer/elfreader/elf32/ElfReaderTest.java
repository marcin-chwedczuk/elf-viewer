package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.StandardSectionNames;
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

        // 1. Check Section symbols have their names resolved
        Optional<Elf32Symbol> textSectionSymbol = symbols.slowlyFindSymbolByName(".text");
        assertThat(textSectionSymbol)
                .isPresent();

        // 2. Check symbol for 'main' is defined and all values are set
        Optional<Elf32Symbol> maybeMain = symbols.slowlyFindSymbolByName("main");
        assertThat(maybeMain)
                .isPresent();

        Elf32Symbol main = maybeMain.get();
        assertThat(main.binding())
                .isEqualTo(Elf32SymbolBinding.Global);
        assertThat(main.symbolType())
                .isEqualTo(Elf32SymbolType.Function);

        // TODO: Parse visibility
        assertThat(main.other())
                .isEqualTo((byte)0);

        assertThat(main.size())
                .isEqualTo(46);
        assertThat(main.value())
                .isEqualTo(new Elf32Address(0x0804840b));

        // Check section header index, it should point to .text section
        assertThat(main.index())
                .isEqualTo(new SectionHeaderTableIndex(14));
        assertThat(elfFile.sectionHeaders.get(14).name())
                .isEqualTo(".text");
    }

    @Test
    void elf32_relocation_table() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32SectionHeader> maybeRelocationsSection = elfFile.getSectionHeader(".rel.dyn");
        assertThat(maybeRelocationsSection)
                .isPresent();

        RelocationsTable relocations = new RelocationsTable(
                helloWorld32,
                elfFile.endianness,
                maybeRelocationsSection.get(),
                elfFile);

        assertThat(relocations.size())
                .isEqualTo(1);

        Elf32Relocation relocation = relocations.get(0);
        assertThat(relocation.offset())
                .isEqualTo(new Elf32Address(0x08049ffc));
        assertThat(relocation.info())
                .isEqualTo(0x00000206);
    }

    @Test
    void elf32_segments() {
        List<Elf32ProgramHeader> segments = ElfReader
                .readElf32(helloWorld32)
                .programHeaders;

        assertThat(segments.size())
                .isEqualTo(9);

        Elf32ProgramHeader textSegment = segments.get(2);

        assertThat(textSegment.type())
                .isEqualTo(Elf32SegmentType.Load);
        assertThat(textSegment.fileOffset())
                .isEqualTo(new Elf32Offset(0x000000));
        assertThat(textSegment.virtualAddress())
                .isEqualTo(new Elf32Address(0x08048000));
        assertThat(textSegment.physicalAddress())
                .isEqualTo(new Elf32Address(0x08048000));
        assertThat(textSegment.fileSize())
                .isEqualTo(0x005c8);
        assertThat(textSegment.memorySize())
                .isEqualTo(0x005c8);
        assertThat(textSegment.flags())
                .isEqualTo(Elf32SegmentFlags.of(
                        Elf32SegmentFlags.Readable,
                        Elf32SegmentFlags.Executable
                ));
        assertThat(textSegment.alignment())
                .isEqualTo(0x1000);
    }

    @Test
    void elf32_notes() {
        Elf32File helloWorldElf = ElfReader.readElf32(helloWorld32);

        List<Elf32NoteInformation> notes = ElfReader.readNotes(helloWorldElf, ".note.ABI-tag");

        // TODO: Fix it
        for (Elf32NoteInformation note : notes) {
            // System.out.println(note);
        }
    }

    @Test
    void elf32_interpreter() {
        Elf32File helloWorldElf = ElfReader.readElf32(helloWorld32);
        Elf32ProgramHeader interpreterSegment = helloWorldElf.programHeaders.stream()
                .filter(ph -> ph.type().equals(Elf32SegmentType.Interpreter))
                .findFirst()
                .get();

        Elf32InterpreterProgramHeader iph = new Elf32InterpreterProgramHeader(
                helloWorldElf,
                interpreterSegment);

        assertThat(iph.getInterpreterPath())
                .isEqualTo("/lib/ld-linux.so.2");
    }

    @Test
    void dynamic_section() {
        Elf32File helloWorldElf = ElfReader.readElf32(helloWorld32);
        List<Elf32DynamicStructure> results = ElfReader.readDynamicSection(helloWorldElf);

        assertThat(results.get(0))
                .isEqualTo(new Elf32DynamicStructure(
                        Elf32DynamicArrayTag.NEEDED,
                        1,
                        null));

        assertThat(results.get(1))
                .isEqualTo(new Elf32DynamicStructure(
                        Elf32DynamicArrayTag.INIT,
                        null,
                        new Elf32Address(0x80482a8)));

        // Read library name
        Elf32DynamicStructure strTabPtr = results.stream()
                .filter(x -> x.tag().equals(Elf32DynamicArrayTag.STR_TAB))
                .findFirst()
                .get();

        int offset = results.get(0).value();
        // TODO: Translate STRTAB memory address -> physical file offset

        // Read using StringTable ...

        // See: https://stackoverflow.com/a/22613627/1779504
    }

    @Test
    void gnu_hash_section() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Elf32SectionHeader dynsymSection = elfFile
                .getSectionHeader(StandardSectionNames.DYN_SYM)
                .get();

        Elf32SectionHeader dymstrSection = elfFile
                .getSectionHeader(StandardSectionNames.DYN_STR)
                .get();

        StringTable symbolNames =
                new StringTable(elfFile.storage, dymstrSection);

        SymbolTable dymsym = new SymbolTable(
                elfFile.storage,
                elfFile.endianness,
                dynsymSection,
                symbolNames,
                elfFile);

        Elf32SectionHeader gnuHashSection = elfFile
                .getSectionHeader(StandardSectionNames.GNU_HASH)
                .get();

        Elf32GnuHash gnuHash = ElfReader.readGnuHashSection(
                elfFile,
                gnuHashSection,
                dymsym);

        assertThat(gnuHash.findSymbol("blah"))
                .isEmpty();

        assertThat(gnuHash.findSymbol("_IO_stdin_used").get().name())
                .isEqualTo("_IO_stdin_used");
    }
}
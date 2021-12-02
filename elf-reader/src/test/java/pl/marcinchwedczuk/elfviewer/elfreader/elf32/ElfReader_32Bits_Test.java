package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionHeaderIndex;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuABITag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnuBuildId;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTagType.*;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolBinding.GLOBAL;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolType.FUNCTION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolVisibility.DEFAULT;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.ALLOCATE;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.SectionAttributes.EXECUTABLE;

class ElfReader_32Bits_Test {
    private final AbstractFile helloWorld32;

    public ElfReader_32Bits_Test() throws IOException {
        byte[] binaryBytes = this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes();

        helloWorld32 = new InMemoryFile(binaryBytes);
    }

    @Test
    public void elf32_header() {
        Elf32Header header = ElfReader.readElf32(helloWorld32).header();
        ElfIdentification identification = header.identification();

        assertThat(identification.magicString())
                .isEqualTo("\u007fELF");

        assertThat(identification.printableMagicString())
                .isEqualTo(".ELF");

        // 32 bit elf
        assertThat(identification.elfClass())
                .isEqualTo(ElfClass.ELF_CLASS_32);

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
                .isEqualTo(ElfType.EXECUTABLE);

        // elf machine - x86 (386)
        assertThat(header.machine())
                .isEqualTo(ElfMachine.INTEL_386);

        assertThat(header.version())
                .isEqualTo(ElfVersion.CURRENT);

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
                .isEqualTo(new SectionHeaderIndex(28));
    }

    @Test
    public void elf32_section_header() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);
        Optional<Elf32Section> maybeTextSection = elfFile.sectionWithName(ElfSectionNames.TEXT);

        assertThat(maybeTextSection)
                .isPresent();
        Elf32SectionHeader textSection = maybeTextSection
                .map(Elf32Section::header)
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
                .isEqualTo(new Elf32Address(0x08048310));

        assertThat(textSection.fileOffset())
                .isEqualTo(new Elf32Offset(0x00000310));

        assertThat(textSection.size())
                .isEqualTo(0x00000192);
    }

    @Test
    void elf32_symbol_table() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeSymtabSection =
                elfFile.sectionWithName(ElfSectionNames.SYMTAB);

        assertThat(maybeSymtabSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf32SymbolTableSection.class);
                });

        SymbolTable symbols = ((Elf32SymbolTableSection)maybeSymtabSection.get()).symbolTable();

        // 1. Check Section symbols have their names resolved
        Optional<Elf32Symbol> textSectionSymbol = symbols.slowlyFindSymbolByName(".text");
        assertThat(textSectionSymbol)
                .isPresent();

        // 2. Check symbol for 'main' is defined and all values are set
        Optional<Elf32Symbol> maybeMain = symbols.slowlyFindSymbolByName("main");
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
                            .isEqualTo(46);
                    assertThat(main.value())
                            .isEqualTo(new Elf32Address(0x0804840b));

                    // Check section header index, it should point to .text section
                    assertThat(main.index())
                            .isEqualTo(new SectionHeaderIndex(14));
                    assertThat(elfFile.sectionHeaders.get(14).name())
                            .isEqualTo(".text");
                });
    }

    @Test
    void elf32_relocation_table() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeRelSection =
                elfFile.sectionWithName(ElfSectionNames.REL(".dyn"));

        assertThat(maybeRelSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf32RelocationSection.class);
                });

        Elf32RelocationSection relSection = ((Elf32RelocationSection)maybeRelSection.get());
        List<Elf32Relocation> relocations = relSection.relocations();

        assertThat(relocations.size())
                .isEqualTo(1);

        Elf32Relocation relocation = relocations.get(0);
        assertThat(relocation.offset())
                .isEqualTo(new Elf32Address(0x08049ffc));

        assertThat(relocation.info())
                .isEqualTo(0x00000206);
    }

    @Test
    void elf32_segment_header() {
        List<Elf32Segment> segments = ElfReader
                .readElf32(helloWorld32)
                .segments();

        assertThat(segments.size())
                .isEqualTo(9);

        Elf32ProgramHeader textSegment = segments.get(2).programHeader();

        assertThat(textSegment.type())
                .isEqualTo(Elf32SegmentType.LOAD);
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
                .isEqualTo(new Elf32SegmentFlags(
                        Elf32SegmentFlags.Readable,
                        Elf32SegmentFlags.Executable));
        assertThat(textSegment.alignment())
                .isEqualTo(0x1000);
    }

    @Test
    void elf32_notes_abi_tag() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_ABI_TAG);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf32NotesSection.class);
                });

        Elf32NotesSection notesSection = (Elf32NotesSection) maybeNotesSection.get();

        Elf32NoteGnuABITag gnuAbi = (Elf32NoteGnuABITag) notesSection.notes().get(0);
        assertThat(gnuAbi.minSupportedKernelVersion())
                .isEqualTo("2.6.32");
    }

    @Test
    void elf32_notes_build_id() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_GNU_BUILD_ID);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf32NotesSection.class);
                });

        Elf32NotesSection notesSection = (Elf32NotesSection) maybeNotesSection.get();

        Elf32NoteGnuBuildId buildId = (Elf32NoteGnuBuildId) notesSection.notes().get(0);
        assertThat(buildId.buildId())
                .isEqualTo("70faabdeb335c923041b807b56a05bc131883779");
    }

    @Test
    void elf32_interpreter() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeInterp = elfFile.sectionWithName(ElfSectionNames.INTERP);
        assertThat(maybeInterp)
                .isPresent()
                .hasValueSatisfying(value -> {
                   assertThat(value)
                           .isInstanceOf(Elf32InterpreterSection.class);
                });

        Elf32InterpreterSection interp = ((Elf32InterpreterSection) maybeInterp.get());

        assertThat(interp.interpreterPath())
                .isEqualTo("/lib/ld-linux.so.2");
    }

    @Test
    void elf32_dynamic_section() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeDynamic =
                elfFile.sectionOfType(ElfSectionType.DYNAMIC);

        assertThat(maybeDynamic)
                .isPresent()
                .hasValueSatisfying(value -> {
                   assertThat(value)
                        .isInstanceOf(Elf32DynamicSection.class);
                });

        Elf32DynamicSection dynamic = (Elf32DynamicSection)maybeDynamic.get();
        List<Elf32DynamicTag> results = dynamic.dynamicTags();

        assertThat(results.get(0))
                .isEqualTo(new Elf32DynamicTag(NEEDED, 1));

        assertThat(results.get(1))
                .isEqualTo(new Elf32DynamicTag(INIT, 0x80482a8));

        // Read library name
        Optional<String> libName = dynamic.getDynamicLibraryName(results.get(0));

        assertThat(libName)
                .isPresent()
                .hasValue("libc.so.6");
    }

    @Test
    void elf32_gnu_hash_section() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeGnuHash =
                elfFile.sectionOfType(ElfSectionType.GNU_HASH);

        assertThat(maybeGnuHash)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(Elf32GnuHashSection.class);
                });

        Elf32GnuHashSection gnuHash = (Elf32GnuHashSection)maybeGnuHash.get();
        Elf32GnuHashTable hashTable = gnuHash.gnuHashTable();

        assertThat(hashTable.findSymbol("blah"))
                .isEmpty();

        assertThat(hashTable.findSymbol("_IO_stdin_used").get().name())
                .isEqualTo("_IO_stdin_used");

        // TODO: Test with library exporting more than 1 symbol
    }

    @Test
    void read_comment_section_contents() {
        Elf32File elfFile = ElfReader.readElf32(helloWorld32);

        Optional<Elf32Section> maybeComment = elfFile.sectionWithName(ElfSectionNames.COMMENT);

        assertThat(maybeComment)
                .isPresent();

        Elf32Section comment = maybeComment.get();
        List<String> comments = comment.readContentsAsStrings();

        assertThat(comments)
                .hasSize(1)
                .contains("GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.12) 5.4.0 20160609");
    }
}
package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfReader;
import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteGnuABITag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteGnuBuildId;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf64.ElfAddressAny;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.io.InMemoryFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTagType.INIT;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfDynamicTagType.NEEDED;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolBinding.GLOBAL;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolType.FUNCTION;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolVisibility.DEFAULT;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.PROGBITS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionAttributes.ALLOCATE;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.SectionAttributes.EXECUTABLE;

class ElfReader_32Bits_Test {
    private final AbstractFile helloWorld32;
    private final AbstractFile libc32;

    public ElfReader_32Bits_Test() throws IOException {
        helloWorld32 = new InMemoryFile(this.getClass()
                .getResourceAsStream("hello-world-32")
                .readAllBytes());

        libc32 = new InMemoryFile(this.getClass()
                .getResourceAsStream("libc-2.33.so")
                .readAllBytes());
    }

    @Test
    public void elf32_header() {
        ElfHeader<Integer> header = ElfReader.readElf32(helloWorld32)
                .header();

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
                .isEqualTo(new ElfAddressAny<>(0x8048310));

        // offset into ELF file
        assertThat(header.programHeaderTableOffset())
                .isEqualTo(new ElfOffsetAny<>(52));

        // offset into ELF file
        assertThat(header.sectionHeaderTableOffset())
                .isEqualTo(new ElfOffsetAny<>(6116));

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
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);
        Optional<ElfSection<Integer>> maybeTextSection = elfFile.sectionWithName(ElfSectionNames.TEXT);

        assertThat(maybeTextSection)
                .isPresent();
        ElfSectionHeader<Integer> textSection = maybeTextSection
                .map(ElfSection::header)
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
                .isEqualTo(new ElfAddressAny<>(0x08048310));

        assertThat(textSection.fileOffset())
                .isEqualTo(new ElfOffsetAny<>(0x00000310));

        assertThat(textSection.size())
                .isEqualTo(0x00000192);
    }

    @Test
    void elf32_symbol_table() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeSymtabSection =
                elfFile.sectionWithName(ElfSectionNames.SYMTAB);

        assertThat(maybeSymtabSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfSymbolTableSection.class);
                });

        ElfSymbolTable<Integer> symbols = ((ElfSymbolTableSection<Integer>)maybeSymtabSection.get()).symbolTable();

        // 1. Check Section symbols have their names resolved
        Optional<ElfSymbol<Integer>> textSectionSymbol = symbols.slowlyFindSymbolByName(".text");
        assertThat(textSectionSymbol)
                .isPresent();

        // 2. Check symbol for 'main' is defined and all values are set
        Optional<ElfSymbol<Integer>> maybeMain = symbols.slowlyFindSymbolByName("main");
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
                            .isEqualTo(new ElfAddressAny<>(0x0804840b));

                    // Check section header index, it should point to .text section
                    assertThat(main.index())
                            .isEqualTo(new SectionHeaderIndex(14));
                    assertThat(elfFile.sectionHeaders().get(14).name())
                            .isEqualTo(".text");
                });
    }

    @Test
    void elf32_relocation_table() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeRelSection =
                elfFile.sectionWithName(ElfSectionNames.REL(".dyn"));

        assertThat(maybeRelSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfRelocationSection.class);
                });

        ElfRelocationSection<Integer> relSection = ((ElfRelocationSection<Integer>)maybeRelSection.get());
        List<ElfRelocation<Integer>> relocations = relSection.relocations();

        assertThat(relocations.size())
                .isEqualTo(1);

        ElfRelocation<Integer> relocation = relocations.get(0);
        assertThat(relocation.offset())
                .isEqualTo(new ElfAddressAny<>(0x08049ffc));

        assertThat(relocation.info())
                .isEqualTo(0x00000206);
    }

    @Test
    void elf32_segment_header() {
        List<ElfSegment<Integer>> segments = ElfReader.readElf32(helloWorld32)
                .segments();

        assertThat(segments.size())
                .isEqualTo(9);

        ElfProgramHeader<Integer> textSegment = segments.get(2).programHeader();

        assertThat(textSegment.type())
                .isEqualTo(ElfSegmentType.LOAD);
        assertThat(textSegment.fileOffset())
                .isEqualTo(new ElfOffsetAny<>(0x000000));
        assertThat(textSegment.virtualAddress())
                .isEqualTo(new ElfAddressAny<>(0x08048000));
        assertThat(textSegment.physicalAddress())
                .isEqualTo(new ElfAddressAny<>(0x08048000));
        assertThat(textSegment.fileSize())
                .isEqualTo(0x005c8);
        assertThat(textSegment.memorySize())
                .isEqualTo(0x005c8);
        assertThat(textSegment.flags())
                .isEqualTo(new ElfSegmentFlags(
                        ElfSegmentFlags.Readable,
                        ElfSegmentFlags.Executable));
        assertThat(textSegment.alignment())
                .isEqualTo(0x1000);
    }

    @Test
    void elf32_notes_abi_tag() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_ABI_TAG);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfNotesSection.class);
                });

        ElfNotesSection<Integer> notesSection = (ElfNotesSection<Integer>) maybeNotesSection.get();

        ElfNoteGnuABITag gnuAbi = (ElfNoteGnuABITag) notesSection.notes().get(0);
        assertThat(gnuAbi.minSupportedKernelVersion())
                .isEqualTo("2.6.32");
    }

    @Test
    void elf32_notes_build_id() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_GNU_BUILD_ID);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfNotesSection.class);
                });

        ElfNotesSection<Integer> notesSection = (ElfNotesSection<Integer>) maybeNotesSection.get();

        ElfNoteGnuBuildId buildId = (ElfNoteGnuBuildId) notesSection.notes().get(0);
        assertThat(buildId.buildId())
                .isEqualTo("70faabdeb335c923041b807b56a05bc131883779");
    }

    @Test
    void elf32_interpreter() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeInterp = elfFile.sectionWithName(ElfSectionNames.INTERP);
        assertThat(maybeInterp)
                .isPresent()
                .hasValueSatisfying(value -> {
                   assertThat(value)
                           .isInstanceOf(ElfInterpreterSection.class);
                });

        ElfInterpreterSection<Integer> interp = ((ElfInterpreterSection<Integer>) maybeInterp.get());

        assertThat(interp.interpreterPath())
                .isEqualTo("/lib/ld-linux.so.2");
    }

    @Test
    void elf32_dynamic_section() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeDynamic =
                elfFile.sectionOfType(ElfSectionType.DYNAMIC);

        assertThat(maybeDynamic)
                .isPresent()
                .hasValueSatisfying(value -> {
                   assertThat(value)
                        .isInstanceOf(ElfDynamicSection.class);
                });

        ElfDynamicSection<Integer> dynamic = (ElfDynamicSection<Integer>)maybeDynamic.get();
        List<ElfDynamicTag<Integer>> results = dynamic.dynamicTags();

        assertThat(results.get(0))
                .isEqualTo(new ElfDynamicTag<>(NEEDED, 1));

        assertThat(results.get(1))
                .isEqualTo(new ElfDynamicTag<>(INIT, 0x80482a8));

        // Read library name
        Optional<String> libName = dynamic.getDynamicLibraryName(results.get(0));

        assertThat(libName)
                .isPresent()
                .hasValue("libc.so.6");
    }

    @Test
    void elf32_gnu_hash_section() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeGnuHash =
                elfFile.sectionOfType(ElfSectionType.GNU_HASH);

        assertThat(maybeGnuHash)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfGnuHashSection.class);
                });

        ElfGnuHashSection<Integer> gnuHash = (ElfGnuHashSection<Integer>)maybeGnuHash.get();
        ElfGnuHashTable<Integer> hashTable = gnuHash.gnuHashTable();

        assertThat(hashTable.findSymbol("blah"))
                .isEmpty();

        assertThat(hashTable.findSymbol("_IO_stdin_used").get().name())
                .isEqualTo("_IO_stdin_used");

        // TODO: Test with library exporting more than 1 symbol
    }

    @Test
    void elf32_read_comment_section_contents() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        Optional<ElfSection<Integer>> maybeComment = elfFile.sectionWithName(ElfSectionNames.COMMENT);

        assertThat(maybeComment)
                .isPresent();

        ElfSection<Integer> comment = maybeComment.get();
        List<String> comments = comment.readContentsAsStrings();

        assertThat(comments)
                .hasSize(1)
                .contains("GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.12) 5.4.0 20160609");
    }

    @Test
    void elf32_read_symbol_versions() {
        // TODO: Add as32BitElf() as64BitElf()
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        ElfGnuVersionSection<Integer> gnuVersion = elfFile.sectionWithName(ElfSectionNames.GNU_VERSION)
                .map(ElfSection::asGnuVersionSection)
                .get();

        List<ElfSymbolVersion> versions = gnuVersion.symbolVersions();
        assertThat(versions)
                .isEqualTo(List.of(
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.fromValue((short) 2),
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.fromValue((short) 2),
                        ElfSymbolVersion.GLOBAL
                ));
    }

    @Test
    void elf32_read_symbol_requirements() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(helloWorld32);

        ElfGnuVersionRequirementsSection<Integer> gnuVersion = elfFile.sectionWithName(ElfSectionNames.GNU_VERSION_R)
                .map(ElfSection::asGnuVersionRequirementsSection)
                .get();

        List<ElfVersionNeeded<Integer>> versions = gnuVersion.requirements();
        assertThat(versions).hasSize(1);

        ElfVersionNeeded<Integer> neededEntry = versions.get(0);
        assertThat(neededEntry.version())
                .isEqualTo(ElfVersionNeededRevision.CURRENT);
        assertThat((int) neededEntry.numberOfAuxiliaryEntries())
                .isEqualTo(1);
        assertThat(neededEntry.fileName())
                .isEqualTo("libc.so.6");
        assertThat(neededEntry.offsetAuxiliaryEntries())
                .isEqualTo(16);
        assertThat(neededEntry.offsetNextEntry())
                .isEqualTo(0);

        List<ElfVersionNeededAuxiliary<Integer>> auxiliaryEntries = neededEntry.auxiliaryEntries();
        assertThat(auxiliaryEntries).hasSize(1);

        ElfVersionNeededAuxiliary<Integer> auxiliaryEntry = auxiliaryEntries.get(0);
        assertThat(auxiliaryEntry.hash())
                .isEqualTo(ElfHashTable.elfHash("GLIBC_2.0"));
        assertThat((int) auxiliaryEntry.flags())
                .isEqualTo(0);
        assertThat(auxiliaryEntry.other())
                .isEqualTo(ElfSymbolVersion.fromValue((short) 2));
        assertThat(auxiliaryEntry.name())
                .isEqualTo("GLIBC_2.0");
        assertThat(auxiliaryEntry.offsetNext())
                .isEqualTo(0);
    }

    @Test
    void elf32_read_symbol_definitions() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(libc32);

        ElfGnuVersionDefinitionsSection<Integer> section = elfFile
                .sectionWithName(ElfSectionNames.GNU_VERSION_D)
                .map(ElfSection::asGnuVersionDefinitionsSection)
                .get();

        List<ElfVersionDefinition<Integer>> definitions = section.definitions();
        assertThat(definitions).hasSize(44);

        ElfVersionDefinition<Integer> def3 = definitions.get(2);
        assertThat(def3.version())
                .isEqualTo(ElfVersionDefinitionRevision.CURRENT);
        assertThat((int)def3.flags())
                .isEqualTo(0);
        assertThat(def3.versionIndex())
                .isEqualTo(ElfSymbolVersion.fromValue((short)3));
        assertThat((int)def3.numberOfAuxiliaryEntries())
                .isEqualTo(2);

        // First aux entry contains definition name
        assertThat(def3.nameHash())
                .isEqualTo(ElfHashTable.elfHash("GLIBC_2.1"));

        assertThat(def3.offsetAuxiliary())
                .isEqualTo(20);
        assertThat(def3.offsetNext())
                .isEqualTo(36);

        List<ElfVersionDefinitionAuxiliary<Integer>> auxEntries = def3.auxiliaryEntries();
        assertThat(auxEntries).hasSize(2);

        ElfVersionDefinitionAuxiliary<Integer> nameAuxEntry = auxEntries.get(0);
        assertThat(nameAuxEntry.name())
                .isEqualTo("GLIBC_2.1");
        assertThat(nameAuxEntry.offsetNext())
                .isEqualTo(8);

        ElfVersionDefinitionAuxiliary<Integer> parentEntry = auxEntries.get(1);
        assertThat(parentEntry.name())
                .isEqualTo("GLIBC_2.0");
        assertThat(parentEntry.offsetNext())
                .isEqualTo(0);
    }

    @Test
    void elf32_hash_table() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(libc32);

        ElfHashSection<Integer> hashSection = elfFile
                .sectionOfType(ElfSectionType.HASH)
                .get()
                .asHashSection();

        ElfHashTable<Integer> hashTable = hashSection.hashTable();

        assertThat(hashTable.findSymbol("_dl_argv"))
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value.name())
                            .isEqualTo("_dl_argv");
                });

        assertThat(hashTable.findSymbol("free"))
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value.name())
                            .isEqualTo("free");
                });

        assertThat(hashTable.findSymbol("not-existing-function"))
                .isEmpty();
    }

    @Test
    void elf32_gnu_warning() {
        ElfFile<Integer> elfFile = ElfReader.readElf32(libc32);

        ElfGnuWarningSection<Integer> warningSection = elfFile
                .sectionWithName(".gnu.warning.gets")
                .get()
                .asGnuWarningSection();

        assertThat(warningSection.warning())
                .isEqualTo("the `gets' function is dangerous and should not be used.");
    }
}
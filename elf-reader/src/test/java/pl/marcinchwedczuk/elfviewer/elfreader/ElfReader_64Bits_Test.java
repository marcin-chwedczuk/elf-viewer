package pl.marcinchwedczuk.elfviewer.elfreader;

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
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32.Intel386RelocationType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfAddress;
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

public class ElfReader_64Bits_Test {
    private final AbstractFile helloWorld64;
    private final AbstractFile libc64;
    private final AbstractFile ld64;
    private final AbstractFile arm64libc;

    public ElfReader_64Bits_Test() throws IOException {
        helloWorld64 = new InMemoryFile(this.getClass()
                .getResourceAsStream("hello-world-64")
                .readAllBytes());

        libc64 = new InMemoryFile(this.getClass()
                .getResourceAsStream("libc-2.17.so")
                .readAllBytes());

        ld64 = new InMemoryFile(this.getClass()
                .getResourceAsStream("ld-2.17.so")
                .readAllBytes());

        arm64libc = new InMemoryFile(this.getClass()
                .getResourceAsStream("arm64-libc-2.33.so")
                .readAllBytes());
    }

    @Test
    public void elf64_header() {
        ElfHeader<Long> header = ElfReader.readElf64(helloWorld64).header();
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

        assertThat((int) identification.osAbiVersion())
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
                .isEqualTo(new ElfAddress<>(0x1050L));

        // offset into ELF file
        assertThat(header.programHeaderTableOffset())
                .isEqualTo(new ElfOffset<>(64L));

        // offset into ELF file
        assertThat(header.sectionHeaderTableOffset())
                .isEqualTo(new ElfOffset<>(14744L));

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
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);
        Optional<ElfSection<Long>> maybeTextSection = elfFile.sectionWithName(ElfSectionNames.TEXT);

        assertThat(maybeTextSection)
                .isPresent();
        ElfSectionHeader<Long> textSection = maybeTextSection
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
                .isEqualTo(new ElfAddress<>(0x1050L));

        assertThat(textSection.fileOffset())
                .isEqualTo(new ElfOffset<>(0x1050L));

        assertThat(textSection.size())
                .isEqualTo(0x181);
    }

    @Test
    void elf64_symbol_table() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeSymtabSection =
                elfFile.sectionWithName(ElfSectionNames.SYMTAB);

        assertThat(maybeSymtabSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfSymbolTableSection.class);
                });

        ElfSymbolTable<Long> symbols = ((ElfSymbolTableSection<Long>) maybeSymtabSection.get()).symbolTable();

        // 1. Check Section symbols have their names resolved
        Optional<ElfSymbol<Long>> textSectionSymbol = symbols.slowlyFindSymbolByName(".text");
        assertThat(textSectionSymbol)
                .isPresent();

        // 2. Check symbol for 'main' is defined and all values are set
        Optional<ElfSymbol<Long>> maybeMain = symbols.slowlyFindSymbolByName("main");
        assertThat(maybeMain)
                .isPresent()
                .hasValueSatisfying(main -> {
                    assertThat(main.binding())
                            .isEqualTo(GLOBAL);
                    assertThat(main.symbolType())
                            .isEqualTo(FUNCTION);

                    assertThat(main.other())
                            .isEqualTo((byte) 0);
                    assertThat(main.visibility())
                            .isEqualTo(DEFAULT);

                    assertThat(main.size())
                            .isEqualTo(34);
                    assertThat(main.value())
                            .isEqualTo(new ElfAddress<>(0x0000000000001135L));

                    // Check section header index, it should point to .text section
                    assertThat(main.index())
                            .isEqualTo(new SectionHeaderIndex(14));
                    assertThat(elfFile.sectionHeaders().get(14).name())
                            .isEqualTo(".text");
                });
    }

    @Test
    void elf64_relocation_table() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeRelSection =
                elfFile.sectionWithName(ElfSectionNames.RELA(".dyn"));

        assertThat(maybeRelSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfRelocationAddendSection.class);
                });

        ElfRelocationAddendSection<Long> relSection = ((ElfRelocationAddendSection<Long>) maybeRelSection.get());
        List<ElfRelocationAddend<Long>> relocations = relSection.relocations();

        assertThat(relocations.size())
                .isEqualTo(8);

        ElfRelocationAddend<Long> relocation = relocations.get(0);
        assertThat(relocation.offset())
                .isEqualTo(new ElfAddress<>(0x000000003de8L));

        assertThat(relocation.info())
                .isEqualTo(0x000000000008);

        assertThat(relocation.intel386RelocationType())
                .isEqualTo(Intel386RelocationType.RELATIVE);

        assertThat(relocation.addend())
                .isEqualTo(0x1130);
    }

    @Test
    void elf64_segment_header() {
        List<ElfSegment<Long>> segments = ElfReader.readElf64(helloWorld64)
                .segments();

        assertThat(segments.size())
                .isEqualTo(11);

        ElfProgramHeader<Long> textSegment = segments.get(3).programHeader();

        assertThat(textSegment.type())
                .isEqualTo(ElfSegmentType.LOAD);

        assertThat(textSegment.fileOffset())
                .isEqualTo(new ElfOffset<>(0x0000000000001000L));
        assertThat(textSegment.virtualAddress())
                .isEqualTo(new ElfAddress<>(0x0000000000001000L));
        assertThat(textSegment.physicalAddress())
                .isEqualTo(new ElfAddress<>(0x0000000000001000L));

        assertThat(textSegment.fileSize())
                .isEqualTo(0x00000000000001dd);
        assertThat(textSegment.memorySize())
                .isEqualTo(0x00000000000001dd);

        assertThat(textSegment.flags())
                .isEqualTo(new ElfSegmentFlags(
                        ElfSegmentFlags.Readable,
                        ElfSegmentFlags.Executable));

        assertThat(textSegment.alignment())
                .isEqualTo(0x1000);
    }

    @Test
    void elf64_notes_abi_tag() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_ABI_TAG);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfNotesSection.class);
                });

        ElfNotesSection<Long> notesSection = (ElfNotesSection<Long>) maybeNotesSection.get();

        ElfNoteGnuABITag gnuAbi = (ElfNoteGnuABITag) notesSection.notes().get(0);
        assertThat(gnuAbi.minSupportedKernelVersion())
                .isEqualTo("3.2.0");
    }

    @Test
    void elf64_notes_build_id() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeNotesSection = elfFile.sectionWithName(ElfSectionNames.NOTE_GNU_BUILD_ID);
        assertThat(maybeNotesSection)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfNotesSection.class);
                });

        ElfNotesSection<Long> notesSection = (ElfNotesSection<Long>) maybeNotesSection.get();

        ElfNoteGnuBuildId buildId = (ElfNoteGnuBuildId) notesSection.notes().get(0);
        assertThat(buildId.buildId())
                .isEqualTo("cfc42fc9d9070c324115c2bb9e55fff59f2a86d6");
    }

    @Test
    void elf64_interpreter() {
        ElfFile<Long> elfFile = (ElfFile<Long>) ElfReader.readElf(helloWorld64);

        Optional<ElfSection<Long>> maybeInterp = elfFile.sectionWithName(ElfSectionNames.INTERP);
        assertThat(maybeInterp)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfInterpreterSection.class);
                });

        ElfInterpreterSection<Long> interp = ((ElfInterpreterSection<Long>) maybeInterp.get());

        assertThat(interp.interpreterPath())
                .isEqualTo("/lib64/ld-linux-x86-64.so.2");
    }

    @Test
    void elf64_dynamic_section() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeDynamic =
                elfFile.sectionOfType(ElfSectionType.DYNAMIC);

        assertThat(maybeDynamic)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfDynamicSection.class);
                });

        ElfDynamicSection<Long> dynamic = (ElfDynamicSection<Long>) maybeDynamic.get();
        List<ElfDynamicTag<Long>> results = dynamic.dynamicTags();

        assertThat(results.get(0))
                .isEqualTo(new ElfDynamicTag<>(NEEDED, 39L));

        assertThat(results.get(1))
                .isEqualTo(new ElfDynamicTag<>(INIT, 0x1000L));

        // Read library name
        Optional<String> libName = dynamic.getDynamicLibraryName(results.get(0));

        assertThat(libName)
                .isPresent()
                .hasValue("libc.so.6");
    }

    @Test
    void elf32_gnu_hash_section() {
        // TODO: https://flapenguin.me/elf-dt-gnu-hash
        // TODO: https://binutils.sourceware.narkive.com/6VzWCiUN/gnu-hash-section-format
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeGnuHash =
                elfFile.sectionOfType(ElfSectionType.GNU_HASH);

        assertThat(maybeGnuHash)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value)
                            .isInstanceOf(ElfGnuHashSection.class);
                });

        ElfGnuHashSection<Long> gnuHash = (ElfGnuHashSection<Long>) maybeGnuHash.get();
        ElfGnuHashTable<Long> hashTable = gnuHash.gnuHashTable();

        assertThat(hashTable.findSymbol("blah"))
                .isEmpty();

        assertThat(hashTable.findSymbol("__cxa_finalize").get().name())
                .isEqualTo("__cxa_finalize");
    }

    @Test
    void efl64_read_comment_section_contents() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        Optional<ElfSection<Long>> maybeComment = elfFile.sectionWithName(ElfSectionNames.COMMENT);

        assertThat(maybeComment)
                .isPresent();

        ElfSection<Long> comment = maybeComment.get();
        List<String> comments = comment.readContentsAsStrings();

        assertThat(comments)
                .hasSize(1)
                .contains("GCC: (Debian 10.2.1-6) 10.2.1 20210110");
    }

    @Test
    void elf64_read_symbol_versions() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        ElfGnuVersionSection<Long> gnuVersion = elfFile.sectionWithName(ElfSectionNames.GNU_VERSION)
                .map(ElfSection::asGnuVersionSection)
                .get();

        List<ElfSymbolVersion> versions = gnuVersion.symbolVersions();
        assertThat(versions)
                .isEqualTo(List.of(
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.fromValue((short) 2),
                        ElfSymbolVersion.fromValue((short) 2),
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.LOCAL,
                        ElfSymbolVersion.fromValue((short) 2)
                ));
    }

    @Test
    void elf64_read_symbol_requirements() {
        ElfFile<Long> elfFile = ElfReader.readElf64(helloWorld64);

        ElfGnuVersionRequirementsSection<Long> gnuVersion = elfFile.sectionWithName(ElfSectionNames.GNU_VERSION_R)
                .map(ElfSection::asGnuVersionRequirementsSection)
                .get();

        List<ElfVersionNeeded<Long>> versions = gnuVersion.requirements();
        assertThat(versions).hasSize(1);

        ElfVersionNeeded<Long> neededEntry = versions.get(0);
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

        List<ElfVersionNeededAuxiliary<Long>> auxiliaryEntries = neededEntry.auxiliaryEntries();
        assertThat(auxiliaryEntries).hasSize(1);

        ElfVersionNeededAuxiliary<Long> auxiliaryEntry = auxiliaryEntries.get(0);
        assertThat(auxiliaryEntry.hash())
                .isEqualTo(ElfHashTable.elfHash("GLIBC_2.2.5"));
        assertThat((int) auxiliaryEntry.flags())
                .isEqualTo(0);
        assertThat(auxiliaryEntry.other())
                .isEqualTo(ElfSymbolVersion.fromValue((short) 2));
        assertThat(auxiliaryEntry.name())
                .isEqualTo("GLIBC_2.2.5");
        assertThat(auxiliaryEntry.offsetNext())
                .isEqualTo(0);
    }

    @Test
    void elf64_read_symbol_definitions() {
        ElfFile<Long> elfFile = ElfReader.readElf64(libc64);

        ElfGnuVersionDefinitionsSection<Long> section = elfFile
                .sectionWithName(ElfSectionNames.GNU_VERSION_D)
                .map(ElfSection::asGnuVersionDefinitionsSection)
                .get();

        List<ElfVersionDefinition<Long>> definitions = section.definitions();
        assertThat(definitions).hasSize(22);

        ElfVersionDefinition<Long> def3 = definitions.get(3);
        assertThat(def3.version())
                .isEqualTo(ElfVersionDefinitionRevision.CURRENT);
        assertThat((int)def3.flags())
                .isEqualTo(0);
        assertThat(def3.versionIndex())
                .isEqualTo(ElfSymbolVersion.fromValue((short)4));
        assertThat((int)def3.numberOfAuxiliaryEntries())
                .isEqualTo(2);

        // First aux entry contains definition name
        assertThat(def3.nameHash())
                .isEqualTo(ElfHashTable.elfHash("GLIBC_2.3"));

        assertThat(def3.offsetAuxiliary())
                .isEqualTo(20);
        assertThat(def3.offsetNext())
                .isEqualTo(36);

        List<ElfVersionDefinitionAuxiliary<Long>> auxEntries = def3.auxiliaryEntries();
        assertThat(auxEntries).hasSize(2);

        ElfVersionDefinitionAuxiliary<Long> nameAuxEntry = auxEntries.get(0);
        assertThat(nameAuxEntry.name())
                .isEqualTo("GLIBC_2.3");
        assertThat(nameAuxEntry.offsetNext())
                .isEqualTo(8);

        ElfVersionDefinitionAuxiliary<Long> parentEntry = auxEntries.get(1);
        assertThat(parentEntry.name())
                .isEqualTo("GLIBC_2.2.6");
        assertThat(parentEntry.offsetNext())
                .isEqualTo(0);
    }

    @Test
    void elf64_hash_table() {
        ElfFile<Long> elfFile = ElfReader.readElf64(ld64);

        ElfHashSection<Long> hashSection = elfFile
                .sectionOfType(ElfSectionType.HASH)
                .get()
                .asHashSection();

        ElfHashTable<Long> hashTable = hashSection.hashTable();

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
    void elf64_gnu_warning() {
        ElfFile<Long> elfFile = ElfReader.readElf64(libc64);

        ElfGnuWarningSection<Long> warningSection = elfFile
                .sectionWithName(".gnu.warning.gets")
                .get()
                .asGnuWarningSection();

        assertThat(warningSection.warning())
                .isEqualTo("the `gets' function is dangerous and should not be used.");
    }
}

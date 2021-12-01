package pl.marcinchwedczuk.elfviewer.elfreader;

public class ElfSectionNames {
    /**
     * This section holds uninitialized data that contributes to
     * the program's memory image.  By definition, the system
     * initializes the data with zeros when the program begins to
     * run.  This section is of type SHT_NOBITS. The attribute
     * types are SHF_ALLOC and SHF_WRITE.
     */
    public static final String BSS = ".bss";

    /**
     * This section holds version control information.  This
     * section is of type SHT_PROGBITS.  No attribute types are
     * used.
     */
    public static final String COMMENT = ".comment";

    /**
     * This section holds initialized pointers to the C++
     * constructor functions.  This section is of type
     * SHT_PROGBITS.  The attribute types are SHF_ALLOC and
     * SHF_WRITE.
     */
    public static final String CTORS = ".ctors";

    /**
     * This section holds initialized data that contribute to the
     * program's memory image.  This section is of type
     * SHT_PROGBITS.  The attribute types are SHF_ALLOC and
     * SHF_WRITE.
     */
    public static final String DATA = ".data";

    /**
     * This section holds initialized data that contribute to the
     * program's memory image.  This section is of type
     * SHT_PROGBITS.  The attribute types are SHF_ALLOC and
     * SHF_WRITE.
     */
    public static final String DATA1 = ".data1";

    /**
     * This section holds information for symbolic debugging.
     * The contents are unspecified.  This section is of type
     * SHT_PROGBITS.  No attribute types are used.
     */
    public static final String DEBUG = ".debug";

    /**
     * This section holds initialized pointers to the C++
     * destructor functions.  This section is of type
     * SHT_PROGBITS.  The attribute types are SHF_ALLOC and
     * SHF_WRITE.
     */
    public static final String DTORS = ".dtors";

    /**
     * This section holds dynamic linking information.  The
     * section's attributes will include the SHF_ALLOC bit.
     * Whether the SHF_WRITE bit is set is processor-specific.
     * This section is of type SHT_DYNAMIC.  See the attributes
     * above.
     */
    public static final String DYNAMIC = ".dynamic";

    /**
     * This section holds strings needed for dynamic linking,
     * most commonly the strings that represent the names
     * associated with symbol table entries.  This section is of
     * type SHT_STRTAB.  The attribute type used is SHF_ALLOC.
     */
    public static final String DYNSTR = ".dynstr";

    /**
     * This section holds the dynamic linking symbol table.  This
     * section is of type SHT_DYNSYM.  The attribute used is
     * SHF_ALLOC.
     */
    public static final String DYNSYM = ".dynsym";

    /**
     * This section holds executable instructions that contribute
     * to the process termination code.  When a program exits
     * normally the system arranges to execute the code in this
     * section.  This section is of type SHT_PROGBITS.  The
     * attributes used are SHF_ALLOC and SHF_EXECINSTR.
     */
    public static final String FINI = ".fini";

    /**
     * This section holds the global offset table.  This section
     * is of type SHT_PROGBITS.  The attributes are processor-
     * specific.
     */
    public static final String GOT = ".got";

    /**
     * This section holds a symbol hash table.  This section is
     * of type SHT_HASH.  The attribute used is SHF_ALLOC.
     */
    public static final String HASH = ".hash";

    /**
     * This section holds executable instructions that contribute
     * to the process initialization code.  When a program starts
     * to run the system arranges to execute the code in this
     * section before calling the main program entry point.  This
     * section is of type SHT_PROGBITS.  The attributes used are
     * SHF_ALLOC and SHF_EXECINSTR.
     */
    public static final String INIT = ".init";

    /**
     * This section holds the pathname of a program interpreter.
     * If the file has a loadable segment that includes the
     * section, the section's attributes will include the
     * SHF_ALLOC bit.  Otherwise, that bit will be off.  This
     * section is of type SHT_PROGBITS.
     */
    public static final String INTERP = ".interp";

    /**
     * This section holds line number information for symbolic
     * debugging, which describes the correspondence between the
     * program source and the machine code.  The contents are
     * unspecified.  This section is of type SHT_PROGBITS.  No
     * attribute types are used.
     */
    public static final String LINE = ".line";

    /**
     * This section holds various notes.  This section is of type
     * SHT_NOTE.  No attribute types are used.
     */
    public static final String NOTE = ".note";

    /**
     * This section is used to declare the expected run-time ABI
     * of the ELF image.  It may include the operating system
     * name and its run-time versions.  This section is of type
     * SHT_NOTE.  The only attribute used is SHF_ALLOC.
     */
    public static final String NOTE_ABI_TAG = ".note.ABI-tag";

    /**
     * This section is used to hold an ID that uniquely
     * identifies the contents of the ELF image.  Different files
     * with the same build ID should contain the same executable
     * content.  See the --build-id option to the GNU linker (ld
     * (1)) for more details.  This section is of type SHT_NOTE.
     * The only attribute used is SHF_ALLOC.
     */
    public static final String NOTE_GNU_BUILD_ID = ".note.gnu.build-id";

    /**
     * This section is used in Linux object files for declaring
     * stack attributes.  This section is of type SHT_PROGBITS.
     * The only attribute used is SHF_EXECINSTR.  This indicates
     * to the GNU linker that the object file requires an
     * executable stack.
     */
    public static final String NOTE_GNU_STACK = ".note.GNU-stack";

    /**
     * OpenBSD native executables usually contain this section to
     * identify themselves so the kernel can bypass any
     * compatibility ELF binary emulation tests when loading the
     * file.
     */
    public static final String NOTE_OPENBSD_IDENT = ".note.GNU-stack";

    /**
     * This section holds the procedure linkage table.  This
     * section is of type SHT_PROGBITS.  The attributes are
     * processor-specific.
     */
    public static final String PLT = ".plt";

    /**
     * This section holds relocation information as described
     * below.  If the file has a loadable segment that includes
     * relocation, the section's attributes will include the
     * SHF_ALLOC bit.  Otherwise, the bit will be off.  By
     * convention, "NAME" is supplied by the section to which the
     * relocations apply.  Thus a relocation section for .text
     * normally would have the name .rel.text.  This section is
     * of type SHT_REL.
     */
    public static final String REL_PREFIX = ".rel";

    public static String REL(String forSection) {
        return REL_PREFIX + forSection;
    }

    /**
     * This section holds relocation information as described
     * below.  If the file has a loadable segment that includes
     * relocation, the section's attributes will include the
     * SHF_ALLOC bit.  Otherwise, the bit will be off.  By
     * convention, "NAME" is supplied by the section to which the
     * relocations apply.  Thus a relocation section for .text
     * normally would have the name .rela.text.  This section is
     * of type SHT_RELA.
     */
    public static final String RELA_PREFIX = ".rela";

    /**
     * This section holds read-only data that typically
     * contributes to a nonwritable segment in the process image.
     * This section is of type SHT_PROGBITS.  The attribute used
     * is SHF_ALLOC.
     */
    public static final String RO_DATA = ".rodata";

    /**
     * This section holds read-only data that typically
     * contributes to a nonwritable segment in the process image.
     * This section is of type SHT_PROGBITS.  The attribute used
     * is SHF_ALLOC.
     */
    public static final String RO_DATA1 = ".rodata1";

    /**
     * This section holds section names.  This section is of type
     * SHT_STRTAB.  No attribute types are used.
     */
    public static final String SH_STR_TAB = ".shstrtab";

    /**
     * This section holds strings, most commonly the strings that
     * represent the names associated with symbol table entries.
     * If the file has a loadable segment that includes the
     * symbol string table, the section's attributes will include
     * the SHF_ALLOC bit.  Otherwise, the bit will be off.  This
     * section is of type SHT_STRTAB.
     */
    public static final String STRTAB = ".strtab";

    /**
     * This section holds a symbol table.  If the file has a
     * loadable segment that includes the symbol table, the
     * section's attributes will include the SHF_ALLOC bit.
     * Otherwise, the bit will be off.  This section is of type
     * SHT_SYMTAB.
     */
    public static final String SYMTAB = ".symtab";

    /**
     * This section holds the "text", or executable instructions,
     * of a program.  This section is of type SHT_PROGBITS.  The
     * attributes used are SHF_ALLOC and SHF_EXECINSTR.
     */
    public static final String TEXT = ".text";

    public static final String GNU_HASH = ".gnu.hash";

    /**
     * This section holds the version symbol table, an array of
     * ElfN_Half elements.  This section is of type
     * SHT_GNU_versym.  The attribute type used is SHF_ALLOC.
     */
    public static final String GNU_VERSION = ".gnu.version";

    /**
     * This section holds the version symbol definitions, a table
     * of ElfN_Verdef structures.  This section is of type
     * SHT_GNU_verdef.  The attribute type used is SHF_ALLOC.
     */
    public static final String GNU_VERSION_D = ".gnu.version_d";

    /**
     * This section holds the version symbol needed elements, a
     * table of ElfN_Verneed structures.  This section is of type
     * SHT_GNU_versym.  The attribute type used is SHF_ALLOC.
     */
    public static final String GNU_VERSION_R = ".gnu.version_r";
}

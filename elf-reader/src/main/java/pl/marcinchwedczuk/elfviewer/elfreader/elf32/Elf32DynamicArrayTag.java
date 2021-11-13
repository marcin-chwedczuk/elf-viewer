package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

public enum Elf32DynamicArrayTag {
    /**
     * An entry with a DT_NULL tag marks the end of the _DYNAMIC array.
     */
    @ElfApi("DT_NULL")
    NULL(0),

    /**
     * This element holds the string table offset of a null-terminated string,
     * giving the name of a needed library. The offset is an index into the table
     * recorded in the DT_STRTAB entry.
     * See "Shared Object Dependencies'' for more information about these names.
     * The dynamic array may contain multiple entries with this type.
     * These entries' relative order is significant,
     * though their relation to entries of other types is not.
     */
    @ElfApi("DT_NEEDED")
    NEEDED(1),

    /**
     * This element holds the total size, in bytes,
     * of the relocation entries associated with the procedure linkage table.
     * If an entry of type DT_JMPREL is present, a DT_PLTRELSZ must accompany it.
     */
    @ElfApi("DT_PLTRELSZ")
    PLT_REL_SZ(2),

    /**
     * This element holds an address associated with the procedure linkage
     * table and/or the global offset table.
     */
    @ElfApi("DT_PLTGOT")
    PLT_GOT(3),

    /**
     * This element holds the address of the symbol hash table,
     * described in "Hash Table".
     * This hash table refers to the symbol table referenced by
     * the DT_SYMTAB element.
     */
    @ElfApi("DT_HASH")
    HASH(4),

    /**
     * This element holds the address of the string table, described in Chapter 1.
     * Symbol names, library names, and other strings reside in this table.
     */
    @ElfApi("DT_STRTAB")
    STR_TAB(5),

    /**
     * This element holds the address of the symbol table, described in Chapter 1,
     * with Elf32_Sym entries for the 32-bit class of files.
     */
    @ElfApi("DT_SYMTAB")
    SYM_TAB(6),

    /**
     * This element holds the address of a relocation table, described in Chapter 1.
     * Entries in the table have explicit addends,
     * such as Elf32_Rela for the 32-bit file class.
     * An object file may have multiple relocation sections.
     * When building the relocation table for an executable or shared object file,
     * the link editor catenates those sections to form a single table.
     * Although the sections remain independent in the object file,
     * the dynamic linker sees a single table. When the dynamic linker creates
     * the process image for an executable file or adds a shared object to the
     * process image, it reads the relocation table and performs the associated
     * actions. If this element is present, the dynamic structure must
     * also have DT_RELASZ and DT_RELAENT elements.
     * When relocation is "mandatory" for a file, either DT_RELA or DT_REL may occur
     * (both are permitted but not required).
     */
    @ElfApi("DT_RELA")
    RELA(7),

    /**
     * This element holds the total size, in bytes, of the DT_RELA relocation table.
     */
    @ElfApi("DT_RELASZ")
    RELA_SZ(8),

    /**
     * This element holds the size, in bytes, of the DT_RELA relocation entry.
     */
    @ElfApi("DT_RELAENT")
    RELA_ENT(9),

    /**
     * This element holds the size, in bytes, of the string table.
     */
    @ElfApi("DT_STRSZ")
    STRSZ(10),

    /**
     * This element holds the size, in bytes, of a symbol table entry.
     */
    @ElfApi("DT_SYMENT")
    SYMENT(11),

    /**
     * This element holds the address of the initialization function,
     * discussed in "Initialization and Termination Functions" below.
     */
    @ElfApi("DT_INIT")
    INIT(12),

    /**
     * This element holds the address of the termination function,
     * discussed in "Initialization and Termination Functions" below.
     */
    @ElfApi("DT_FINI")
    FINI(13),

    /**
     * This element holds the string table offset of a null-terminated string,
     * giving the name of the shared object. The offset is an index into
     * the table recorded intheDT_STRTABentry.
     * See "SharedObjectDependencies" below for more information about these names.
     */
    @ElfApi("DT_SONAME")
    SONAME(14),

    /**
     * This element holds the string table offset of a null-terminated search
     * library search path string, discussed in "Shared Object Dependencies".
     * The offset is an index into the table recorded in the DT_STRTAB entry.
     */
    @ElfApi("DT_RPATH")
    RPATH(15),

    /**
     * This element's presence in a shared object library alters the dynamic
     * linker's symbol resolution algorithm for references within the library.
     * Instead of starting a symbol search with the executable file,
     * the dynamic linker starts from the shared object itself.
     * If the shared object fails to supply the referenced symbol,
     * the dynamic linker then searches the executable file and other
     * shared objects as usual.
     */
    @ElfApi("DT_SYMBOLIC")
    SYMBOLIC(16),

    /**
     * This element is similar to DT_RELA, except its table has implicit addends,
     * such as Elf32_Rel for the 32-bit file class.
     * If this element is present, the dynamic structure must also have
     * DT_RELSZ and DT_RELENT elements.
     */
    @ElfApi("DT_REL")
    REL(17),

    /**
     * This element holds the total size, in bytes, of the DT_REL relocation table.
     */
    @ElfApi("DT_RELSZ")
    RELSZ(18),

    /**
     * This element holds the size, in bytes, of the DT_REL relocation entry.
     */
    @ElfApi("DT_RELENT")
    RELENT(19),

    /**
     * This member specifies the type of relocation entry to which
     * the procedure linkage table refers. The d_val member holds DT_REL or
     * DT_RELA, as appropriate. All relocations in a procedure linkage
     * table must use the same relocation.
     */
    @ElfApi("DT_PLTREL")
    PLTREL(20),

    /**
     * This member is used for debugging.
     * Its contents are not specified in this document.
     */
    @ElfApi("DT_DEBUG")
    DEBUG(21),

    /**
     * This member's absence signifies that no relocation entry should cause a
     * modification to a non-writable segment, as specified by the segment
     * permissions in the program header table. If this member is present,
     * one or more relocation entries might request modifications to a non-writable
     * segment, and the dynamic linker can prepare accordingly.
     */
    @ElfApi("DT_TEXTREL")
    TEXTREL(22),

    /**
     * If present, this entries d_ptr member holds the address of relocation
     * entries associated solely with the procedure linkage table.
     * Separating these relocation entries lets the dynamic linker ignore them
     * during process initialization, if lazy binding is enabled. If this entry
     * is present, the related entries of types DT_PLTRELSZ and DT_PLTREL
     * must also be present.
     */
    @ElfApi("DT_JMPREL")
    JMPREL(23),

    /**
     * If present in a shared object or executable, this entry instructs the dynamic
     * linker to process all relocations for the object containing this entry
     * before transferring control to the program. The presence of this entry
     * takes precedence over a directive to use lazy binding for this object
     * when specified through the environment or via dlopen(BA_LIB).
     */
    @ElfApi("DT_BIND_NOW")
    BIND_NOW(24),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     * If meanings are specified, the processor supplement explains them.
     */
    @ElfApi("DT_LOPROC")
    LoProcessorSpecific(0x70000000),

    /**
     * Values in this inclusive range are reserved for processor-specific semantics.
     * If meanings are specified, the processor supplement explains them.
     */
    @ElfApi("DT_HIPROC")
    HiProcessorSpecific(0x7fffffff);

    private final int value;

    Elf32DynamicArrayTag(int v) {
        this.value = v;
    }
}

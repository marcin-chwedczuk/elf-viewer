package pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.BytePartialEnum;

import java.util.Collection;
import java.util.Map;

// TODO: Add docs, see page 57 of pdf in ./docs directory
public class Intel386RelocationType extends BytePartialEnum<Intel386RelocationType> {
    private static final Map<Byte, Intel386RelocationType> byValue = mkByValueMap();
    private static final Map<String, Intel386RelocationType> byName = mkByNameMap();

    /* No reloc */
    @ElfApi("R_NONE")
    public static final Intel386RelocationType NONE = new Intel386RelocationType(b(0), "NONE");

    /* Direct 32 bit  */
    @ElfApi("R_32")
    public static final Intel386RelocationType R32 = new Intel386RelocationType(b(1), "R32");

    /* PC relative 32 bit */
    @ElfApi("R_PC32")
    public static final Intel386RelocationType PC32 = new Intel386RelocationType(b(2), "PC32");

    /* 32 bit GOT entry */
    @ElfApi("R_GOT32")
    public static final Intel386RelocationType GOT32 = new Intel386RelocationType(b(3), "GOT32");

    /* 32 bit PLT address */
    @ElfApi("R_PLT32")
    public static final Intel386RelocationType PLT32 = new Intel386RelocationType(b(4), "PLT32");

    /* Copy symbol at runtime */
    @ElfApi("R_COPY")
    public static final Intel386RelocationType COPY = new Intel386RelocationType(b(5), "COPY");

    /* Create GOT entry */
    @ElfApi("R_GLOB_DAT")
    public static final Intel386RelocationType GLOB_DAT = new Intel386RelocationType(b(6), "GLOB_DAT");

    /* Create PLT entry */
    @ElfApi("R_JMP_SLOT")
    public static final Intel386RelocationType JMP_SLOT = new Intel386RelocationType(b(7), "JMP_SLOT");

    /* Adjust by program base */
    @ElfApi("R_RELATIVE")
    public static final Intel386RelocationType RELATIVE = new Intel386RelocationType(b(8), "RELATIVE");

    /* 32 bit offset to GOT */
    @ElfApi("R_GOTOFF")
    public static final Intel386RelocationType GOTOFF = new Intel386RelocationType(b(9), "GOTOFF");

    /* 32 bit PC relative offset to GOT */
    @ElfApi("R_GOTPC")
    public static final Intel386RelocationType GOTPC = new Intel386RelocationType(b(10), "GOTPC");

    @ElfApi("R_32PLT")
    public static final Intel386RelocationType R32PLT = new Intel386RelocationType(b(11), "R32PLT");

    /* Offset in static TLS block */
    @ElfApi("R_TLS_TPOFF")
    public static final Intel386RelocationType TLS_TPOFF = new Intel386RelocationType(b(14), "TLS_TPOFF");

    /* Address of GOT entry for static TLS block offset */
    @ElfApi("R_TLS_IE")
    public static final Intel386RelocationType TLS_IE = new Intel386RelocationType(b(15), "TLS_IE");

    /* GOT entry for static TLS block offset */
    @ElfApi("R_TLS_GOTIE")
    public static final Intel386RelocationType TLS_GOTIE = new Intel386RelocationType(b(16), "TLS_GOTIE");

    /* Offset relative to static TLS block */
    @ElfApi("R_TLS_LE")
    public static final Intel386RelocationType TLS_LE = new Intel386RelocationType(b(17), "TLS_LE");

    /* Direct 32 bit for GNU version of general dynamic thread local data */
    @ElfApi("R_TLS_GD")
    public static final Intel386RelocationType TLS_GD = new Intel386RelocationType(b(18), "TLS_GD");

    /* Direct 32 bit for GNU version of local dynamic thread local data in LE code */
    @ElfApi("R_TLS_LDM")
    public static final Intel386RelocationType TLS_LDM = new Intel386RelocationType(b(19), "TLS_LDM");

    @ElfApi("R_16")
    public static final Intel386RelocationType R16= new Intel386RelocationType(b(20), "R16");

    @ElfApi("R_PC16")
    public static final Intel386RelocationType PC16 = new Intel386RelocationType(b(21), "PC16");

    @ElfApi("R_8")
    public static final Intel386RelocationType R8 = new Intel386RelocationType(b(22), "R8");

    @ElfApi("R_PC8")
    public static final Intel386RelocationType PC8 = new Intel386RelocationType(b(23), "PC8");

    /* Direct 32 bit for general dynamic thread local data */
    @ElfApi("R_TLS_GD_32")
    public static final Intel386RelocationType TLS_GD_32 = new Intel386RelocationType(b(24), "TLS_GD_32");

    /* Tag for pushl in GD TLS code */
    @ElfApi("R_TLS_GD_PUSH")
    public static final Intel386RelocationType TLS_GD_PUSH = new Intel386RelocationType(b(25), "TLS_GD_PUSH");

    /* Relocation for call to __tls_get_addr() */
    @ElfApi("R_TLS_GD_CALL")
    public static final Intel386RelocationType TLS_GD_CALL = new Intel386RelocationType(b(26), "TLS_GD_CALL");

    /* Tag for popl in GD TLS code */
    @ElfApi("R_TLS_GD_POP")
    public static final Intel386RelocationType TLS_GD_POP = new Intel386RelocationType(b(27), "TLS_GD_POP");

    /* Direct 32 bit for local dynamic thread local data in LE code */
    @ElfApi("R_TLS_LDM_32")
    public static final Intel386RelocationType TLS_LDM_32 = new Intel386RelocationType(b(28), "TLS_LDM_32");

    /* Tag for pushl in LDM TLS code */
    @ElfApi("R_TLS_LDM_PUSH")
    public static final Intel386RelocationType TLS_LDM_PUSH = new Intel386RelocationType(b(29), "TLS_LDM_PUSH");

    /* Relocation for call to __tls_get_addr() in LDM code */
    @ElfApi("R_TLS_LDM_CALL")
    public static final Intel386RelocationType TLS_LDM_CALL = new Intel386RelocationType(b(30), "TLS_LDM_CALL");

    /* Tag for popl in LDM TLS code */
    @ElfApi("R_TLS_LDM_POP")
    public static final Intel386RelocationType TLS_LDM_POP = new Intel386RelocationType(b(31), "TLS_LDM_POP");

    /* Offset relative to TLS block */
    @ElfApi("R_TLS_LDO_32")
    public static final Intel386RelocationType TLS_LDO_32 = new Intel386RelocationType(b(32), "TLS_LDO_32");

    /* GOT entry for negated static TLS block offset */
    @ElfApi("R_TLS_IE_32")
    public static final Intel386RelocationType TLS_IE_32 = new Intel386RelocationType(b(33), "TLS_IE_32");

    /* Negated offset relative to static TLS block */
    @ElfApi("R_TLS_LE_32")
    public static final Intel386RelocationType TLS_LE_32 = new Intel386RelocationType(b(34), "TLS_LE_32");

    /* ID of module containing symbol */
    @ElfApi("R_TLS_DTPMOD32")
    public static final Intel386RelocationType TLS_DTPMOD32 = new Intel386RelocationType(b(35), "TLS_DTPMOD32");

    /* Offset in TLS block */
    @ElfApi("R_TLS_DTPOFF32")
    public static final Intel386RelocationType TLS_DTPOFF32 = new Intel386RelocationType(b(36), "TLS_DTPOFF32");

    /* Negated offset in static TLS block */
    @ElfApi("R_TLS_TPOFF32")
    public static final Intel386RelocationType TLS_TPOFF32 = new Intel386RelocationType(b(37), "TLS_TPOFF32");

    /* 32-bit symbol size */
    @ElfApi("R_SIZE32")
    public static final Intel386RelocationType SIZE32 = new Intel386RelocationType(b(38), "SIZE32");

    /* GOT offset for TLS descriptor.  */
    @ElfApi("R_TLS_GOTDESC")
    public static final Intel386RelocationType TLS_GOTDESC = new Intel386RelocationType(b(39), "TLS_GOTDESC");

    /* Marker of call through TLS descriptor for relaxation.  */
    @ElfApi("R_TLS_DESC_CALL")
    public static final Intel386RelocationType TLS_DESC_CALL = new Intel386RelocationType(b(40), "TLS_DESC_CALL");

    /* TLS descriptor containing pointer to code and to argument, returning the TLS offset for the symbol.  */
    @ElfApi("R_TLS_DESC")
    public static final Intel386RelocationType TLS_DESC = new Intel386RelocationType(b(41), "TLS_DESC");

    /* Adjust indirectly by program base */
    @ElfApi("R_IRELATIVE")
    public static final Intel386RelocationType IRELATIVE = new Intel386RelocationType(b(42), "IRELATIVE");

    /* Load from 32 bit GOT entry, relaxable. */
    @ElfApi("R_GOT32X")
    public static final Intel386RelocationType GOT32X = new Intel386RelocationType(b(43), "GOT32X");

    private Intel386RelocationType(byte value) {
        super(value);
    }

    private Intel386RelocationType(byte value, String name) {
        super(value, name, byValue, byName);
    }

    public static Intel386RelocationType fromType(int relocationType) {
        Args.checkByteValue(relocationType);
        return fromValue((byte)relocationType);
    }

    public static Intel386RelocationType fromValue(byte value) {
        return BytePartialEnum.fromValueOrCreate(value, byValue, Intel386RelocationType::new);
    }

    public static Intel386RelocationType fromName(String name) {
        return BytePartialEnum.fromName(name, byName);
    }

    public static Collection<Intel386RelocationType> knownValues() {
        return BytePartialEnum.knownValues(byValue);
    }
}

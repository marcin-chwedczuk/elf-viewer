package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Applicable to Elf files with type {@link pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfType#COREDUMP}.
 */
public class ElfNoteTypeCore extends IntPartialEnum<ElfNoteTypeCore> {
    private static final Map<Integer, ElfNoteTypeCore> byValue = mkByValueMap();
    private static final Map<String, ElfNoteTypeCore> byName = mkByNameMap();

    /* Legal values for note segment descriptor types for core files. */

    /**
     * Contains copy of prstatus struct
     */
    @ElfApi("NT_PRSTATUS")
    public static final ElfNoteTypeCore PRSTATUS = new ElfNoteTypeCore(1, "PRSTATUS");

    /**
     * Contains copy of fpregset struct.
     */
    @ElfApi("NT_PRFPREG")
    public static final ElfNoteTypeCore PRFPREG = new ElfNoteTypeCore(2, "PRFPREG");

    /**
     * Contains copy of fpregset struct
     */
    @ElfApi("NT_FPREGSET")
    public static final ElfNoteTypeCore FPREGSET = new ElfNoteTypeCore(2, "FPREGSET");

    /**
     * Contains copy of prpsinfo struct
     */
    @ElfApi("NT_PRPSINFO")
    public static final ElfNoteTypeCore PRPSINFO = new ElfNoteTypeCore(3, "PRPSINFO");

    /**
     * Contains copy of prxregset struct
     */
    @ElfApi("NT_PRXREG")
    public static final ElfNoteTypeCore PRXREG = new ElfNoteTypeCore(4, "PRXREG");

    /*
     * Contains copy of task structure
     *
     * TODO: Duplicate - to investigate.
     */
    // @ElfApi("NT_TASKSTRUCT")
    // public static final Elf32NoteTypeCore TASKSTRUCT = new Elf32NoteTypeCore(4, "TASKSTRUCT");

    /**
     * String from sysinfo(SI_PLATFORM)
     */
    @ElfApi("NT_PLATFORM")
    public static final ElfNoteTypeCore PLATFORM = new ElfNoteTypeCore(5, "PLATFORM");

    /**
     * Contains copy of auxv array
     */
    @ElfApi("NT_AUXV")
    public static final ElfNoteTypeCore AUXV = new ElfNoteTypeCore(6, "AUXV");

    /**
     * Contains copy of gwindows struct
     */
    @ElfApi("NT_GWINDOWS")
    public static final ElfNoteTypeCore GWINDOWS = new ElfNoteTypeCore(7, "GWINDOWS");

    /**
     * Contains copy of asrset struct
     */
    @ElfApi("NT_ASRS")
    public static final ElfNoteTypeCore ASRS = new ElfNoteTypeCore(8, "ASRS");

    /**
     * Contains copy of pstatus struct
     */
    @ElfApi("NT_PSTATUS")
    public static final ElfNoteTypeCore PSTATUS = new ElfNoteTypeCore(10, "PSTATUS");

    /**
     * Contains copy of psinfo struct
     */
    @ElfApi("NT_PSINFO")
    public static final ElfNoteTypeCore PSINFO = new ElfNoteTypeCore(13, "PSINFO");

    /**
     * Contains copy of prcred struct
     */
    @ElfApi("NT_PRCRED")
    public static final ElfNoteTypeCore PRCRED = new ElfNoteTypeCore(14, "PRCRED");

    /**
     * Contains copy of utsname struct
     */
    @ElfApi("NT_UTSNAME")
    public static final ElfNoteTypeCore UTSNAME = new ElfNoteTypeCore(15, "UTSNAME");

    /**
     * Contains copy of lwpstatus struct
     */
    @ElfApi("NT_LWPSTATUS")
    public static final ElfNoteTypeCore LWPSTATUS = new ElfNoteTypeCore(16, "LWPSTATUS");

    /**
     * Contains copy of lwpinfo struct
     */
    @ElfApi("NT_LWPSINFO")
    public static final ElfNoteTypeCore LWPSINFO = new ElfNoteTypeCore(17, "LWPSINFO");

    /**
     * Contains copy of fprxregset struct
     */
    @ElfApi("NT_PRFPXREG")
    public static final ElfNoteTypeCore PRFPXREG = new ElfNoteTypeCore(20, "PRFPXREG");

    /**
     * Contains copy of siginfo_t, size might increase
     */
    @ElfApi("NT_SIGINFO")
    public static final ElfNoteTypeCore SIGINFO = new ElfNoteTypeCore(0x53494749, "SIGINFO");

    /**
     * Contains information about mapped files
     */
    @ElfApi("NT_FILE")
    public static final ElfNoteTypeCore FILE = new ElfNoteTypeCore(0x46494c45, "FILE");

    /**
     * Contains copy of user_fxsr_struct
     */
    @ElfApi("NT_PRXFPREG")
    public static final ElfNoteTypeCore PRXFPREG = new ElfNoteTypeCore(0x46e62b7f, "PRXFPREG");

    /**
     * PowerPC Altivec/VMX registers
     */
    @ElfApi("NT_PPC_VMX")
    public static final ElfNoteTypeCore PPC_VMX = new ElfNoteTypeCore(0x100, "PPC_VMX");

    /**
     * PowerPC SPE/EVR registers
     */
    @ElfApi("NT_PPC_SPE")
    public static final ElfNoteTypeCore PPC_SPE = new ElfNoteTypeCore(0x101, "PPC_SPE");

    /**
     * PowerPC VSX registers
     */
    @ElfApi("NT_PPC_VSX")
    public static final ElfNoteTypeCore PPC_VSX = new ElfNoteTypeCore(0x102, "PPC_VSX");

    /**
     * Target Address Register
     */
    @ElfApi("NT_PPC_TAR")
    public static final ElfNoteTypeCore PPC_TAR = new ElfNoteTypeCore(0x103, "PPC_TAR");

    /**
     * Program Priority Register
     */
    @ElfApi("NT_PPC_PPR")
    public static final ElfNoteTypeCore PPC_PPR = new ElfNoteTypeCore(0x104, "PPC_PPR");

    /**
     * Data Stream Control Register
     */
    @ElfApi("NT_PPC_DSCR")
    public static final ElfNoteTypeCore PPC_DSCR = new ElfNoteTypeCore(0x105, "PPC_DSCR");

    /**
     * Event Based Branch Registers
     */
    @ElfApi("NT_PPC_EBB")
    public static final ElfNoteTypeCore PPC_EBB = new ElfNoteTypeCore(0x106, "PPC_EBB");

    /**
     * Performance Monitor Registers
     */
    @ElfApi("NT_PPC_PMU")
    public static final ElfNoteTypeCore PPC_PMU = new ElfNoteTypeCore(0x107, "PPC_PMU");

    /**
     * TM checkpointed GPR Registers
     */
    @ElfApi("NT_PPC_TM_CGPR")
    public static final ElfNoteTypeCore PPC_TM_CGPR = new ElfNoteTypeCore(0x108, "PPC_TM_CGPR");

    /**
     * TM checkpointed FPR Registers
     */
    @ElfApi("NT_PPC_TM_CFPR")
    public static final ElfNoteTypeCore PPC_TM_CFPR = new ElfNoteTypeCore(0x109, "PPC_TM_CFPR");

    /**
     * TM checkpointed VMX Registers
     */
    @ElfApi("NT_PPC_TM_CVMX")
    public static final ElfNoteTypeCore PPC_TM_CVMX = new ElfNoteTypeCore(0x10a, "PPC_TM_CVMX");

    /**
     * TM checkpointed VSX Registers
     */
    @ElfApi("NT_PPC_TM_CVSX")
    public static final ElfNoteTypeCore PPC_TM_CVSX = new ElfNoteTypeCore(0x10b, "PPC_TM_CVSX");

    /**
     * TM Special Purpose Registers
     */
    @ElfApi("NT_PPC_TM_SPR")
    public static final ElfNoteTypeCore PPC_TM_SPR = new ElfNoteTypeCore(0x10c, "PPC_TM_SPR");

    /**
     * TM checkpointed Target Address Register
     */
    @ElfApi("NT_PPC_TM_CTAR")
    public static final ElfNoteTypeCore PPC_TM_CTAR = new ElfNoteTypeCore(0x10d, "PPC_TM_CTAR");

    /**
     * TM checkpointed Program Priority Register
     */
    @ElfApi("NT_PPC_TM_CPPR")
    public static final ElfNoteTypeCore PPC_TM_CPPR = new ElfNoteTypeCore(0x10e, "PPC_TM_CPPR");

    /**
     * TM checkpointed Data Stream Control Register
     */
    @ElfApi("NT_PPC_TM_CDSCR")
    public static final ElfNoteTypeCore PPC_TM_CDSCR = new ElfNoteTypeCore(0x10f, "PPC_TM_CDSCR");

    /**
     * Memory Protection Keys registers.
     */
    @ElfApi("NT_PPC_PKEY")
    public static final ElfNoteTypeCore PPC_PKEY = new ElfNoteTypeCore(0x110, "PPC_PKEY");

    /**
     * i386 TLS slots (struct user_desc)
     */
    @ElfApi("NT_386_TLS")
    public static final ElfNoteTypeCore I386_TLS = new ElfNoteTypeCore(0x200, "I386_TLS");

    /**
     * x86 io permission bitmap (1=deny)
     */
    @ElfApi("NT_386_IOPERM")
    public static final ElfNoteTypeCore I386_IOPERM = new
            ElfNoteTypeCore(0x201, "I386_IOPERM");


    /**
     * x86 extended state using xsave
     */
    @ElfApi("NT_X86_XSTATE")
    public static final ElfNoteTypeCore X86_XSTATE = new ElfNoteTypeCore(0x202, "X86_XSTATE");

    /**
     * s390 upper register halves
     */
    @ElfApi("NT_S390_HIGH_GPRS")
    public static final ElfNoteTypeCore S390_HIGH_GPRS = new ElfNoteTypeCore(0x300, "S390_HIGH_GPRS");

    /**
     * s390 timer register
     */
    @ElfApi("NT_S390_TIMER")
    public static final ElfNoteTypeCore S390_TIMER = new ElfNoteTypeCore(0x301, "S390_TIMER");

    /**
     * s390 TOD clock comparator register
     */
    @ElfApi("NT_S390_TODCMP")
    public static final ElfNoteTypeCore S390_TODCMP = new ElfNoteTypeCore(0x302, "S390_TODCMP");

    /**
     * s390 TOD programmable register
     */
    @ElfApi("NT_S390_TODPREG")
    public static final ElfNoteTypeCore S390_TODPREG = new ElfNoteTypeCore(0x303, "S390_TODPREG");

    /**
     * s390 control registers
     */
    @ElfApi("NT_S390_CTRS")
    public static final ElfNoteTypeCore S390_CTRS = new ElfNoteTypeCore(0x304, "S390_CTRS");

    /**
     * s390 prefix register
     */
    @ElfApi("NT_S390_PREFIX")
    public static final ElfNoteTypeCore S390_PREFIX = new ElfNoteTypeCore(0x305, "S390_PREFIX");

    /**
     * s390 breaking event address
     */
    @ElfApi("NT_S390_LAST_BREAK")
    public static final ElfNoteTypeCore S390_LAST_BREAK = new ElfNoteTypeCore(0x306, "S390_LAST_BREAK");

    /**
     * s390 system call restart data
     */
    @ElfApi("NT_S390_SYSTEM_CALL")
    public static final ElfNoteTypeCore S390_SYSTEM_CALL = new ElfNoteTypeCore(0x307, "S390_SYSTEM_CALL");

    /**
     * s390 transaction diagnostic block
     */
    @ElfApi("NT_S390_TDB")
    public static final ElfNoteTypeCore S390_TDB = new ElfNoteTypeCore(0x308, "S390_TDB");

    /**
     * s390 vector registers 0-15 upper half.
     */
    @ElfApi("NT_S390_VXRS_LOW")
    public static final ElfNoteTypeCore S390_VXRS_LOW = new ElfNoteTypeCore(0x309, "S390_VXRS_LOW");

    /**
     * s390 vector registers 16-31.
     */
    @ElfApi("NT_S390_VXRS_HIGH")
    public static final ElfNoteTypeCore S390_VXRS_HIGH = new ElfNoteTypeCore(0x30a, "S390_VXRS_HIGH");

    /**
     * s390 guarded storage registers.
     */
    @ElfApi("NT_S390_GS_CB")
    public static final ElfNoteTypeCore S390_GS_CB = new ElfNoteTypeCore(0x30b, "S390_GS_CB");

    /**
     * s390 guarded storage broadcast control block.
     */
    @ElfApi("NT_S390_GS_BC")
    public static final ElfNoteTypeCore S390_GS_BC = new ElfNoteTypeCore(0x30c, "S390_GS_BC");

    /**
     * s390 runtime instrumentation.
     */
    @ElfApi("NT_S390_RI_CB")
    public static final ElfNoteTypeCore S390_RI_CB = new ElfNoteTypeCore(0x30d, "S390_RI_CB");

    /**
     * ARM VFP/NEON registers
     */
    @ElfApi("NT_ARM_VFP")
    public static final ElfNoteTypeCore ARM_VFP = new ElfNoteTypeCore(0x400, "ARM_VFP");

    /**
     * ARM TLS register
     */
    @ElfApi("NT_ARM_TLS")
    public static final ElfNoteTypeCore ARM_TLS = new ElfNoteTypeCore(0x401, "ARM_TLS");

    /**
     * ARM hardware breakpoint registers
     */
    @ElfApi("NT_ARM_HW_BREAK")
    public static final ElfNoteTypeCore ARM_HW_BREAK = new ElfNoteTypeCore(0x402, "ARM_HW_BREAK");

    /**
     * ARM hardware watchpoint registers
     */
    @ElfApi("NT_ARM_HW_WATCH")
    public static final ElfNoteTypeCore ARM_HW_WATCH = new ElfNoteTypeCore(0x403, "ARM_HW_WATCH");

    /**
     * ARM system call number
     */
    @ElfApi("NT_ARM_SYSTEM_CALL")
    public static final ElfNoteTypeCore ARM_SYSTEM_CALL = new ElfNoteTypeCore(0x404, "ARM_SYSTEM_CALL");

    /**
     * ARM Scalable Vector Extension registers
     */
    @ElfApi("NT_ARM_SVE")
    public static final ElfNoteTypeCore ARM_SVE = new ElfNoteTypeCore(0x405, "ARM_SVE");

    /**
     * Vmcore Device Dump Note.
     */
    @ElfApi("NT_VMCOREDD")
    public static final ElfNoteTypeCore VMCOREDD = new ElfNoteTypeCore(0x700, "VMCOREDD");

    protected ElfNoteTypeCore(int value) {
        super(value);
    }

    protected ElfNoteTypeCore(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfNoteTypeCore fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, ElfNoteTypeCore::new);
    }

    public static ElfNoteTypeCore fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfNoteTypeCore> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

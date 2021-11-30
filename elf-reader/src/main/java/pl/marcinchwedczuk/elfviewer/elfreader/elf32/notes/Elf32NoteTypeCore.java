package pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Applicable to Elf files with type {@link pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfType#COREDUMP}.
 */
public class Elf32NoteTypeCore extends IntPartialEnum<Elf32NoteTypeCore> {
    private static final Map<Integer, Elf32NoteTypeCore> byValue = mkByValueMap();
    private static final Map<String, Elf32NoteTypeCore> byName = mkByNameMap();

    /* Legal values for note segment descriptor types for core files. */

    /**
     * Contains copy of prstatus struct
     */
    @ElfApi("NT_PRSTATUS")
    public static final Elf32NoteTypeCore PRSTATUS = new Elf32NoteTypeCore(1, "PRSTATUS");

    /**
     * Contains copy of fpregset struct.
     */
    @ElfApi("NT_PRFPREG")
    public static final Elf32NoteTypeCore PRFPREG = new Elf32NoteTypeCore(2, "PRFPREG");

    /**
     * Contains copy of fpregset struct
     */
    @ElfApi("NT_FPREGSET")
    public static final Elf32NoteTypeCore FPREGSET = new Elf32NoteTypeCore(2, "FPREGSET");

    /**
     * Contains copy of prpsinfo struct
     */
    @ElfApi("NT_PRPSINFO")
    public static final Elf32NoteTypeCore PRPSINFO = new Elf32NoteTypeCore(3, "PRPSINFO");

    /**
     * Contains copy of prxregset struct
     */
    @ElfApi("NT_PRXREG")
    public static final Elf32NoteTypeCore PRXREG = new Elf32NoteTypeCore(4, "PRXREG");

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
    public static final Elf32NoteTypeCore PLATFORM = new Elf32NoteTypeCore(5, "PLATFORM");

    /**
     * Contains copy of auxv array
     */
    @ElfApi("NT_AUXV")
    public static final Elf32NoteTypeCore AUXV = new Elf32NoteTypeCore(6, "AUXV");

    /**
     * Contains copy of gwindows struct
     */
    @ElfApi("NT_GWINDOWS")
    public static final Elf32NoteTypeCore GWINDOWS = new Elf32NoteTypeCore(7, "GWINDOWS");

    /**
     * Contains copy of asrset struct
     */
    @ElfApi("NT_ASRS")
    public static final Elf32NoteTypeCore ASRS = new Elf32NoteTypeCore(8, "ASRS");

    /**
     * Contains copy of pstatus struct
     */
    @ElfApi("NT_PSTATUS")
    public static final Elf32NoteTypeCore PSTATUS = new Elf32NoteTypeCore(10, "PSTATUS");

    /**
     * Contains copy of psinfo struct
     */
    @ElfApi("NT_PSINFO")
    public static final Elf32NoteTypeCore PSINFO = new Elf32NoteTypeCore(13, "PSINFO");

    /**
     * Contains copy of prcred struct
     */
    @ElfApi("NT_PRCRED")
    public static final Elf32NoteTypeCore PRCRED = new Elf32NoteTypeCore(14, "PRCRED");

    /**
     * Contains copy of utsname struct
     */
    @ElfApi("NT_UTSNAME")
    public static final Elf32NoteTypeCore UTSNAME = new Elf32NoteTypeCore(15, "UTSNAME");

    /**
     * Contains copy of lwpstatus struct
     */
    @ElfApi("NT_LWPSTATUS")
    public static final Elf32NoteTypeCore LWPSTATUS = new Elf32NoteTypeCore(16, "LWPSTATUS");

    /**
     * Contains copy of lwpinfo struct
     */
    @ElfApi("NT_LWPSINFO")
    public static final Elf32NoteTypeCore LWPSINFO = new Elf32NoteTypeCore(17, "LWPSINFO");

    /**
     * Contains copy of fprxregset struct
     */
    @ElfApi("NT_PRFPXREG")
    public static final Elf32NoteTypeCore PRFPXREG = new Elf32NoteTypeCore(20, "PRFPXREG");

    /**
     * Contains copy of siginfo_t, size might increase
     */
    @ElfApi("NT_SIGINFO")
    public static final Elf32NoteTypeCore SIGINFO = new Elf32NoteTypeCore(0x53494749, "SIGINFO");

    /**
     * Contains information about mapped files
     */
    @ElfApi("NT_FILE")
    public static final Elf32NoteTypeCore FILE = new Elf32NoteTypeCore(0x46494c45, "FILE");

    /**
     * Contains copy of user_fxsr_struct
     */
    @ElfApi("NT_PRXFPREG")
    public static final Elf32NoteTypeCore PRXFPREG = new Elf32NoteTypeCore(0x46e62b7f, "PRXFPREG");

    /**
     * PowerPC Altivec/VMX registers
     */
    @ElfApi("NT_PPC_VMX")
    public static final Elf32NoteTypeCore PPC_VMX = new Elf32NoteTypeCore(0x100, "PPC_VMX");

    /**
     * PowerPC SPE/EVR registers
     */
    @ElfApi("NT_PPC_SPE")
    public static final Elf32NoteTypeCore PPC_SPE = new Elf32NoteTypeCore(0x101, "PPC_SPE");

    /**
     * PowerPC VSX registers
     */
    @ElfApi("NT_PPC_VSX")
    public static final Elf32NoteTypeCore PPC_VSX = new Elf32NoteTypeCore(0x102, "PPC_VSX");

    /**
     * Target Address Register
     */
    @ElfApi("NT_PPC_TAR")
    public static final Elf32NoteTypeCore PPC_TAR = new Elf32NoteTypeCore(0x103, "PPC_TAR");

    /**
     * Program Priority Register
     */
    @ElfApi("NT_PPC_PPR")
    public static final Elf32NoteTypeCore PPC_PPR = new Elf32NoteTypeCore(0x104, "PPC_PPR");

    /**
     * Data Stream Control Register
     */
    @ElfApi("NT_PPC_DSCR")
    public static final Elf32NoteTypeCore PPC_DSCR = new Elf32NoteTypeCore(0x105, "PPC_DSCR");

    /**
     * Event Based Branch Registers
     */
    @ElfApi("NT_PPC_EBB")
    public static final Elf32NoteTypeCore PPC_EBB = new Elf32NoteTypeCore(0x106, "PPC_EBB");

    /**
     * Performance Monitor Registers
     */
    @ElfApi("NT_PPC_PMU")
    public static final Elf32NoteTypeCore PPC_PMU = new Elf32NoteTypeCore(0x107, "PPC_PMU");

    /**
     * TM checkpointed GPR Registers
     */
    @ElfApi("NT_PPC_TM_CGPR")
    public static final Elf32NoteTypeCore PPC_TM_CGPR = new Elf32NoteTypeCore(0x108, "PPC_TM_CGPR");

    /**
     * TM checkpointed FPR Registers
     */
    @ElfApi("NT_PPC_TM_CFPR")
    public static final Elf32NoteTypeCore PPC_TM_CFPR = new Elf32NoteTypeCore(0x109, "PPC_TM_CFPR");

    /**
     * TM checkpointed VMX Registers
     */
    @ElfApi("NT_PPC_TM_CVMX")
    public static final Elf32NoteTypeCore PPC_TM_CVMX = new Elf32NoteTypeCore(0x10a, "PPC_TM_CVMX");

    /**
     * TM checkpointed VSX Registers
     */
    @ElfApi("NT_PPC_TM_CVSX")
    public static final Elf32NoteTypeCore PPC_TM_CVSX = new Elf32NoteTypeCore(0x10b, "PPC_TM_CVSX");

    /**
     * TM Special Purpose Registers
     */
    @ElfApi("NT_PPC_TM_SPR")
    public static final Elf32NoteTypeCore PPC_TM_SPR = new Elf32NoteTypeCore(0x10c, "PPC_TM_SPR");

    /**
     * TM checkpointed Target Address Register
     */
    @ElfApi("NT_PPC_TM_CTAR")
    public static final Elf32NoteTypeCore PPC_TM_CTAR = new Elf32NoteTypeCore(0x10d, "PPC_TM_CTAR");

    /**
     * TM checkpointed Program Priority Register
     */
    @ElfApi("NT_PPC_TM_CPPR")
    public static final Elf32NoteTypeCore PPC_TM_CPPR = new Elf32NoteTypeCore(0x10e, "PPC_TM_CPPR");

    /**
     * TM checkpointed Data Stream Control Register
     */
    @ElfApi("NT_PPC_TM_CDSCR")
    public static final Elf32NoteTypeCore PPC_TM_CDSCR = new Elf32NoteTypeCore(0x10f, "PPC_TM_CDSCR");

    /**
     * Memory Protection Keys registers.
     */
    @ElfApi("NT_PPC_PKEY")
    public static final Elf32NoteTypeCore PPC_PKEY = new Elf32NoteTypeCore(0x110, "PPC_PKEY");

    /**
     * i386 TLS slots (struct user_desc)
     */
    @ElfApi("NT_386_TLS")
    public static final Elf32NoteTypeCore I386_TLS = new Elf32NoteTypeCore(0x200, "I386_TLS");

    /**
     * x86 io permission bitmap (1=deny)
     */
    @ElfApi("NT_386_IOPERM")
    public static final Elf32NoteTypeCore I386_IOPERM = new
            Elf32NoteTypeCore(0x201, "I386_IOPERM");


    /**
     * x86 extended state using xsave
     */
    @ElfApi("NT_X86_XSTATE")
    public static final Elf32NoteTypeCore X86_XSTATE = new Elf32NoteTypeCore(0x202, "X86_XSTATE");

    /**
     * s390 upper register halves
     */
    @ElfApi("NT_S390_HIGH_GPRS")
    public static final Elf32NoteTypeCore S390_HIGH_GPRS = new Elf32NoteTypeCore(0x300, "S390_HIGH_GPRS");

    /**
     * s390 timer register
     */
    @ElfApi("NT_S390_TIMER")
    public static final Elf32NoteTypeCore S390_TIMER = new Elf32NoteTypeCore(0x301, "S390_TIMER");

    /**
     * s390 TOD clock comparator register
     */
    @ElfApi("NT_S390_TODCMP")
    public static final Elf32NoteTypeCore S390_TODCMP = new Elf32NoteTypeCore(0x302, "S390_TODCMP");

    /**
     * s390 TOD programmable register
     */
    @ElfApi("NT_S390_TODPREG")
    public static final Elf32NoteTypeCore S390_TODPREG = new Elf32NoteTypeCore(0x303, "S390_TODPREG");

    /**
     * s390 control registers
     */
    @ElfApi("NT_S390_CTRS")
    public static final Elf32NoteTypeCore S390_CTRS = new Elf32NoteTypeCore(0x304, "S390_CTRS");

    /**
     * s390 prefix register
     */
    @ElfApi("NT_S390_PREFIX")
    public static final Elf32NoteTypeCore S390_PREFIX = new Elf32NoteTypeCore(0x305, "S390_PREFIX");

    /**
     * s390 breaking event address
     */
    @ElfApi("NT_S390_LAST_BREAK")
    public static final Elf32NoteTypeCore S390_LAST_BREAK = new Elf32NoteTypeCore(0x306, "S390_LAST_BREAK");

    /**
     * s390 system call restart data
     */
    @ElfApi("NT_S390_SYSTEM_CALL")
    public static final Elf32NoteTypeCore S390_SYSTEM_CALL = new Elf32NoteTypeCore(0x307, "S390_SYSTEM_CALL");

    /**
     * s390 transaction diagnostic block
     */
    @ElfApi("NT_S390_TDB")
    public static final Elf32NoteTypeCore S390_TDB = new Elf32NoteTypeCore(0x308, "S390_TDB");

    /**
     * s390 vector registers 0-15 upper half.
     */
    @ElfApi("NT_S390_VXRS_LOW")
    public static final Elf32NoteTypeCore S390_VXRS_LOW = new Elf32NoteTypeCore(0x309, "S390_VXRS_LOW");

    /**
     * s390 vector registers 16-31.
     */
    @ElfApi("NT_S390_VXRS_HIGH")
    public static final Elf32NoteTypeCore S390_VXRS_HIGH = new Elf32NoteTypeCore(0x30a, "S390_VXRS_HIGH");

    /**
     * s390 guarded storage registers.
     */
    @ElfApi("NT_S390_GS_CB")
    public static final Elf32NoteTypeCore S390_GS_CB = new Elf32NoteTypeCore(0x30b, "S390_GS_CB");

    /**
     * s390 guarded storage broadcast control block.
     */
    @ElfApi("NT_S390_GS_BC")
    public static final Elf32NoteTypeCore S390_GS_BC = new Elf32NoteTypeCore(0x30c, "S390_GS_BC");

    /**
     * s390 runtime instrumentation.
     */
    @ElfApi("NT_S390_RI_CB")
    public static final Elf32NoteTypeCore S390_RI_CB = new Elf32NoteTypeCore(0x30d, "S390_RI_CB");

    /**
     * ARM VFP/NEON registers
     */
    @ElfApi("NT_ARM_VFP")
    public static final Elf32NoteTypeCore ARM_VFP = new Elf32NoteTypeCore(0x400, "ARM_VFP");

    /**
     * ARM TLS register
     */
    @ElfApi("NT_ARM_TLS")
    public static final Elf32NoteTypeCore ARM_TLS = new Elf32NoteTypeCore(0x401, "ARM_TLS");

    /**
     * ARM hardware breakpoint registers
     */
    @ElfApi("NT_ARM_HW_BREAK")
    public static final Elf32NoteTypeCore ARM_HW_BREAK = new Elf32NoteTypeCore(0x402, "ARM_HW_BREAK");

    /**
     * ARM hardware watchpoint registers
     */
    @ElfApi("NT_ARM_HW_WATCH")
    public static final Elf32NoteTypeCore ARM_HW_WATCH = new Elf32NoteTypeCore(0x403, "ARM_HW_WATCH");

    /**
     * ARM system call number
     */
    @ElfApi("NT_ARM_SYSTEM_CALL")
    public static final Elf32NoteTypeCore ARM_SYSTEM_CALL = new Elf32NoteTypeCore(0x404, "ARM_SYSTEM_CALL");

    /**
     * ARM Scalable Vector Extension registers
     */
    @ElfApi("NT_ARM_SVE")
    public static final Elf32NoteTypeCore ARM_SVE = new Elf32NoteTypeCore(0x405, "ARM_SVE");

    /**
     * Vmcore Device Dump Note.
     */
    @ElfApi("NT_VMCOREDD")
    public static final Elf32NoteTypeCore VMCOREDD = new Elf32NoteTypeCore(0x700, "VMCOREDD");

    protected Elf32NoteTypeCore(int value) {
        super(value);
    }

    protected Elf32NoteTypeCore(int value, String name) {
        super(value, name, byValue, byName);
    }

    public static Elf32NoteTypeCore fromValue(int value) {
        return IntPartialEnum.fromValueOrCreate(value, byValue, Elf32NoteTypeCore::new);
    }

    public static Elf32NoteTypeCore fromName(String name) {
        return IntPartialEnum.fromName(name, byName);
    }

    public static Collection<Elf32NoteTypeCore> knownValues() {
        return IntPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2apiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

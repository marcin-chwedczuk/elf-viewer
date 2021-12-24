package pl.marcinchwedczuk.elfviewer.elfreader.elf;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ShortPartialEnum;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ElfMachine extends ShortPartialEnum<ElfMachine> {
    private static final Map<Short, ElfMachine> byValue = mkByValueMap();
    private static final Map<String, ElfMachine> byName = mkByNameMap();

    /**
     * An unknown machine.
     */
    @ElfApi("ET_NONE")
    public static final ElfMachine NONE = new ElfMachine(s(0), "NONE");

    /**
     * AT&T WE 32100.
     */
    @ElfApi("EM_M32")
    public static final ElfMachine M32 = new ElfMachine(s(1), "M32");

    /**
     * Sun Microsystems SPARC.
     */
    @ElfApi("EM_SPARC")
    public static final ElfMachine SPARC = new ElfMachine(s(2), "SPARC");

    /**
     * Intel 80386.
     */
    @ElfApi("EM_386")
    public static final ElfMachine INTEL_386 = new ElfMachine(s(3), "INTEL_386");

    /**
     * Motorola 68000.
     */
    @ElfApi("EM_68K")
    public static final ElfMachine MOTOROLA_68K = new ElfMachine(s(4), "MOTOROLA_68K");

    /**
     * Motorola 88000.
     */
    @ElfApi("EM_88K")
    public static final ElfMachine MOTOROLA_88K = new ElfMachine(s(5), "MOTOROLA_88K");

    /**
     * Intel MCU.
     */
    @ElfApi("EM_IAMCU")
    public static final ElfMachine INTEL_MCU = new ElfMachine(s(6), "INTEL_MCU");

    /**
     * Intel 80860.
     */
    @ElfApi("EM_860")
    public static final ElfMachine INTEL_860 = new ElfMachine(s(7), "INTEL_860");

    /**
     * MIPS RS3000 (big-endian only).
     */
    @ElfApi("EM_MIPS")
    public static final ElfMachine MIPS = new ElfMachine(s(8), "MIPS");

    /**
     * IBM System/370.
     */
    @ElfApi("EM_S370")
    public static final ElfMachine IBM_SYSTEM_370 = new ElfMachine(s(9), "IBM_SYSTEM_370");

    /**
     * MIPS R3000 little-endian
     */
    @ElfApi("EM_MIPS_RS3_LE")
    public static final ElfMachine MIPS_R3000 = new ElfMachine(s(10), "MIPS_R3000");

    /* reserved 11-14 */

    /**
     * HPPA
     */
    @ElfApi("EM_PARISC")
    public static final ElfMachine PARISC = new ElfMachine(s(15), "PARISC");

    /* reserved 16 */

    /**
     * Fujitsu VPP500
     */
    @ElfApi("EM_VPP500")
    public static final ElfMachine VPP500 = new ElfMachine(s(17), "VPP500");

    /**
     * Sun's "v8plus"
     */
    @ElfApi("EM_SPARC32PLUS")
    public static final ElfMachine SPARC32PLUS = new ElfMachine(s(18), "SPARC32PLUS");

    /**
     * Intel 80960
     */
    @ElfApi("EM_960")
    public static final ElfMachine INTEL_960 = new ElfMachine(s(19), "INTEL_960");

    /**
     * PowerPC
     */
    @ElfApi("EM_PPC")
    public static final ElfMachine PPC = new ElfMachine(s(20), "PPC");

    /**
     * PowerPC 64-bit
     */
    @ElfApi("EM_PPC64")
    public static final ElfMachine PPC64 = new ElfMachine(s(21), "PPC64");

    /**
     * IBM S390
     */
    @ElfApi("EM_S390")
    public static final ElfMachine S390 = new ElfMachine(s(22), "S390");

    /**
     * IBM SPU/SPC
     */
    @ElfApi("EM_SPU")
    public static final ElfMachine SPU = new ElfMachine(s(23), "SPU");

    /* reserved 24-35 */

    /**
     * NEC V800 series
     */
    @ElfApi("EM_V800")
    public static final ElfMachine V800 = new ElfMachine(s(36), "V800");

    /**
     * Fujitsu FR20
     */
    @ElfApi("EM_FR20")
    public static final ElfMachine FR20 = new ElfMachine(s(37), "FR20");

    /**
     * TRW RH-32
     */
    @ElfApi("EM_RH32")
    public static final ElfMachine RH32 = new ElfMachine(s(38), "RH32");

    /**
     * Motorola RCE
     */
    @ElfApi("EM_RCE")
    public static final ElfMachine RCE = new ElfMachine(s(39), "RCE");

    /**
     * ARM
     */
    @ElfApi("EM_ARM")
    public static final ElfMachine ARM = new ElfMachine(s(40), "ARM");

    /**
     * Digital Alpha
     */
    @ElfApi("EM_FAKE_ALPHA")
    public static final ElfMachine FAKE_ALPHA = new ElfMachine(s(41), "FAKE_ALPHA");

    /**
     * Hitachi SH
     */
    @ElfApi("EM_SH")
    public static final ElfMachine SH = new ElfMachine(s(42), "SH");

    /**
     * SPARC v9 64-bit
     */
    @ElfApi("EM_SPARCV9")
    public static final ElfMachine SPARCV9 = new ElfMachine(s(43), "SPARCV9");

    /**
     * Siemens Tricore
     */
    @ElfApi("EM_TRICORE")
    public static final ElfMachine TRICORE = new ElfMachine(s(44), "TRICORE");

    /**
     * Argonaut RISC Core
     */
    @ElfApi("EM_ARC")
    public static final ElfMachine ARC = new ElfMachine(s(45), "ARC");

    /**
     * Hitachi H8/300
     */
    @ElfApi("EM_H8_300")
    public static final ElfMachine H8_300 = new ElfMachine(s(46), "H8_300");

    /**
     * Hitachi H8/300H
     */
    @ElfApi("EM_H8_300H")
    public static final ElfMachine H8_300H = new ElfMachine(s(47), "H8_300H");

    /**
     * Hitachi H8S
     */
    @ElfApi("EM_H8S")
    public static final ElfMachine H8S = new ElfMachine(s(48), "H8S");

    /**
     * Hitachi H8/500
     */
    @ElfApi("EM_H8_500")
    public static final ElfMachine H8_500 = new ElfMachine(s(49), "H8_500");

    /**
     * Intel Itanium
     */
    @ElfApi("EM_IA_64")
    public static final ElfMachine IA_64 = new ElfMachine(s(50), "IA_64");

    /**
     * Stanford MIPS-X
     */
    @ElfApi("EM_MIPS_X")
    public static final ElfMachine MIPS_X = new ElfMachine(s(51), "MIPS_X");

    /**
     * Motorola Coldfire
     */
    @ElfApi("EM_COLDFIRE")
    public static final ElfMachine COLDFIRE = new ElfMachine(s(52), "COLDFIRE");

    /**
     * Motorola M68HC12
     */
    @ElfApi("EM_68HC12")
    public static final ElfMachine MOTOROLA_68HC12 = new ElfMachine(s(53), "MOTOROLA_68HC12");

    /**
     * Fujitsu MMA Multimedia Accelerator
     */
    @ElfApi("EM_MMA")
    public static final ElfMachine MMA = new ElfMachine(s(54), "MMA");

    /**
     * Siemens PCP
     */
    @ElfApi("EM_PCP")
    public static final ElfMachine PCP = new ElfMachine(s(55), "PCP");

    /**
     * Sony nCPU embeeded RISC
     */
    @ElfApi("EM_NCPU")
    public static final ElfMachine NCPU = new ElfMachine(s(56), "NCPU");

    /**
     * Denso NDR1 microprocessor
     */
    @ElfApi("EM_NDR1")
    public static final ElfMachine NDR1 = new ElfMachine(s(57), "NDR1");

    /**
     * Motorola Start
     */
    @ElfApi("EM_STARCORE")
    public static final ElfMachine STARCORE = new ElfMachine(s(58), "STARCORE");

    /**
     * Toyota ME16 processor
     */
    @ElfApi("EM_ME16")
    public static final ElfMachine ME16 = new ElfMachine(s(59), "ME16");

    /**
     * STMicroelectronic ST100 processor
     */
    @ElfApi("EM_ST100")
    public static final ElfMachine ST100 = new ElfMachine(s(60), "ST100");

    /**
     * Advanced Logic Corp. Tinyj emb.fam
     */
    @ElfApi("EM_TINYJ")
    public static final ElfMachine TINYJ = new ElfMachine(s(61), "TINYJ");

    /**
     * AMD x86-64 architecture
     */
    @ElfApi("EM_X86_64")
    public static final ElfMachine X86_64 = new ElfMachine(s(62), "X86_64");

    /**
     * Sony DSP Processor
     */
    @ElfApi("EM_PDSP")
    public static final ElfMachine PDSP = new ElfMachine(s(63), "PDSP");

    /**
     * Digital PDP-10
     */
    @ElfApi("EM_PDP10")
    public static final ElfMachine PDP10 = new ElfMachine(s(64), "PDP10");

    /**
     * Digital PDP-11
     */
    @ElfApi("EM_PDP11")
    public static final ElfMachine PDP11 = new ElfMachine(s(65), "PDP11");

    /**
     * Siemens FX66 microcontroller
     */
    @ElfApi("EM_FX66")
    public static final ElfMachine FX66 = new ElfMachine(s(66), "FX66");

    /**
     * STMicroelectronics ST9+ 8/16 mc
     */
    @ElfApi("EM_ST9PLUS")
    public static final ElfMachine ST9PLUS = new ElfMachine(s(67), "ST9PLUS");

    /**
     * STmicroelectronics ST7 8 bit mc
     */
    @ElfApi("EM_ST7")
    public static final ElfMachine ST7 = new ElfMachine(s(68), "ST7");

    /**
     * Motorola MC68HC16 microcontroller
     */
    @ElfApi("EM_68HC16")
    public static final ElfMachine MOTOROLA_68HC16 = new ElfMachine(s(69), "MOTOROLA_68HC16");

    /**
     * Motorola MC68HC11 microcontroller
     */
    @ElfApi("EM_68HC11")
    public static final ElfMachine MOTOROLA_68HC11 = new ElfMachine(s(70), "MOTOROLA_68HC11");

    /**
     * Motorola MC68HC08 microcontroller
     */
    @ElfApi("EM_68HC08")
    public static final ElfMachine MOTOROLA_68HC08 = new ElfMachine(s(71), "MOTOROLA_68HC08");

    /**
     * Motorola MC68HC05 microcontroller
     */
    @ElfApi("EM_68HC05")
    public static final ElfMachine MOTOROLA_68HC05 = new ElfMachine(s(72), "MOTOROLA_68HC05");

    /**
     * Silicon Graphics SVx
     */
    @ElfApi("EM_SVX")
    public static final ElfMachine SVX = new ElfMachine(s(73), "SVX");

    /**
     * STMicroelectronics ST19 8 bit mc
     */
    @ElfApi("EM_ST19")
    public static final ElfMachine ST19 = new ElfMachine(s(74), "ST19");

    /**
     * Digital VAX
     */
    @ElfApi("EM_VAX")
    public static final ElfMachine VAX = new ElfMachine(s(75), "VAX");

    /**
     * Axis Communications 32-bit emb.proc
     */
    @ElfApi("EM_CRIS")
    public static final ElfMachine CRIS = new ElfMachine(s(76), "CRIS");

    /**
     * Infineon Technologies 32-bit emb.proc
     */
    @ElfApi("EM_JAVELIN")
    public static final ElfMachine JAVELIN = new ElfMachine(s(77), "JAVELIN");

    /**
     * Element 14 64-bit DSP Processor
     */
    @ElfApi("EM_FIREPATH")
    public static final ElfMachine FIREPATH = new ElfMachine(s(78), "FIREPATH");

    /**
     * LSI Logic 16-bit DSP Processor
     */
    @ElfApi("EM_ZSP")
    public static final ElfMachine ZSP = new ElfMachine(s(79), "ZSP");

    /**
     * Donald Knuth's educational 64-bit proc
     */
    @ElfApi("EM_MMIX")
    public static final ElfMachine MMIX = new ElfMachine(s(80), "MMIX");

    /**
     * Harvard University machine-independent object files
     */
    @ElfApi("EM_HUANY")
    public static final ElfMachine HUANY = new ElfMachine(s(81), "HUANY");

    /**
     * SiTera Prism
     */
    @ElfApi("EM_PRISM")
    public static final ElfMachine PRISM = new ElfMachine(s(82), "PRISM");

    /**
     * Atmel AVR 8-bit microcontroller
     */
    @ElfApi("EM_AVR")
    public static final ElfMachine AVR = new ElfMachine(s(83), "AVR");

    /**
     * Fujitsu FR30
     */
    @ElfApi("EM_FR30")
    public static final ElfMachine FR30 = new ElfMachine(s(84), "FR30");

    /**
     * Mitsubishi D10V
     */
    @ElfApi("EM_D10V")
    public static final ElfMachine D10V = new ElfMachine(s(85), "D10V");

    /**
     * Mitsubishi D30V
     */
    @ElfApi("EM_D30V")
    public static final ElfMachine D30V = new ElfMachine(s(86), "D30V");

    /**
     * NEC v850
     */
    @ElfApi("EM_V850")
    public static final ElfMachine V850 = new ElfMachine(s(87), "V850");

    /**
     * Mitsubishi M32R
     */
    @ElfApi("EM_M32R")
    public static final ElfMachine M32R = new ElfMachine(s(88), "M32R");

    /**
     * Matsushita MN10300
     */
    @ElfApi("EM_MN10300")
    public static final ElfMachine MN10300 = new ElfMachine(s(89), "MN10300");

    /**
     * Matsushita MN10200
     */
    @ElfApi("EM_MN10200")
    public static final ElfMachine MN10200 = new ElfMachine(s(90), "MN10200");

    /**
     * picoJava
     */
    @ElfApi("EM_PJ")
    public static final ElfMachine PJ = new ElfMachine(s(91), "PJ");

    /**
     * OpenRISC 32-bit embedded processor
     */
    @ElfApi("EM_OPENRISC")
    public static final ElfMachine OPENRISC = new ElfMachine(s(92), "OPENRISC");

    /**
     * ARC International ARCompact
     */
    @ElfApi("EM_ARC_COMPACT")
    public static final ElfMachine ARC_COMPACT = new ElfMachine(s(93), "ARC_COMPACT");

    /**
     * Tensilica Xtensa Architecture
     */
    @ElfApi("EM_XTENSA")
    public static final ElfMachine XTENSA = new ElfMachine(s(94), "XTENSA");

    /**
     * Alphamosaic VideoCore
     */
    @ElfApi("EM_VIDEOCORE")
    public static final ElfMachine VIDEOCORE = new ElfMachine(s(95), "VIDEOCORE");

    /**
     * Thompson Multimedia General Purpose Proc
     */
    @ElfApi("EM_TMM_GPP")
    public static final ElfMachine TMM_GPP = new ElfMachine(s(96), "TMM_GPP");

    /**
     * National Semi. 32000
     */
    @ElfApi("EM_NS32K")
    public static final ElfMachine NS32K = new ElfMachine(s(97), "NS32K");

    /**
     * Tenor Network TPC
     */
    @ElfApi("EM_TPC")
    public static final ElfMachine TPC = new ElfMachine(s(98), "TPC");

    /**
     * Trebia SNP 1000
     */
    @ElfApi("EM_SNP1K")
    public static final ElfMachine SNP1K = new ElfMachine(s(99), "SNP1K");

    /**
     * STMicroelectronics ST200
     */
    @ElfApi("EM_ST200")
    public static final ElfMachine ST200 = new ElfMachine(s(100), "ST200");

    /**
     * Ubicom IP2xxx
     */
    @ElfApi("EM_IP2K")
    public static final ElfMachine IP2K = new ElfMachine(s(101), "IP2K");

    /**
     * MAX processor
     */
    @ElfApi("EM_MAX")
    public static final ElfMachine MAX = new ElfMachine(s(102), "MAX");

    /**
     * National Semi. CompactRISC
     */
    @ElfApi("EM_CR")
    public static final ElfMachine CR = new ElfMachine(s(103), "CR");

    /**
     * Fujitsu F2MC16
     */
    @ElfApi("EM_F2MC16")
    public static final ElfMachine F2MC16 = new ElfMachine(s(104), "F2MC16");

    /**
     * Texas Instruments msp430
     */
    @ElfApi("EM_MSP430")
    public static final ElfMachine MSP430 = new ElfMachine(s(105), "MSP430");

    /**
     * Analog Devices Blackfin DSP
     */
    @ElfApi("EM_BLACKFIN")
    public static final ElfMachine BLACKFIN = new ElfMachine(s(106), "BLACKFIN");

    /**
     * Seiko Epson S1C33 family
     */
    @ElfApi("EM_SE_C33")
    public static final ElfMachine SE_C33 = new ElfMachine(s(107), "SE_C33");

    /**
     * Sharp embedded microprocessor
     */
    @ElfApi("EM_SEP")
    public static final ElfMachine SEP = new ElfMachine(s(108), "SEP");

    /**
     * Arca RISC
     */
    @ElfApi("EM_ARCA")
    public static final ElfMachine ARCA = new ElfMachine(s(109), "ARCA");

    /**
     * PKU-Unity & MPRC Peking Uni. mc series
     */
    @ElfApi("EM_UNICORE")
    public static final ElfMachine UNICORE = new ElfMachine(s(110), "UNICORE");

    /**
     * eXcess configurable cpu
     */
    @ElfApi("EM_EXCESS")
    public static final ElfMachine EXCESS = new ElfMachine(s(111), "EXCESS");

    /**
     * Icera Semi. Deep Execution Processor
     */
    @ElfApi("EM_DXP")
    public static final ElfMachine DXP = new ElfMachine(s(112), "DXP");

    /**
     * Altera Nios II
     */
    @ElfApi("EM_ALTERA_NIOS2")
    public static final ElfMachine ALTERA_NIOS2 = new ElfMachine(s(113), "ALTERA_NIOS2");

    /**
     * National Semi. CompactRISC CRX
     */
    @ElfApi("EM_CRX")
    public static final ElfMachine CRX = new ElfMachine(s(114), "CRX");

    /**
     * Motorola XGATE
     */
    @ElfApi("EM_XGATE")
    public static final ElfMachine XGATE = new ElfMachine(s(115), "XGATE");

    /**
     * Infineon C16x/XC16x
     */
    @ElfApi("EM_C166")
    public static final ElfMachine C166 = new ElfMachine(s(116), "C166");

    /**
     * Renesas M16C
     */
    @ElfApi("EM_M16C")
    public static final ElfMachine M16C = new ElfMachine(s(117), "M16C");

    /**
     * Microchip Technology dsPIC30F
     */
    @ElfApi("EM_DSPIC30F")
    public static final ElfMachine DSPIC30F = new ElfMachine(s(118), "DSPIC30F");

    /**
     * Freescale Communication Engine RISC
     */
    @ElfApi("EM_CE")
    public static final ElfMachine CE = new ElfMachine(s(119), "CE");

    /**
     * Renesas M32C
     */
    @ElfApi("EM_M32C")
    public static final ElfMachine M32C = new ElfMachine(s(120), "M32C");

    /* reserved 121-130 */

    /**
     * Altium TSK3000
     */
    @ElfApi("EM_TSK3000")
    public static final ElfMachine TSK3000 = new ElfMachine(s(131), "TSK3000");

    /**
     * Freescale RS08
     */
    @ElfApi("EM_RS08")
    public static final ElfMachine RS08 = new ElfMachine(s(132), "RS08");

    /**
     * Analog Devices SHARC family
     */
    @ElfApi("EM_SHARC")
    public static final ElfMachine SHARC = new ElfMachine(s(133), "SHARC");

    /**
     * Cyan Technology eCOG2
     */
    @ElfApi("EM_ECOG2")
    public static final ElfMachine ECOG2 = new ElfMachine(s(134), "ECOG2");

    /**
     * Sunplus S+core7 RISC
     */
    @ElfApi("EM_SCORE7")
    public static final ElfMachine SCORE7 = new ElfMachine(s(135), "SCORE7");

    /**
     * New Japan Radio (NJR) 24-bit DSP
     */
    @ElfApi("EM_DSP24")
    public static final ElfMachine DSP24 = new ElfMachine(s(136), "DSP24");

    /**
     * Broadcom VideoCore III
     */
    @ElfApi("EM_VIDEOCORE3")
    public static final ElfMachine VIDEOCORE3 = new ElfMachine(s(137), "VIDEOCORE3");

    /**
     * RISC for Lattice FPGA
     */
    @ElfApi("EM_LATTICEMICO32")
    public static final ElfMachine LATTICEMICO32 = new ElfMachine(s(138), "LATTICEMICO32");

    /**
     * Seiko Epson C17
     */
    @ElfApi("EM_SE_C17")
    public static final ElfMachine SE_C17 = new ElfMachine(s(139), "SE_C17");

    /**
     * Texas Instruments TMS320C6000 DSP
     */
    @ElfApi("EM_TI_C6000")
    public static final ElfMachine TI_C6000 = new ElfMachine(s(140), "TI_C6000");

    /**
     * Texas Instruments TMS320C2000 DSP
     */
    @ElfApi("EM_TI_C2000")
    public static final ElfMachine TI_C2000 = new ElfMachine(s(141), "TI_C2000");

    /**
     * Texas Instruments TMS320C55x DSP
     */
    @ElfApi("EM_TI_C5500")
    public static final ElfMachine TI_C5500 = new ElfMachine(s(142), "TI_C5500");

    /**
     * Texas Instruments App. Specific RISC
     */
    @ElfApi("EM_TI_ARP32")
    public static final ElfMachine TI_ARP32 = new ElfMachine(s(143), "TI_ARP32");

    /**
     * Texas Instruments Prog. Realtime Unit
     */
    @ElfApi("EM_TI_PRU")
    public static final ElfMachine TI_PRU = new ElfMachine(s(144), "TI_PRU");

    /* reserved 145-159 */

    /**
     * STMicroelectronics 64bit VLIW DSP
     */
    @ElfApi("EM_MMDSP_PLUS")
    public static final ElfMachine MMDSP_PLUS = new ElfMachine(s(160), "MMDSP_PLUS");

    /**
     * Cypress M8C
     */
    @ElfApi("EM_CYPRESS_M8C")
    public static final ElfMachine CYPRESS_M8C = new ElfMachine(s(161), "CYPRESS_M8C");

    /**
     * Renesas R32C
     */
    @ElfApi("EM_R32C")
    public static final ElfMachine R32C = new ElfMachine(s(162), "R32C");

    /**
     * NXP Semi. TriMedia
     */
    @ElfApi("EM_TRIMEDIA")
    public static final ElfMachine TRIMEDIA = new ElfMachine(s(163), "TRIMEDIA");

    /**
     * QUALCOMM DSP6
     */
    @ElfApi("EM_QDSP6")
    public static final ElfMachine QDSP6 = new ElfMachine(s(164), "QDSP6");

    /**
     * Intel 8051 and variants
     */
    @ElfApi("EM_8051")
    public static final ElfMachine INTEL_8051 = new ElfMachine(s(165), "INTEL_8051");

    /**
     * STMicroelectronics STxP7x
     */
    @ElfApi("EM_STXP7X")
    public static final ElfMachine STXP7X = new ElfMachine(s(166), "STXP7X");

    /**
     * Andes Tech. compact code emb. RISC
     */
    @ElfApi("EM_NDS32")
    public static final ElfMachine NDS32 = new ElfMachine(s(167), "NDS32");

    /**
     * Cyan Technology eCOG1X
     */
    @ElfApi("EM_ECOG1X")
    public static final ElfMachine ECOG1X = new ElfMachine(s(168), "ECOG1X");

    /**
     * Dallas Semi. MAXQ30 mc
     */
    @ElfApi("EM_MAXQ30")
    public static final ElfMachine MAXQ30 = new ElfMachine(s(169), "MAXQ30");

    /**
     * New Japan Radio (NJR) 16-bit DSP
     */
    @ElfApi("EM_XIMO16")
    public static final ElfMachine XIMO16 = new ElfMachine(s(170), "XIMO16");

    /**
     * M2000 Reconfigurable RISC
     */
    @ElfApi("EM_MANIK")
    public static final ElfMachine MANIK = new ElfMachine(s(171), "MANIK");

    /**
     * Cray NV2 vector architecture
     */
    @ElfApi("EM_CRAYNV2")
    public static final ElfMachine CRAYNV2 = new ElfMachine(s(172), "CRAYNV2");

    /**
     * Renesas RX
     */
    @ElfApi("EM_RX")
    public static final ElfMachine RX = new ElfMachine(s(173), "RX");

    /**
     * Imagination Tech. META
     */
    @ElfApi("EM_METAG")
    public static final ElfMachine METAG = new ElfMachine(s(174), "METAG");

    /**
     * MCST Elbrus
     */
    @ElfApi("EM_MCST_ELBRUS")
    public static final ElfMachine MCST_ELBRUS = new ElfMachine(s(175), "MCST_ELBRUS");

    /**
     * Cyan Technology eCOG16
     */
    @ElfApi("EM_ECOG16")
    public static final ElfMachine ECOG16 = new ElfMachine(s(176), "ECOG16");

    /**
     * National Semi. CompactRISC CR16
     */
    @ElfApi("EM_CR16")
    public static final ElfMachine CR16 = new ElfMachine(s(177), "CR16");

    /**
     * Freescale Extended Time Processing Unit
     */
    @ElfApi("EM_ETPU")
    public static final ElfMachine ETPU = new ElfMachine(s(178), "ETPU");

    /**
     * Infineon Tech. SLE9X
     */
    @ElfApi("EM_SLE9X")
    public static final ElfMachine SLE9X = new ElfMachine(s(179), "SLE9X");

    /**
     * Intel L10M
     */
    @ElfApi("EM_L10M")
    public static final ElfMachine L10M = new ElfMachine(s(180), "L10M");

    /**
     * Intel K10M
     */
    @ElfApi("EM_K10M")
    public static final ElfMachine K10M = new ElfMachine(s(181), "K10M");

    /* reserved 182 */

    /**
     * ARM AARCH64
     */
    @ElfApi("EM_AARCH64")
    public static final ElfMachine AARCH64 = new ElfMachine(s(183), "AARCH64");

    /* reserved 184 */

    /**
     * Amtel 32-bit microprocessor
     */
    @ElfApi("EM_AVR32")
    public static final ElfMachine AVR32 = new ElfMachine(s(185), "AVR32");

    /**
     * STMicroelectronics STM8
     */
    @ElfApi("EM_STM8")
    public static final ElfMachine STM8 = new ElfMachine(s(186), "STM8");

    /**
     * Tileta TILE64
     */
    @ElfApi("EM_TILE64")
    public static final ElfMachine TILE64 = new ElfMachine(s(187), "TILE64");

    /**
     * Tilera TILEPro
     */
    @ElfApi("EM_TILEPRO")
    public static final ElfMachine TILEPRO = new ElfMachine(s(188), "TILEPRO");

    /**
     * Xilinx MicroBlaze
     */
    @ElfApi("EM_MICROBLAZE")
    public static final ElfMachine MICROBLAZE = new ElfMachine(s(189), "MICROBLAZE");

    /**
     * NVIDIA CUDA
     */
    @ElfApi("EM_CUDA")
    public static final ElfMachine CUDA = new ElfMachine(s(190), "CUDA");

    /**
     * Tilera TILE-Gx
     */
    @ElfApi("EM_TILEGX")
    public static final ElfMachine TILEGX = new ElfMachine(s(191), "TILEGX");

    /**
     * CloudShield
     */
    @ElfApi("EM_CLOUDSHIELD")
    public static final ElfMachine CLOUDSHIELD = new ElfMachine(s(192), "CLOUDSHIELD");

    /**
     * KIPO-KAIST Core-A 1st gen.
     */
    @ElfApi("EM_COREA_1ST")
    public static final ElfMachine COREA_1ST = new ElfMachine(s(193), "COREA_1ST");

    /**
     * KIPO-KAIST Core-A 2nd gen.
     */
    @ElfApi("EM_COREA_2ND")
    public static final ElfMachine COREA_2ND = new ElfMachine(s(194), "COREA_2ND");

    /**
     * Synopsys ARCompact V2
     */
    @ElfApi("EM_ARC_COMPACT2")
    public static final ElfMachine ARC_COMPACT2 = new ElfMachine(s(195), "ARC_COMPACT2");

    /**
     * Open8 RISC
     */
    @ElfApi("EM_OPEN8")
    public static final ElfMachine OPEN8 = new ElfMachine(s(196), "OPEN8");

    /**
     * Renesas RL78
     */
    @ElfApi("EM_RL78")
    public static final ElfMachine RL78 = new ElfMachine(s(197), "RL78");

    /**
     * Broadcom VideoCore V
     */
    @ElfApi("EM_VIDEOCORE5")
    public static final ElfMachine VIDEOCORE5 = new ElfMachine(s(198), "VIDEOCORE5");

    /**
     * Renesas 78KOR
     */
    @ElfApi("EM_78KOR")
    public static final ElfMachine RENESAS_78KOR = new ElfMachine(s(199), "RENESAS_78KOR");

    /**
     * Freescale 56800EX DSC
     */
    @ElfApi("EM_56800EX")
    public static final ElfMachine FREESCALE_56800EX = new ElfMachine(s(200), "FREESCALE_56800EX");

    /**
     * Beyond BA1
     */
    @ElfApi("EM_BA1")
    public static final ElfMachine BA1 = new ElfMachine(s(201), "BA1");

    /**
     * Beyond BA2
     */
    @ElfApi("EM_BA2")
    public static final ElfMachine BA2 = new ElfMachine(s(202), "BA2");

    /**
     * XMOS xCORE
     */
    @ElfApi("EM_XCORE")
    public static final ElfMachine XCORE = new ElfMachine(s(203), "XCORE");

    /**
     * Microchip 8-bit PIC(r)
     */
    @ElfApi("EM_MCHP_PIC")
    public static final ElfMachine MCHP_PIC = new ElfMachine(s(204), "MCHP_PIC");

    /* reserved 205-209 */

    /**
     * KM211 KM32
     */
    @ElfApi("EM_KM32")
    public static final ElfMachine KM32 = new ElfMachine(s(210), "KM32");

    /**
     * KM211 KMX32
     */
    @ElfApi("EM_KMX32")
    public static final ElfMachine KMX32 = new ElfMachine(s(211), "KMX32");

    /**
     * KM211 KMX16
     */
    @ElfApi("EM_EMX16")
    public static final ElfMachine EMX16 = new ElfMachine(s(212), "EMX16");

    /**
     * KM211 KMX8
     */
    @ElfApi("EM_EMX8")
    public static final ElfMachine EMX8 = new ElfMachine(s(213), "EMX8");

    /**
     * KM211 KVARC
     */
    @ElfApi("EM_KVARC")
    public static final ElfMachine KVARC = new ElfMachine(s(214), "KVARC");

    /**
     * Paneve CDP
     */
    @ElfApi("EM_CDP")
    public static final ElfMachine CDP = new ElfMachine(s(215), "CDP");

    /**
     * Cognitive Smart Memory Processor
     */
    @ElfApi("EM_COGE")
    public static final ElfMachine COGE = new ElfMachine(s(216), "COGE");

    /**
     * Bluechip CoolEngine
     */
    @ElfApi("EM_COOL")
    public static final ElfMachine COOL = new ElfMachine(s(217), "COOL");

    /**
     * Nanoradio Optimized RISC
     */
    @ElfApi("EM_NORC")
    public static final ElfMachine NORC = new ElfMachine(s(218), "NORC");

    /**
     * CSR Kalimba
     */
    @ElfApi("EM_CSR_KALIMBA")
    public static final ElfMachine CSR_KALIMBA = new ElfMachine(s(219), "CSR_KALIMBA");

    /**
     * Zilog Z80
     */
    @ElfApi("EM_Z80")
    public static final ElfMachine Z80 = new ElfMachine(s(220), "Z80");

    /**
     * Controls and Data Services VISIUMcore
     */
    @ElfApi("EM_VISIUM")
    public static final ElfMachine VISIUM = new ElfMachine(s(221), "VISIUM");

    /**
     * FTDI Chip FT32
     */
    @ElfApi("EM_FT32")
    public static final ElfMachine FT32 = new ElfMachine(s(222), "FT32");

    /**
     * Moxie processor
     */
    @ElfApi("EM_MOXIE")
    public static final ElfMachine MOXIE = new ElfMachine(s(223), "MOXIE");

    /**
     * AMD GPU
     */
    @ElfApi("EM_AMDGPU")
    public static final ElfMachine AMDGPU = new ElfMachine(s(224), "AMDGPU");

    /* reserved 225-242 */

    /**
     * RISC-V
     */
    @ElfApi("EM_RISCV")
    public static final ElfMachine RISCV = new ElfMachine(s(243), "RISCV");

    /**
     * Linux BPF -- in-kernel virtual machine
     */
    @ElfApi("EM_BPF")
    public static final ElfMachine BPF = new ElfMachine(s(247), "BPF");

    private ElfMachine(short value) {
        super(value);
    }

    private ElfMachine(short value, String name) {
        super(value, name, byValue, byName);
    }

    public static ElfMachine fromValue(short value) {
        return ShortPartialEnum.fromValueOrCreate(value, byValue, ElfMachine::new);
    }

    public static ElfMachine fromName(String name) {
        return ShortPartialEnum.fromName(name, byName);
    }

    public static Collection<ElfMachine> knownValues() {
        return ShortPartialEnum.knownValues(byValue);
    }

    private static AtomicReference<Map<String, String>> name2apiName = new AtomicReference<>(null);

    public String apiName() {
        if (name2apiName.get() == null) {
            HashMap<String, String> name2apiName = new HashMap<>();
            Class<?> thisClass = this.getClass();

            for (Field field : getClass().getFields()) {
                // Static fields only
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;

                // Field must be of enum type
                if (!thisClass.isAssignableFrom(field.getType())) continue;

                // Field must be @ElfApi annotation
                ElfApi elfApi = field.getAnnotation(ElfApi.class);
                if (elfApi == null) continue;

                try {
                    ShortPartialEnum<?> value = (ShortPartialEnum<?>)field.get(null);
                    // Add mapping
                    name2apiName.put(value.name(), elfApi.value());
                } catch(IllegalAccessException e) {
                    // Log but otherwise ignore
                    e.printStackTrace();
                }
            }

            ElfMachine.name2apiName.set(name2apiName);
        }

        if (name2apiName.get().containsKey(name())) {
            return name2apiName.get().get(name());
        } else {
            return String.format("0x%02x", value());
        }
    }

    private static AtomicReference<Map<String, String>> name2apiNameMappingContainer = new AtomicReference<>(null);
    @Override
    protected AtomicReference<Map<String, String>> name2ApiNameMappingContainer() {
        return name2apiNameMappingContainer;
    }
}

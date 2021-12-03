package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;

public abstract class ElfFile<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final ElfHeader<NATIVE_WORD> header;

    protected ElfFile(ElfHeader<NATIVE_WORD> header) {
        this.header = header;
    }

    public ElfHeader<NATIVE_WORD> header() {
        return header;
    }
}

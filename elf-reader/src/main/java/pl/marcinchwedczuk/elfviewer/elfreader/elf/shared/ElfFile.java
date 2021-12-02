package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;

public abstract class ElfFile<
        ADDRESS extends ElfAddress<ADDRESS>,
        OFFSET extends ElfOffset<OFFSET>,
        HEADER extends ElfHeader<ADDRESS, OFFSET>>
{
    private final HEADER header;

    protected ElfFile(HEADER header) {
        this.header = header;
    }

    public final HEADER header() {
        return header;
    }
}

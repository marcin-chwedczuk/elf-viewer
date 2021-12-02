package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;

public class Elf64File
        extends ElfFile<
            Elf64Address,
            Elf64Offset,
            Elf64Header>
{
    public Elf64File(Elf64Header header) {
        super(header);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;

public class Elf64File extends ElfFile<Long> {
    public Elf64File(Elf64Header header) {
        super(header);
    }

    @Override
    public Elf64Header header() {
        return (Elf64Header) super.header();
    }
}

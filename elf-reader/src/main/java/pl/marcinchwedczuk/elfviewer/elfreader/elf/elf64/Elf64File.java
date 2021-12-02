package pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;

public class Elf64File extends ElfFile {
    private final Elf64Header header;

    public Elf64File(Elf64Header header) {
        this.header = header;
    }

    public Elf64Header header() {
        return header;
    }
}

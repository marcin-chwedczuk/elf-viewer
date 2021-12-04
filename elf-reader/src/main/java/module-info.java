module pl.marcinchwedczuk.elfviewer.elfreader {
    exports pl.marcinchwedczuk.elfviewer.elfreader;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32.intel32;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;
    exports pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments;

    exports pl.marcinchwedczuk.elfviewer.elfreader.elf.elf64;

    exports pl.marcinchwedczuk.elfviewer.elfreader.io;
    exports pl.marcinchwedczuk.elfviewer.elfreader.endianness;
    exports pl.marcinchwedczuk.elfviewer.elfreader.utils;
}
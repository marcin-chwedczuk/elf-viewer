package pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;

public interface Elf32Visitor {
    void enter(ElfIdentification identification);
    void exit(ElfIdentification identification);

    void enter(Elf32Header header);
    void exit(Elf32Header header);

    void enterSections();
    void enter(Elf32Section section);
    void exit(Elf32Section section);
    void enter(Elf32DynamicSection section);
    void exit(Elf32DynamicSection section);
    void enter(Elf32InterpreterSection section);
    void exit(Elf32InterpreterSection section);
    void enter(Elf32NotesSection section);
    void exit(Elf32NotesSection section);
    void enter(Elf32RelocationSection section);
    void exit(Elf32RelocationSection section);
    void enter(Elf32StringTableSection section);
    void exit(Elf32StringTableSection section);
    void enter(Elf32SymbolTableSection section);
    void exit(Elf32SymbolTableSection section);
    void enter(Elf32GnuHashSection section);
    void exit(Elf32GnuHashSection section);
    void enter(Elf32InvalidSection section);
    void exit(Elf32InvalidSection section);
    void exitSections();

    void enterSegments();
    void enter(Elf32Segment programHeader);
    void exit(Elf32Segment programHeader);
    void exitSegments();
}

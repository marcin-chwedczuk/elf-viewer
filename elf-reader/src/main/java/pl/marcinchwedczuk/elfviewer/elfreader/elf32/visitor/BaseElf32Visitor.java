package pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;

public abstract class BaseElf32Visitor implements Elf32Visitor {
    @Override
    public void enter(ElfIdentification identification) { }
    @Override
    public void exit(ElfIdentification identification) { }

    @Override
    public void enter(Elf32Header header) { }
    @Override
    public void exit(Elf32Header header) { }

    @Override
    public void enterSections() { }

    @Override
    public void enter(Elf32BasicSection section) { }
    @Override
    public void exit(Elf32BasicSection section) { }

    @Override
    public void enter(Elf32DynamicSection section) { }
    @Override
    public void exit(Elf32DynamicSection section) { }

    @Override
    public void enter(Elf32InterpreterSection section) { }
    @Override
    public void exit(Elf32InterpreterSection section) { }

    @Override
    public void enter(Elf32NotesSection section) { }
    @Override
    public void exit(Elf32NotesSection section) { }

    @Override
    public void enter(Elf32RelocationSection section) { }
    @Override
    public void exit(Elf32RelocationSection section) { }

    @Override
    public void enter(Elf32StringTableSection section) { }
    @Override
    public void exit(Elf32StringTableSection section) { }

    @Override
    public void enter(Elf32SymbolTableSection section) { }
    @Override
    public void exit(Elf32SymbolTableSection section) { }

    @Override
    public void enter(Elf32GnuHashSection section) { }
    @Override
    public void exit(Elf32GnuHashSection section) { }


    @Override
    public void enter(Elf32InvalidSection section) { }
    @Override
    public void exit(Elf32InvalidSection section) { }

    @Override
    public void exitSections() { }

    @Override
    public void enterSegments() { }

    @Override
    public void enter(Elf32Segment segment) { }
    @Override
    public void exit(Elf32Segment segment) { }

    @Override
    public void exitSegments() { }
}

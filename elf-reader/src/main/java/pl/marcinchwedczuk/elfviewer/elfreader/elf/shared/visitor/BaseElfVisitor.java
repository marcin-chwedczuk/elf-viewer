package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;

public class BaseElfVisitor<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
        implements ElfVisitor<NATIVE_WORD>
{

    @Override
    public void enter(ElfIdentification identification) {

    }

    @Override
    public void exit(ElfIdentification identification) {

    }

    @Override
    public void enter(ElfHeader<NATIVE_WORD> header) {

    }

    @Override
    public void exit(ElfHeader<NATIVE_WORD> header) {

    }

    @Override
    public void enterSections() {

    }

    @Override
    public void enter(ElfSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfDynamicSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfDynamicSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfInterpreterSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfInterpreterSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfNotesSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfNotesSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfRelocationSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfRelocationSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfRelocationAddendSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfRelocationAddendSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfStringTableSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfStringTableSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfSymbolTableSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfSymbolTableSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfGnuHashSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfGnuHashSection<NATIVE_WORD> section) {

    }

    @Override
    public void enter(ElfInvalidSection<NATIVE_WORD> section) {

    }

    @Override
    public void exit(ElfInvalidSection<NATIVE_WORD> section) {

    }

    @Override
    public void exitSections() {

    }

    @Override
    public void enterSegments() {

    }

    @Override
    public void enter(ElfSegment<NATIVE_WORD> programHeader) {

    }

    @Override
    public void exit(ElfSegment<NATIVE_WORD> programHeader) {

    }

    @Override
    public void exitSegments() {

    }
}

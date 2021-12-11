package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;

public interface ElfVisitor<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    void enter(ElfIdentification identification);
    void exit(ElfIdentification identification);

    void enter(ElfHeader<NATIVE_WORD> header);
    void exit(ElfHeader<NATIVE_WORD> header);

    void enterSections();
    void enter(ElfSection<NATIVE_WORD> section);
    void exit(ElfSection<NATIVE_WORD> section);
    void enter(ElfDynamicSection<NATIVE_WORD> section);
    void exit(ElfDynamicSection<NATIVE_WORD> section);
    void enter(ElfInterpreterSection<NATIVE_WORD> section);
    void exit(ElfInterpreterSection<NATIVE_WORD> section);
    void enter(ElfNotesSection<NATIVE_WORD> section);
    void exit(ElfNotesSection<NATIVE_WORD> section);
    void enter(ElfRelocationSection<NATIVE_WORD> section);
    void exit(ElfRelocationSection<NATIVE_WORD> section);
    void enter(ElfRelocationAddendSection<NATIVE_WORD> section);
    void exit(ElfRelocationAddendSection<NATIVE_WORD> section);
    void enter(ElfStringTableSection<NATIVE_WORD> section);
    void exit(ElfStringTableSection<NATIVE_WORD> section);
    void enter(ElfSymbolTableSection<NATIVE_WORD> section);
    void exit(ElfSymbolTableSection<NATIVE_WORD> section);
    void enter(ElfGnuHashSection<NATIVE_WORD> section);
    void exit(ElfGnuHashSection<NATIVE_WORD> section);
    void enter(ElfInvalidSection<NATIVE_WORD> section);
    void exit(ElfInvalidSection<NATIVE_WORD> section);
    void enter(ElfGnuVersionSection<NATIVE_WORD> section);
    void exit(ElfGnuVersionSection<NATIVE_WORD> section);
    void enter(ElfGnuVersionRequirementsSection<NATIVE_WORD> section);
    void exit(ElfGnuVersionRequirementsSection<NATIVE_WORD> section);
    void exitSections();

    void enterSegments();
    void enter(ElfSegment<NATIVE_WORD> programHeader);
    void exit(ElfSegment<NATIVE_WORD> programHeader);
    void exitSegments();
}

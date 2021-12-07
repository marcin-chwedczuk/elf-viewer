package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;

public abstract class ElfElement<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    public abstract void accept(ElfVisitor<NATIVE_WORD> visitor);
}

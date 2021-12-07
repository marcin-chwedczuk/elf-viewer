package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor;

public interface ElfVisitable {
    void accept(ElfVisitor visitor);
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor;

public interface Elf32Visitable {
    void accept(Elf32Visitor visitor);
}

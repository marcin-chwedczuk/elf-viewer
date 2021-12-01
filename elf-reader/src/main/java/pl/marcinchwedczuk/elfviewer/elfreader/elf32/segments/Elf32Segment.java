package pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Element;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Elf32Segment extends Elf32Element {
    private final Elf32File elfFile;
    private final Elf32ProgramHeader programHeader;
    private final List<Elf32Section> sections;

    public Elf32Segment(Elf32File elfFile,
                        Elf32ProgramHeader programHeader,
                        List<Elf32Section> sections) {
        this.elfFile = elfFile;
        this.programHeader = requireNonNull(programHeader);
        this.sections = new ArrayList<>(sections);
    }

    public Elf32ProgramHeader programHeader() {
        return programHeader;
    }

    public List<Elf32Section> containedSections() {
        return Collections.unmodifiableList(sections);
    }

    public FileView contents() {
        return new FileView(
                elfFile.storage(),
                programHeader.fileOffset(),
                programHeader.fileSize());
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

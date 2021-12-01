package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Note;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Offset;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.notes.Elf32NoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.NOTE;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class Elf32NotesSection extends Elf32Section {
    public Elf32NotesSection(Elf32File elfFile,
                             Elf32SectionHeader header) {
        super(elfFile, header);

        Args.checkSectionType(header, NOTE);
    }

    public List<Elf32Note> notes() {
        List<Elf32Note> notes = new ArrayList<>();

        long curr = 0L;
        FileView contents = contents();

        while (curr < contents.length()) {
            StructuredFile sf = new StructuredFile(
                    contents,
                    elfFile().endianness(),
                    curr);

            int nameLen = sf.readUnsignedInt();
            int descLen = sf.readUnsignedInt();
            int type = sf.readUnsignedInt();

            String name = sf.readFixedSizeStringWithAlignment(nameLen, 4);
            byte[] descriptor = sf.readFixedSizeByteArrayWithAlignment(descLen, 4);

            // TODO: Extract factory
            if ("GNU".equals(name)) {
                notes.add(Elf32NoteGnu.createGnuNote(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            } else {
                notes.add(new Elf32Note(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            }

            curr = sf.currentPositionInFile().longValue();
        }

        return notes;
    }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNote;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteGnu;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile32;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.NOTE;

public class ElfNotesSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfNotesSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory, ElfFile<NATIVE_WORD> elfFile, ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);

        Args.checkSectionType(header, NOTE);
    }

    public List<ElfNote> notes() {
        List<ElfNote> notes = new ArrayList<>();

        long curr = 0L;
        FileView contents = contents();

        while (curr < contents.length()) {
            StructuredFile32 sf = new StructuredFile32(
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
                notes.add(ElfNoteGnu.createGnuNote(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            } else {
                notes.add(new ElfNote(
                        nameLen, name,
                        descLen, descriptor,
                        type));
            }

            curr = sf.currentPositionInFile().value().longValue();
        }

        return notes;
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

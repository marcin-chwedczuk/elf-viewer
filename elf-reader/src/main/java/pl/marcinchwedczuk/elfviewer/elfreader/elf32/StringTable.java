package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.STRING_TABLE;

public class StringTable {
    private final AbstractFile file;
    private final Elf32SectionHeader section;

    public StringTable(AbstractFile file,
                       Elf32SectionHeader section) {
        requireNonNull(file);
        requireNonNull(section);

        Args.checkSectionType(section, STRING_TABLE);

        this.file = file;
        this.section = section;
    }

    String getStringAtIndex(StringTableIndex index) {
        long startOffset = section.offsetInFile().longValue() + index.intValue();
        long sectionEndOffset = section.sectionEndOffsetInFile().longValue();

        ByteList buffer = new ByteList();

        long offset = startOffset;
        while (offset < sectionEndOffset) {
            byte b = file.read(offset);
            if (b == 0) break;

            buffer.add(b);
            offset++;
        }

        return buffer.toAsciiString();
    }
}

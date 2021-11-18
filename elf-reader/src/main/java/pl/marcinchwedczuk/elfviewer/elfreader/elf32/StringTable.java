package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.STRING_TABLE;

/**
 * String table sections hold null-terminated character sequences,
 * commonly called strings.  The object file uses these strings to
 * represent symbol and section names.  One references a string as
 * an index into the string table section.  The first byte, which is
 * index zero, is defined to hold a null byte ('\0').  Similarly, a
 * string table's last byte is defined to hold a null byte, ensuring
 * null termination for all strings.
 */
public class StringTable {
    private final AbstractFile file;
    private final Elf32Offset offsetInFile;
    private final Elf32Offset endOffsetInFile;

    public StringTable(AbstractFile file,
                       Elf32SectionHeader section) {
        requireNonNull(file);
        requireNonNull(section);

        Args.checkSectionType(section, STRING_TABLE);

        this.file = file;
        this.offsetInFile = section.offsetInFile();
        this.endOffsetInFile = section.sectionEndOffsetInFile();
    }

    public StringTable(AbstractFile file,
                       Elf32Offset offsetInFile,
                       Elf32Offset endOffsetInFile) {
        // TODO: Improve
        this.file = file;
        this.offsetInFile = offsetInFile;
        this.endOffsetInFile = endOffsetInFile;
    }

    String getStringAtIndex(StringTableIndex index) {
        long startOffset = offsetInFile.longValue() + index.intValue();
        long sectionEndOffset = endOffsetInFile.longValue();

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

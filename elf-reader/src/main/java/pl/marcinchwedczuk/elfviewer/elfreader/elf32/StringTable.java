package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        this.offsetInFile = section.fileOffset();
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

    public boolean isValidIndex(StringTableIndex index) {
        long startOffset = offsetInFile.intValue();
        long sectionEndOffset = endOffsetInFile.intValue();

        return (index.intValue() < (sectionEndOffset - startOffset));
    }

    public Collection<StringTableEntry> getContents() {
        long sectionStartOffset = offsetInFile.intValue();
        long sectionEndOffset = endOffsetInFile.intValue();
        long curr = 0;

        List<StringTableEntry> result = new ArrayList<>();

        while ((sectionStartOffset + curr) < sectionEndOffset) {
            // TODO: int or long?
            StringTableIndex index = new StringTableIndex((int)curr);
            String s = getStringAtIndex(index);
            result.add(new StringTableEntry(index, s));

            curr += s.length() + 1; // +1 for NULL termination
        }

        return result;
    }

    public String getStringAtIndex(StringTableIndex index) {
        long startOffset = offsetInFile.intValue() + index.intValue();
        long sectionEndOffset = endOffsetInFile.intValue();

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

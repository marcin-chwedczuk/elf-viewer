package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared;

import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.STRING_TABLE;

/**
 * String table sections hold null-terminated character sequences,
 * commonly called strings.  The object file uses these strings to
 * represent symbol and section names.  One references a string as
 * an index into the string table section.  The first byte, which is
 * index zero, is defined to hold a null byte ('\0').  Similarly, a
 * string table's last byte is defined to hold a null byte, ensuring
 * null termination for all strings.
 */
public class ElfStringTable<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final AbstractFile file;
    private final ElfOffset<NATIVE_WORD> offsetInFile;
    private final ElfOffset<NATIVE_WORD> endOffsetInFile;

    public ElfStringTable(AbstractFile file,
                          ElfSectionHeader<NATIVE_WORD> section) {
        requireNonNull(file);
        requireNonNull(section);

        Args.checkSectionType(section, STRING_TABLE);

        this.file = file;
        this.offsetInFile = section.fileOffset();
        this.endOffsetInFile = section.sectionEndOffsetInFile();
    }

    public ElfStringTable(AbstractFile file,
                          ElfOffset<NATIVE_WORD> offsetInFile,
                          ElfOffset<NATIVE_WORD> endOffsetInFile) {
        // TODO: Improve
        this.file = file;
        this.offsetInFile = offsetInFile;
        this.endOffsetInFile = endOffsetInFile;
    }

    public boolean isValidIndex(StringTableIndex index) {
        long startOffset = offsetInFile.value().longValue();
        long sectionEndOffset = endOffsetInFile.value().longValue();

        return (index.intValue() < (sectionEndOffset - startOffset));
    }

    public Collection<StringTableEntry> getContents() {
        long sectionStartOffset = offsetInFile.value().longValue();
        long sectionEndOffset = endOffsetInFile.value().longValue();
        long curr = 0;

        List<StringTableEntry> result = new ArrayList<>();

        while ((sectionStartOffset + curr) < sectionEndOffset) {
            // TODO: int or long?
            StringTableIndex index = new StringTableIndex(Math.toIntExact(curr));
            String s = getStringAtIndex(index);
            result.add(new StringTableEntry(index, s));

            curr += s.length() + 1; // +1 for NULL termination
        }

        return result;
    }

    public String getStringAtIndex(StringTableIndex index) {
        long startOffset = offsetInFile.value().longValue() + index.intValue();
        long sectionEndOffset = endOffsetInFile.value().longValue();

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

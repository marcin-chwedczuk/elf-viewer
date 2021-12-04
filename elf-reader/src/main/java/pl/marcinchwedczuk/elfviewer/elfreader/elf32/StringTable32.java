package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.StringTable;
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
public class StringTable32 extends StringTable<Integer> {
    public StringTable32(AbstractFile file,
                         Elf32SectionHeader section) {
        super(file, section);
    }

    public StringTable32(AbstractFile file,
                         Elf32Offset offsetInFile,
                         Elf32Offset endOffsetInFile) {
        super(file, offsetInFile, endOffsetInFile);
    }
}

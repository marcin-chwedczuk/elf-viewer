package pl.marcinchwedczuk.elfviewer.elfreader.elf64;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfStringTable;
import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

/**
 * String table sections hold null-terminated character sequences,
 * commonly called strings.  The object file uses these strings to
 * represent symbol and section names.  One references a string as
 * an index into the string table section.  The first byte, which is
 * index zero, is defined to hold a null byte ('\0').  Similarly, a
 * string table's last byte is defined to hold a null byte, ensuring
 * null termination for all strings.
 */
public class Elf64StringTable extends ElfStringTable<Long> {
    public Elf64StringTable(AbstractFile file,
                            Elf64SectionHeader section) {
        super(file, section);
    }

    public Elf64StringTable(AbstractFile file,
                            Elf64Offset offsetInFile,
                            Elf64Offset endOffsetInFile) {
        super(file, offsetInFile, endOffsetInFile);
    }
}

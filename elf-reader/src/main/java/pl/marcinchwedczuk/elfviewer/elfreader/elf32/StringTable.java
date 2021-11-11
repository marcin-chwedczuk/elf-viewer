package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.io.AbstractFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringTable {
    private final AbstractFile file;
    private final Elf32SectionHeader section;

    public StringTable(AbstractFile file,
                       Elf32SectionHeader section) {
        if (!section.type().equals(ElfSectionType.StringTable))
            throw new IllegalArgumentException("Invalid section type!");

        this.file = file;
        this.section = section;
    }

    String getStringAtIndex(StringTableIndex index) {
        long startOffset = section.offsetInFile().longValue() + index.intValue();

        List<Byte> buffer = new ArrayList<>();

        long offset = startOffset;
        byte b = 0;
        do {
            b = file.read(offset);
            if (b != 0)
                buffer.add(b);
            offset++;
            // TODO: Check for string table end
        } while (b != 0);

        // TODO: Not optimal
        byte[] buffer2 = new byte[buffer.size()];
        for (int i = 0; i < buffer2.length; i++) {
            buffer2[i] = buffer.get(i);
        }

        return new String(buffer2, 0, buffer2.length, StandardCharsets.US_ASCII);
    }
}

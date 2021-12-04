package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;

public class ElfInvalidSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {

    private final Exception error;

    public ElfInvalidSection(NativeWord<NATIVE_WORD> nativeWord,
                             StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                             ElfFile<NATIVE_WORD> elfFile,
                             ElfSectionHeader<NATIVE_WORD> header,
                             Exception error) {
        super(nativeWord, structuredFileFactory, elfFile, header);
        this.error = error;
    }

    public Exception error() {
        return error;
    }
}

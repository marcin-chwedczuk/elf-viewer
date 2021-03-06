package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfStringTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.STRING_TABLE;

public class ElfStringTableSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfStringTableSection(NativeWord<NATIVE_WORD> nativeWord,
            StructuredFileFactory<NATIVE_WORD> structuredFileFactory,
                                 ElfFile<NATIVE_WORD> elfFile,
                                 ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);


        Args.checkSectionType(header, STRING_TABLE);
    }

    public ElfStringTable<NATIVE_WORD> stringTable() {
        // TODO: Add ctor contents + size
        return new ElfStringTable<>(
                contents(),
                nativeWord.zeroOffset(),
                nativeWord.zeroOffset().plus(header().size().longValue()));
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

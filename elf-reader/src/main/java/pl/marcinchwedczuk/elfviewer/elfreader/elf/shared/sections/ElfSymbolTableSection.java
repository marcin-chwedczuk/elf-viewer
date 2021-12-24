package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFileFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.DYNAMIC_SYMBOLS;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionType.SYMBOL_TABLE;

public class ElfSymbolTableSection<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > extends ElfSection<NATIVE_WORD> {
    public ElfSymbolTableSection(NativeWord<NATIVE_WORD> nativeWord, StructuredFileFactory<NATIVE_WORD> structuredFileFactory, ElfFile<NATIVE_WORD> elfFile, ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord, structuredFileFactory, elfFile, header);


        Args.checkSectionType(header(), SYMBOL_TABLE, DYNAMIC_SYMBOLS);
    }

    public ElfSymbolTable<NATIVE_WORD> symbolTable() {
        int stringTableSectionIndex = header().link();

        ElfStringTableSection<NATIVE_WORD> stringTableSection =
                (ElfStringTableSection<NATIVE_WORD>) elfFile().sections().get(stringTableSectionIndex);

        return new ElfSymbolTable<>(
                nativeWord, structuredFileFactory,
                elfFile(),
                header(),
                stringTableSection.stringTable());
    }

    @Override
    public void accept(ElfVisitor<NATIVE_WORD> visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

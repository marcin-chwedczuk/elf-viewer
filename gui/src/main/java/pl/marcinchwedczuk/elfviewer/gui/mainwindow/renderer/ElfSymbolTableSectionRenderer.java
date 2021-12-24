package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSymbolTableSection;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfSymbolTableSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfSymbolTableSection<NATIVE_WORD> symbolTableSection;

    public ElfSymbolTableSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                         ElfSymbolTableSection<NATIVE_WORD> symbolTableSection) {
        super(nativeWord);
        this.symbolTableSection = symbolTableSection;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Offset", indexAccessor(0), ALIGN_RIGHT),
                mkColumn("st_name", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("(st_name)", indexAccessor(2)),
                mkColumn("st_value", indexAccessor(3), ALIGN_RIGHT),
                mkColumn("st_size", indexAccessor(4), ALIGN_RIGHT),
                mkColumn("st_info", indexAccessor(5), ALIGN_RIGHT),
                mkColumn("ST_BIND", indexAccessor(6)),
                mkColumn("ST_TYPE", indexAccessor(7)),
                mkColumn("st_other", indexAccessor(8), ALIGN_RIGHT),
                mkColumn("ST_VISIBILITY", indexAccessor(9), ALIGN_RIGHT),
                mkColumn("st_shndx", indexAccessor(10), ALIGN_RIGHT),
                mkColumn("(st_shndx)", indexAccessor(11))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        ElfSymbolTable<NATIVE_WORD> elf32SymbolTable = symbolTableSection.symbolTable();

        return elf32SymbolTable.symbols().stream()
                .map(entry -> mkStrings(
                        hex(entry.index.intValue()),
                        hex(entry.symbol.nameIndex().intValue()),
                        entry.symbol.name(),
                        entry.symbol.value().toString(),
                        dec(entry.symbol.size()),
                        hex(entry.symbol.info()),
                        entry.symbol.binding().apiName(),
                        entry.symbol.symbolType().apiName(),
                        hex(entry.symbol.other()),
                        entry.symbol.visibility().apiName(),
                        hex(entry.symbol.index().intValue()),
                        entry.relatedSection != null
                                ? entry.relatedSection.name()
                                : entry.symbol.index().toString()))
                .collect(toList());
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfSymbolTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SymbolTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32SymbolTableSection;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.SymbolTableEntryDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class Elf32SymbolTableSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<SymbolTableEntryDto, NATIVE_WORD>
{
    private final ElfSymbolTableSection<NATIVE_WORD> symbolTableSection;

    public Elf32SymbolTableSectionRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                           ElfSymbolTableSection<NATIVE_WORD> symbolTableSection) {
        super(nativeWord);
        this.symbolTableSection = symbolTableSection;
    }

    @Override
    protected List<TableColumn<SymbolTableEntryDto, String>> defineColumns() {
        return List.of(
                mkColumn("Offset", SymbolTableEntryDto::getOffset, ALIGN_RIGHT),
                mkColumn("st_name", SymbolTableEntryDto::getNameIndex, ALIGN_RIGHT),
                mkColumn("(st_name)", SymbolTableEntryDto::getName),
                mkColumn("st_value", SymbolTableEntryDto::getValue, ALIGN_RIGHT),
                mkColumn("st_size", SymbolTableEntryDto::getSize, ALIGN_RIGHT),
                mkColumn("st_info", SymbolTableEntryDto::getInfo, ALIGN_RIGHT),
                mkColumn("ST_BIND", SymbolTableEntryDto::getBinding),
                mkColumn("ST_TYPE", SymbolTableEntryDto::getSymbolType),
                mkColumn("st_other", SymbolTableEntryDto::getOther, ALIGN_RIGHT),
                mkColumn("ST_VISIBILITY", SymbolTableEntryDto::getVisibility, ALIGN_RIGHT),
                mkColumn("st_shndx", SymbolTableEntryDto::getIndex, ALIGN_RIGHT),
                mkColumn("(st_shndx)", SymbolTableEntryDto::getRelatedSectionName)
        );
    }

    @Override
    protected List<? extends SymbolTableEntryDto> defineRows() {
        ElfSymbolTable<NATIVE_WORD> elf32SymbolTable = symbolTableSection.symbolTable();

        return elf32SymbolTable.symbols().stream()
                .map(entry -> new SymbolTableEntryDto(
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

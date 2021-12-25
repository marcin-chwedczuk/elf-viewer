package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;

import java.util.List;
import java.util.function.Predicate;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfSectionHeaderRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfSectionHeader<NATIVE_WORD> header;

    public ElfSectionHeaderRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                    ElfSectionHeader<NATIVE_WORD> header) {
        super(nativeWord);
        this.header = header;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Field\nName", indexAccessor(0)),
                mkColumn("Value\nRaw", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("Value\nParsed", indexAccessor(2)),
                mkColumn("Comment", indexAccessor(3))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return List.of(
                mkStrings("sh_name",
                        hex(header.nameIndex().intValue()),
                        header.name(),
                        ""),

                mkStrings("sh_type",
                        hex(header.type().value()),
                        header.type().apiName(),
                        ""),

                mkStrings("sh_flags",
                        hex(header.flags().intValue()),
                        header.flags().toString(),
                        ""),

                mkStrings("sh_addr", header.virtualAddress().toString()),
                mkStrings("sh_offset", header.fileOffset().toString()),
                mkStrings("sh_size", dec(header.size())),
                mkStrings("sh_link", hex(header.link())),
                mkStrings("sh_info", hex(header.info())),
                mkStrings("sh_addralign", dec(header.addressAlignment())),
                mkStrings("sh_entsize", dec(header.containedEntrySize()))
        );
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.GenericNumericItem;

import java.util.List;

public class SectionHeaderRenderer extends BaseRenderer<GenericNumericItem> {
    private final Elf32SectionHeader header;

    public SectionHeaderRenderer(Elf32SectionHeader header) {
        this.header = header;
    }

    @Override
    protected List<TableColumn<GenericNumericItem, String>> defineColumns() {
        return List.of(
                mkColumn("Field", GenericNumericItem::getFieldName),
                mkColumn("Value", GenericNumericItem::getValue)
        );
    }

    @Override
    protected List<? extends GenericNumericItem> defineRows() {
        return List.of(
                new GenericNumericItem("sh_name", hex(header.nameIndex().intValue())),
                new GenericNumericItem("name", header.name()),
                new GenericNumericItem("sh_type", header.type()),
                new GenericNumericItem("sh_flags", header.flags().toString()),
                new GenericNumericItem("sh_addr", header.inMemoryAddress()),
                new GenericNumericItem("sh_offset", header.offsetInFile()),
                new GenericNumericItem("sh_size", header.sectionSize()),
                new GenericNumericItem("sh_link", hex(header.link())),
                new GenericNumericItem("sh_info", hex(header.info())),
                new GenericNumericItem("sh_addralign", header.addressAlignment()),
                new GenericNumericItem("sh_entsize", header.containedEntrySize())
        );
    }
}

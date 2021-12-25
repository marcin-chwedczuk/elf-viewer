package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;

import java.util.List;
import java.util.function.Predicate;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ElfIdentificationRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final ElfIdentification identification;

    public ElfIdentificationRenderer(NativeWord<NATIVE_WORD> nativeWord,
                                     ElfIdentification identification) {
        super(nativeWord);
        this.identification = identification;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("Field Name", indexAccessor(0)),
                mkColumn("Raw\nValue", indexAccessor(1), ALIGN_RIGHT),
                mkColumn("Parsed\nValue", indexAccessor(2)),
                mkColumn("Comment", indexAccessor(3))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return List.of(
                mkStrings(
                        "EI_MAG0..3",
                        ByteArrays.toHexString(identification.magicBytes(), ":"),
                        identification.printableMagicString(),
                        ""),

                mkStrings("EI_CLASS",
                        hex(identification.elfClass().value()),
                        identification.elfClass().apiName(),
                        ""),

                mkStrings("EI_DATA",
                        hex(identification.elfData().value()),
                        identification.elfData().apiName(),
                        ""),

                mkStrings("EI_VERSION",
                        // This field occur twice in ELF header.
                        // In identification bytes it is 1 byte long.
                        hex((byte)identification.elfVersion().value()),
                        identification.elfVersion().apiName(),
                        ""),

                mkStrings("EI_OSABI",
                        hex(identification.osAbi().value()),
                        identification.osAbi().apiName(),
                        ""),

                mkStrings("EI_ABIVERSION",
                        hex(identification.osAbiVersion()),
                        dec(identification.osAbiVersion()),
                        ""),

                mkStrings("EI_PAD", ByteArrays.toHexString(identification.paddingBytes(), ":"))
        );
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}

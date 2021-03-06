package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto.HexRowDto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.ColumnAttributes.ALIGN_RIGHT;

public class ContentsHexRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<HexRowDto, NATIVE_WORD>
{
    public static final String ENTRY_NAME = "(contents)";

    private final FileView fileView;

    public ContentsHexRenderer(NativeWord<NATIVE_WORD> nativeWord,
                               FileView fileView) {
        super(nativeWord);
        this.fileView = fileView;
    }

    public FileView fileView() { return fileView; }

    @Override
    protected List<TableColumn<HexRowDto, String>> defineColumns() {
        return List.of(
                mkColumn("File Offset", HexRowDto::getAddress),
                mkColumn("00", HexRowDto::b0, ALIGN_RIGHT),
                mkColumn("01", HexRowDto::b1, ALIGN_RIGHT),
                mkColumn("02", HexRowDto::b2, ALIGN_RIGHT),
                mkColumn("03", HexRowDto::b3, ALIGN_RIGHT),
                mkColumn("04", HexRowDto::b4, ALIGN_RIGHT),
                mkColumn("05", HexRowDto::b5, ALIGN_RIGHT),
                mkColumn("06", HexRowDto::b6, ALIGN_RIGHT),
                mkColumn("07", HexRowDto::b7, ALIGN_RIGHT),
                mkColumn("08", HexRowDto::b8, ALIGN_RIGHT),
                mkColumn("09", HexRowDto::b9, ALIGN_RIGHT),
                mkColumn("0a", HexRowDto::ba, ALIGN_RIGHT),
                mkColumn("0b", HexRowDto::bb, ALIGN_RIGHT),
                mkColumn("0c", HexRowDto::bc, ALIGN_RIGHT),
                mkColumn("0d", HexRowDto::bd, ALIGN_RIGHT),
                mkColumn("0e", HexRowDto::be, ALIGN_RIGHT),
                mkColumn("0f", HexRowDto::bf, ALIGN_RIGHT),
                mkColumn("asciiView", HexRowDto::printableAsciiView)
        );
    }

    @Override
    protected List<HexRowDto> defineRows() {
        List<HexRowDto> rows = new ArrayList<>((int) (fileView.viewLength() / 16) + 1);

        // Align start to 16th byte
        long start = fileView.viewOffsetToFileOffset(0);
        int startOffset = 0;
        while ((start % 16) != 0) {
            start--;
            startOffset--;
        }

        for (int i = startOffset; i < fileView.viewLength(); i += 16) {
            rows.add(new HexRowDto(
                    String.format("%08X",  fileView.viewOffsetToFileOffset(i)),
                    i,
                    fileView));
        }

        return rows;
    }

    @Override
    protected Predicate<HexRowDto> createFilter(String searchPhrase) {
        Predicate<String[]> filter = mkStringsFilter(searchPhrase);
        return (dto) -> filter.test(new String[] { dto.hexView(), dto.printableAsciiView() });
    }
}

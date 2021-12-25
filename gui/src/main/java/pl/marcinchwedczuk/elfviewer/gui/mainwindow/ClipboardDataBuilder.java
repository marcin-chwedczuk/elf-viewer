package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Objects;

public class ClipboardDataBuilder {
    private static final String SEPARATOR = "|";

    private final StringBuilder sb = new StringBuilder();

    private int lastRow = -1;

    public void addCellData(int row, int col, Object data) {
        if (lastRow != row) {
            // new row
            if (lastRow != -1) {
                sb.append('\n');
            }
            lastRow = row;
        } else {
            sb.append(SEPARATOR);
        }

        sb.append(Objects.toString(data));
    }

    public void copyToClipboard() {
        final ClipboardContent content = new ClipboardContent();
        content.putString(sb.toString());

        Clipboard.getSystemClipboard().setContent(content);
    }
}

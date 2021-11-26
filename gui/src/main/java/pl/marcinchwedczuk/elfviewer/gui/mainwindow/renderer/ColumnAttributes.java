package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;

public enum ColumnAttributes {
    ALIGN_RIGHT {
        @Override
        public void apply(TableColumn<?, ?> column) {
            String style = column.getStyle();
            style = style + " -fx-alignment: CENTER-RIGHT;";
            column.setStyle(style);
        }
    };

    public abstract void apply(TableColumn<?,?> column);
}

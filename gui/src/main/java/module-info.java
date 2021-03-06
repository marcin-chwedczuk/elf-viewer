module pl.marcinchwedczuk.elfviewer.gui {
    requires pl.marcinchwedczuk.elfviewer.elfreader;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    exports pl.marcinchwedczuk.elfviewer.gui;
    exports pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;
    exports pl.marcinchwedczuk.elfviewer.gui.mainwindow;

    // Allow @FXML injection to private fields.
    opens pl.marcinchwedczuk.elfviewer.gui.mainwindow;
    opens pl.marcinchwedczuk.elfviewer.gui.aboutdialog;
    opens pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;
    exports pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;
}
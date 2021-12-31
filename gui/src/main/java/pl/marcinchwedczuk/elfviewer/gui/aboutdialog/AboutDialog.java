package pl.marcinchwedczuk.elfviewer.gui.aboutdialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pl.marcinchwedczuk.elfviewer.gui.App;

import java.io.IOException;

public class AboutDialog {
    public static AboutDialog show(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AboutDialog.class.getResource("AboutDialog.fxml"));

            Stage childWindow = new Stage();
            childWindow.initOwner(owner);
            childWindow.initModality(Modality.WINDOW_MODAL);
            childWindow.initStyle(StageStyle.UTILITY);
            childWindow.setTitle("About...");
            childWindow.setScene(new Scene(loader.load()));
            childWindow.setResizable(false);
            childWindow.sizeToScene();

            AboutDialog controller = loader.getController();
            childWindow.show();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private GridPane rootElement;

    @FXML
    private void guiClose() {
        ((Stage)rootElement.getScene().getWindow()).close();
    }

    @FXML
    private void linkIcons8() {
        App.hostServices().showDocument("https://icons8.com/");
    }

    @FXML
    private void linkSourceCode() {
        App.hostServices().showDocument("https://github.com/marcin-chwedczuk/elf-viewer");
    }

    @FXML
    private void linkBugReport() {
        App.hostServices().showDocument("https://github.com/marcin-chwedczuk/elf-viewer/issues");
    }
}

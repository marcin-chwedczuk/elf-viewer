package pl.marcinchwedczuk.elfviewer.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class UiService {
    public static void infoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Information");
        alert.setContentText(message);
        // ID for tests
        alert.getDialogPane().setId("info-dialog");
        alert.showAndWait();
    }

    public static void errorDialog(String message) {
        errorDialog(message, "");
    }
    public static void errorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        // ID for tests
        alert.getDialogPane().setId("error-dialog");
        alert.showAndWait();
    }

    public static boolean confirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        // alert.setHeaderText("Question");
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }
}
package pl.marcinchwedczuk.elfviewer.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.MainWindow;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            showExceptionDialog(e);
        });

        MainWindow.showOn(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void showExceptionDialog(Throwable e) {
        StringBuilder msg = new StringBuilder();
        msg.append("Unhandled exception:\n");

        Throwable curr = e;
        while (curr != null) {
            if (curr.getMessage() != null && !curr.getMessage().isBlank()) {
                msg.append(curr.getClass().getSimpleName()).append(": ")
                        .append(curr.getMessage())
                        .append("\n");
            }
            curr = curr.getCause();
        }

        UiService.errorDialog("Unhandled exception", msg.toString());
    }
}
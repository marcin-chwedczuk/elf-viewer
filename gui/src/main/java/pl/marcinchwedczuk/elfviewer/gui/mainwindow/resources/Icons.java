package pl.marcinchwedczuk.elfviewer.gui.mainwindow.resources;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class Icons {
    private Icons() { }

    public static Image saveIcon() {
        return load("save-icon");
    }

    private static Image load(String iconName) {
        try (InputStream res = Icons.class.getResourceAsStream(iconName + ".png")) {
            return new Image(res);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

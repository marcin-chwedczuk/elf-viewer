package pl.marcinchwedczuk.elfviewer.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class AppSettings {
    private static final String KEY_recentFiles = "recentFiles";

    private final Preferences preferences = Preferences
            .userNodeForPackage(AppSettings.class);

    public List<File> recentFiles() {
        List<File> result = new ArrayList<>();

        int i = 0;
        while (true) {
            String path = preferences.get(KEY_recentFiles + i, null);
            if (path == null || path.isBlank())
                break;

            result.add(new File(path));
            i++;
        }

        return result;
    }

    public void addRecentFile(File file) {
        List<File> current = recentFiles();

        current.removeIf(f -> f.equals(file));
        current.add(0, file);

        // Overwrite old entries
        int i = 0;
        for (; i < current.size(); i++) {
            preferences.put(KEY_recentFiles + i, current.get(i).getAbsolutePath());
        }

        // Remove redundant entries
        while (preferences.get(KEY_recentFiles + i, null) != null) {
            preferences.remove(KEY_recentFiles + i);
            i++;
        }
    }

    public void save() {
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Cannot save app preferences.", e);
        }
    }
}

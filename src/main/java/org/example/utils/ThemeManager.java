package org.example.utils;

import java.util.prefs.Preferences;
import javafx.scene.Scene;

public class ThemeManager {
    private static final String LIGHT = "/css/light.css";
    private static final String DARK  = "/css/dark.css";
    private static boolean darkMode;
    private static final Preferences PREFS = Preferences.userNodeForPackage(ThemeManager.class);

    static {
        // lit la préférence stockée (false par défaut)
        darkMode = PREFS.getBoolean("darkMode", false);
    }

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String sheet = darkMode ? DARK : LIGHT;
        scene.getStylesheets().add(ThemeManager.class.getResource(sheet).toExternalForm());
    }

    public static void toggle(Scene scene) {
        darkMode = !darkMode;
        PREFS.putBoolean("darkMode", darkMode);
        applyTheme(scene);
    }

    public static boolean isDark() {
        return darkMode;
    }
}

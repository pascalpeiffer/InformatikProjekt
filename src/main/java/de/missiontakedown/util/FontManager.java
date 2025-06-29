package de.missiontakedown.util;

import de.missiontakedown.resources.ResourceManager;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jan
 */
public class FontManager {

    public static Font JERSEY_10;
    public static Font JERSEY_20;

    public static void registerFonts() throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        JERSEY_10 = loadFont("/font/Jersey-10.ttf", ge);
        JERSEY_20 = loadFont("/font/Jersey-20.ttf", ge);
    }

    private static Font loadFont(String resourceLocation, GraphicsEnvironment ge) throws IOException {
        try (InputStream is = ResourceManager.class.getResourceAsStream(resourceLocation)) {
            assert is != null;
            Font f = Font.createFont(Font.TRUETYPE_FONT, is);
            ge.registerFont(f);
            return f;
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

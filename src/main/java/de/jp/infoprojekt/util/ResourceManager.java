package de.jp.infoprojekt.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ResourceManager {

    public static BufferedImage TITLE_SCENE_BACKGROUND;

    public static void loadImageResources() throws IOException {
        TITLE_SCENE_BACKGROUND = ImageIO.read(Objects.requireNonNull(ResourceManager.class.getResource("/titleSceneBackground.png")));
    }

}

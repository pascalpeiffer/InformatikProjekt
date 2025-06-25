package de.jp.infoprojekt.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * AbstractResource class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class GameResource {

    private final BufferedImage imageResource;

    private float initialScalingX = 1f;
    private float initialScalingY = 1f;

    public GameResource(String resourceLocation) {
        try {
            imageResource = ImageIO.read(Objects.requireNonNull(ResourceManager.class.getResource(resourceLocation)));
        } catch (IOException e) {
            //Should not happen
            System.out.println("Failed to load resource " + resourceLocation);
            throw new RuntimeException(e);
        }
    }

    public GameResource(String resourceLocation, float initialScaling) {
        this(resourceLocation);
        this.initialScalingX = initialScaling;
        this.initialScalingY = initialScaling;
    }

    public GameResource(String resourceLocation, float initialScalingX, float initialScalingY) {
        this(resourceLocation);
        this.initialScalingX = initialScalingX;
        this.initialScalingY = initialScalingY;
    }

    public BufferedImage getResource() {
        return imageResource;
    }

    public int getWidth() {
        return (int) (imageResource.getWidth() * initialScalingX * ResourceManager.getScaling().getX());
    }

    public int getHeight() {
        return (int) (imageResource.getHeight() * initialScalingY * ResourceManager.getScaling().getY());
    }

}

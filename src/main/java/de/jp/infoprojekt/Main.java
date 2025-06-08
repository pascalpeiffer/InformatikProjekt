package de.jp.infoprojekt;

import de.jp.infoprojekt.ui.GameScreen;
import de.jp.infoprojekt.util.ImageManager;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            ImageManager.loadImageResources();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new GameScreen().show();
    }

}

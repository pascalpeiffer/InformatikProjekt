package de.jp.infoprojekt.resources;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class GameAudioResource {

    private final URL audioInputURI;

    public GameAudioResource(String resourceLocation) {
        audioInputURI = ResourceManager.class.getResource(resourceLocation);
    }

    public Clip play() {
        return play(1f, () -> {});
    }

    public Clip play(Runnable run) {
        return play(1f, run);
    }

    public Clip play(float volume) {
        return play(volume, () -> {});
    }

    public Clip play(float volume, Runnable run) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioInputURI);
            clip.open(inputStream);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
                volumeControl.setValue(20f * (float) Math.log10(volume));
            }

            final Clip finalClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    new Thread(run).start();
                    finalClip.close(); // Release system resources
                    try {
                        inputStream.close(); // Also close the stream
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            clip.start();

            return clip;

        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

}

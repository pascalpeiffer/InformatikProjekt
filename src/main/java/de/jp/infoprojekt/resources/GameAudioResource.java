package de.jp.infoprojekt.resources;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameAudioResource {

    private final URL audioInputURI;

    private float initialVolume = 1f;

    public GameAudioResource(String resourceLocation) {
        audioInputURI = ResourceManager.class.getResource(resourceLocation);
    }

    public GameAudioResource(String resourceLocation, float volume) {
        this(resourceLocation);
        initialVolume = volume;
    }

    /*public GameAudioResource play() {
        return play(initialVolume, () -> {}, 0f);
    }*/

    /*public Clip play(Runnable run) {
        return play(initialVolume, run, 0f);
    }

    public Clip play(float pan) {
        return play(initialVolume, () -> {}, pan);
    }

    public Clip play(float volume) {
        return play(volume, () -> {}, 0f);
    }

    public Clip play(float volume, Runnable run) {
        return play(volume, run, 0f);
    }*/

    public Instance create() {
        return new Instance();
    }

    public class Instance {

        private final Clip clip;

        private AudioInputStream inputStream;

        private final List<Runnable> endRunnables = new ArrayList<>();

        public Instance() {
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                inputStream = AudioSystem.getAudioInputStream(audioInputURI);
                clip.open(inputStream);
            }catch (Exception ignored) {}
            setVolume(initialVolume);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    endRunnables.forEach(runnable -> {
                        new Thread(runnable).start();
                    });
                    stop();
                }
            });
        }

        public Instance onEnd(Runnable run) {
            endRunnables.add(run);
            return this;
        }

        public Instance setPan(float pan) {
            FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
            if (panControl != null) {
                panControl.setValue(pan);
            }
            return this;
        }

        public Instance setVolume(float volume) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
                volumeControl.setValue(20f * (float) Math.log10(volume));
            }
            return this;
        }

        public float getVolume() {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null) {
                return (float) Math.pow(10, volumeControl.getValue() / 20f);
            }
            return initialVolume;
        }

        public Instance loop(int cound) {
            clip.loop(cound);
            return this;
        }

        public Instance play() {
            clip.start();
            return this;
        }

        public boolean isActive() {
            return clip.isActive();
        }

        public void stop() {
            clip.stop();
            clip.close();
            try {
                inputStream.close();
            } catch (IOException ignored) {}
        }

    }

}

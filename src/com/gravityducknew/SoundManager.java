package com.gravityducknew;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {

    private static Clip bgmClip;
    private static boolean isMuted = false;


    public static void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            if (bgmClip != null && bgmClip.isRunning()) {
                bgmClip.stop();
            }
        } else {
            if (bgmClip != null) {
                bgmClip.start();
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static void playSound(String path) {
        if (isMuted) return;

        new Thread(() -> {
            try {
                InputStream audioSrc = SoundManager.class.getResourceAsStream(path);
                if (audioSrc == null) return;

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void playBGM(String path) {
        stopBGM();
        new Thread(() -> {
            try {
                InputStream audioSrc = SoundManager.class.getResourceAsStream(path);
                if (audioSrc == null) return;

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);

                bgmClip = AudioSystem.getClip();
                bgmClip.open(ais);

                if (!isMuted) {
                    bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                    bgmClip.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}
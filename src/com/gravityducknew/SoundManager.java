package com.gravityducknew;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {

    // Clip nhạc nền để điều khiển dừng/phát
    private static Clip bgmClip;

    // Phát hiệu ứng âm thanh ngắn (SFX)
    public static void playSound(String path) {
        new Thread(() -> {
            try {
                URL soundUrl = SoundManager.class.getResource(path);
                if (soundUrl != null) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } else {
                    System.out.println("Lỗi: Không tìm thấy file âm thanh tại " + path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Phát nhạc nền (BGM) lặp vô tận
    public static void playBGM(String path) {
        stopBGM();
        new Thread(() -> {
            try {
                URL soundUrl = SoundManager.class.getResource(path);
                if (soundUrl != null) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl);
                    bgmClip = AudioSystem.getClip();
                    bgmClip.open(ais);
                    bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                    bgmClip.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Tắt nhạc nền
    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}
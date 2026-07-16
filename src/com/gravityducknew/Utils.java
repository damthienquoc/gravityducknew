package com.gravityducknew;

public class Utils {
    // Độ phân giải màn hình
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // FPS
    public static final int TARGET_FPS = 40;

    public enum GameState { MENU, PLAYING }
    public static GameState gameState = GameState.MENU;
}
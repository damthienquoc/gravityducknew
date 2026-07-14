package com.gravityducknew;

public class Utils {
    // Độ phân giải màn hình chuẩn Retro 4:3
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // Khóa game ở tốc độ 40 khung hình / giây
    public static final int TARGET_FPS = 40;

    public enum GameState { MENU, PLAYING }
    public static GameState gameState = GameState.MENU;
}
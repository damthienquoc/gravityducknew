package com.gravityducknew;

public class Utils {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // FPS
    public static final int TARGET_FPS = 40;

    public static final int TILE_SIZE = 32;

    public static final int TILE_EMPTY = 0;
    public static final int TILE_WALL = 1;
    public static final int TILE_EGG = 2;
    public static final int TILE_TRAP = 3;
    public static final int TILE_ROTATE = 4;

    public enum Gravity {
        UP, DOWN, LEFT, RIGHT
    }


    public enum GameState { MENU, PLAYING }
    public static GameState gameState = GameState.MENU;
}
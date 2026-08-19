package com.gravityducknew;

public class Utils {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // FPS
    public static final int TARGET_FPS = 40;

    public static final int TILE_SIZE = 32;

    public static final int TILE_EMPTY = 00;
    public static final int TILE_WALL1 = 11;
    public static final int TILE_WALL2 = 12;
    public static final int TILE_WALL3 = 13;
    public static final int TILE_WALL4 = 14;


    public static final int TILE_EGG1 = 21;
    public static final int TILE_EGG2 = 22;

    public static final int TILE_TRAP1 = 31;
    public static final int TILE_TRAP2 = 32;
    public static final int TILE_TRAP3 = 33;
    public static final int TILE_TRAP4 = 34;

    public static final int TILE_ROTATE = 44;

    public enum Gravity {
        UP, DOWN, LEFT, RIGHT
    }


    public enum GameState { MENU, PLAYING, PAUSE, WIN }
    public static GameState gameState = GameState.MENU;
}
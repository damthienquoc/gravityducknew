package com.gravityducknew;

public class Utils {
    // Độ phân giải màn hình
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // FPS
    public static final int TARGET_FPS = 40;

    public static final int TILE_SIZE = 32;

    public static final int TILE_EMPTY = 0;
    public static final int TILE_WALL = 1;
    public static final int TILE_EGG = 2;
    public static final int TILE_TRAP = 3;

    public static int[][] mapData = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1},
            {1,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1},
            {1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,1,0,0,0,0,0,0,1,1,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public enum GameState { MENU, PLAYING }
    public static GameState gameState = GameState.MENU;
}
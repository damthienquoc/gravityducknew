package com.gravityducknew;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelManager {
    private int levelHienTai = 1;
    private int maxLevel = 2;
    public int[][] mapHienTai = new int[15][20];

    public LevelManager() {
        loadLevel();
    }

    public void loadLevel(){
        String tenFile = "levels/level" + levelHienTai + ".txt";
        try {
            File file = new File(tenFile);
            Scanner scanner = new Scanner(file);
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 20; j++){
                    if(scanner.hasNextInt()){
                        mapHienTai[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
            System.out.println("Đã tải thành công " + tenFile);
        }
        catch (FileNotFoundException e){
            System.out.println("Không tìm thấy " + tenFile);
        }
    }

    public void nextLevel(){
        levelHienTai++;
        if(levelHienTai > maxLevel) levelHienTai = 1;
        loadLevel();
    }
    public boolean isLastLevel() {
        return levelHienTai >= maxLevel;
    }
    public void resetToFirstLevel() {
        levelHienTai = 1;
        loadLevel();
    }
    public int getCurrentLevel() {
        return levelHienTai;
    }
}

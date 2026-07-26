package com.gravityducknew;

import java.awt.*;

public class Map {
    public void drawMap(Graphics2D gd, LevelManager levelManager) {

        int[][] mapData = levelManager.mapHienTai;

        //vẽ map duyệt qua từng hàng và cột của ma trận
        for(int dong = 0; dong < mapData.length; dong++) {
            for(int cot = 0; cot < mapData[dong].length; cot++) {
                if(mapData[dong][cot] == Utils.TILE_WALL) {
                    gd.setColor(Color.DARK_GRAY);
                    gd.fillRect(cot * Utils.TILE_SIZE, dong * Utils.TILE_SIZE, Utils.TILE_SIZE, Utils.TILE_SIZE);
                }
                else if (mapData[dong][cot] == Utils.TILE_EGG){
                    gd.setColor(Color.GREEN);
                    gd.fillOval(cot * Utils.TILE_SIZE + 6, dong * Utils.TILE_SIZE +4,Utils.TILE_SIZE - 12, Utils.TILE_SIZE - 8);
                }
            }
        }
    }
}

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
                else if(mapData[dong][cot] == Utils.TILE_TRAP){
                    gd.setColor(Color.RED);
                    int[] x = {cot * Utils.TILE_SIZE, cot * Utils.TILE_SIZE + Utils.TILE_SIZE / 2, cot * Utils.TILE_SIZE + Utils.TILE_SIZE};
                    int [] y = {dong * Utils.TILE_SIZE + Utils.TILE_SIZE, dong * Utils.TILE_SIZE, dong * Utils.TILE_SIZE + Utils.TILE_SIZE};
                    gd.fillPolygon(x,y,3);
                }
                else if (mapData[dong][cot] == Utils.TILE_ROTATE) {
                    gd.setColor(Color.ORANGE);
                    gd.fillRect(cot * Utils.TILE_SIZE + 4, dong * Utils.TILE_SIZE + 4, Utils.TILE_SIZE - 8, Utils.TILE_SIZE - 8);
                    gd.setColor(Color.WHITE);
                    gd.drawRect(cot * Utils.TILE_SIZE + 8, dong * Utils.TILE_SIZE + 8, Utils.TILE_SIZE - 16, Utils.TILE_SIZE - 16);
                }
            }
        }
    }
}

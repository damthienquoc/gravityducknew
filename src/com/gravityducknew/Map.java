package com.gravityducknew;

import java.awt.*;

public class Map {
    public void drawMap(Graphics2D gd) {
        //vẽ map duyệt qua từng hàng và cột của ma trận
        for(int dong = 0; dong < Utils.mapData.length; dong++) {
            for(int cot = 0; cot < Utils.mapData[dong].length; cot++) {
                if(Utils.mapData[dong][cot] == Utils.TILE_EMPTY) {
                    gd.setColor(Color.DARK_GRAY);
                    gd.fillRect(dong * );
                }
            }
        }
    }
}

package com.gravityducknew;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public float vTriX, vTriY;
    public float vTocX , vTocY;
    public final float gtTrongLuc = 0.4f; //trọng lực
    public final float tocDo = 5f;
    public final float kichThuoc = 32;
    public int huongDiChuyen = 0; // 0 là đứng yên, 1 là sang trái, -1 là sang phải
    public boolean daoChieu = false;
    public Utils.Gravity gravity = Utils.Gravity.DOWN;
    public final float vTriBatDauX, vTriBatDauY;

    public Player(float viTriBatDauX, float vTriBatDauY) {
        this.vTriBatDauX = viTriBatDauX;
        this.vTriBatDauY = vTriBatDauY;
        vTriX = vTriBatDauX;
        vTriY = vTriBatDauY;
        gravity = Utils.Gravity.DOWN;
    }

    public void update(LevelManager levelManager) {

        switch (gravity){
            case DOWN:
                vTocY += gtTrongLuc;
                if(vTocY > 12f) vTocY = 12f;
                vTocX *= huongDiChuyen;
                break;
            case UP:
                vTocY -= gtTrongLuc;
                if(vTocY < -12f) vTocY = -12f;
                vTocX *= huongDiChuyen;
                break;
            case LEFT:
                vTocX -= gtTrongLuc;
                if (vTocX < -12.0f) vTocX = -12.0f;
                vTocY = tocDo * huongDiChuyen;
                break;
            case RIGHT:
                vTocX += gtTrongLuc;
                if (vTocX > 12.0f) vTocX = 12.0f;
                vTocY = tocDo * huongDiChuyen;
                break;
        }

        vTriX += vTocX;
        handleHorizontalCollision(levelManager.mapHienTai);
        vTriY += vTocY;
        handleVerticalCollision(levelManager.mapHienTai);


        handleTrapCollision(levelManager);
        handleEggCollision(levelManager);
    }

    public void draw(Graphics2D gd) {
        gd.setColor(Color.YELLOW);
        gd.fillRect((int) vTriX, (int) vTriY, (int) kichThuoc, (int) kichThuoc);
    }
    //xử lý bàn phím khi ấn
    public void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            huongDiChuyen = -1;
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            huongDiChuyen = 1;
        }

        if (key == KeyEvent.VK_SPACE) {
            if (daoChieu) {
                if (huongTrongLuc) huongTrongLuc = false;
                else huongTrongLuc = true;
                daoChieu = false;
            }
        }

    }

    // xử lý bàn phím khi thả
    public void handleKeyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        // Chỉ dừng lại khi phím thả ra trùng với hướng đang di chuyển
        if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && huongDiChuyen == -1) {
            huongDiChuyen = 0;
        }
        if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && huongDiChuyen == 1) {
            huongDiChuyen = 0;
        }
    }
    //xử lí va chạm ngang
    public void handleHorizontalCollision(int[][] mapData){
        for(int hang =  (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(mapData[hang][cot] == Utils.TILE_WALL){
                    if(vTocX > 0){
                        vTriX = cot * Utils.TILE_SIZE - kichThuoc;
                    }
                    else if (vTocX < 0){
                        vTriX = (cot + 1) * Utils.TILE_SIZE;
                    }
                    vTocX = 0;
                }
            }
        }
    }
    //xử lí va chạm dọc
    public void handleVerticalCollision(int[][] mapData){
        for(int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(mapData[hang][cot] == Utils.TILE_WALL){
                    if(vTocY > 0){
                        vTriY = hang * Utils.TILE_SIZE - kichThuoc;
                        daoChieu = true;
                    }
                    else if (vTocY < 0){
                        vTriY = (hang + 1) * Utils.TILE_SIZE;
                        daoChieu = true;
                    }
                    vTocY = 0;
                }
            }
        }
    }
    public void handleEggCollision(LevelManager levelManager){
        int[][] mapData = levelManager.mapHienTai;
        for(int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++){
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(mapData[hang][cot] == Utils.TILE_EGG){
                    levelManager.nextLevel();
                    reset();
                    return;
                }
            }
        }
    }

    public void handleTrapCollision(LevelManager levelManager){
        int[][] mapData = levelManager.mapHienTai;
        for(int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++){
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(mapData[hang][cot] == Utils.TILE_TRAP){
                    reset();
                    return;
                }
            }
        }
    }

    public void rotate() {
        switch (gravity) {
            case UP:

        }
    }

    public void handleRotateCollision(LevelManager levelManager){
        int[][] mapData = levelManager.mapHienTai;
        for(int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++){
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(mapData[hang][cot] == Utils.TILE_ROTATE){

                }
            }
        }
    }

    public void reset(){
        vTriX = vTriBatDauX;
        vTriY = vTriBatDauY;
        vTocX = 0;
        vTocY = 0;
        gravity = Utils.Gravity.DOWN;
    }
}

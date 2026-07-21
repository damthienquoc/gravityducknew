package com.gravityducknew;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public float vTriX, vTriY;
    public float vTocX, vTocY;
    public final float gtTrongLuc = 0.4f; //trọng lực
    public final float tocDo = 5f;
    public final float kichThuoc = 32;
    public int huongDiChuyen = 0; // 0 là đứng yên, 1 là sang trái, -1 là sang phải
    public boolean huongTrongLuc = false;
    public boolean daoChieu = false;

    public Player(float vTriX, float vTriY) {
        this.vTriX = vTriX;
        this.vTriY = vTriY;
    }

    public void update() {

        float trongLuc = huongTrongLuc ? -gtTrongLuc : gtTrongLuc;

        vTocY += trongLuc;
        vTocX = tocDo * huongDiChuyen;

        vTriX += vTocX;
        handleHorizontalCollision();
        vTriY += vTocY;
        handleVerticalCollision();
        if (huongTrongLuc && vTocY < -12.0f) vTocY = -12.0f;
        if (!huongTrongLuc && vTocY > 12.0f) vTocY = 12.0f;
    }

    public void draw(Graphics2D gr) {
        gr.setColor(Color.YELLOW);
        gr.fillRect((int) vTriX, (int) vTriY, (int) kichThuoc, (int) kichThuoc);
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
    public void handleHorizontalCollision(){
        for(int hang =  (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(Utils.mapData[hang][cot] == Utils.TILE_WALL){
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
    public void handleVerticalCollision(){
        for(int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for(int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++){
                if(Utils.mapData[hang][cot] == Utils.TILE_WALL){
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
}

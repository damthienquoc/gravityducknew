package com.gravityducknew;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public float vTriX, vTriY;
    public float vTocX, vTocY;
    public final float trongLuc = 0.4f; //trọng lực hút xuống
    public final float tocDo = 5f;
    public final float kichThuoc = 32;
    public int huongDiChuyen = 0; // 0 là đứng yên, 1 là sang trái, -1 là sang phải

    public Player(float vTriX, float vTriY) {
        this.vTriX = vTriX;
        this.vTriY = vTriY;
    }

    public void update() {
        vTocY += trongLuc;
        vTocX = tocDo * huongDiChuyen;
        vTriX += vTocX;
        vTriY += vTocY;

        if (vTocY > 10f) vTocY = 10f;

        if (vTriX < 0) vTriX = 0;
        if (vTriX >= Utils.WIDTH - kichThuoc) vTriX = Utils.WIDTH - kichThuoc;
        if (vTriY <= 0 + kichThuoc) {
            vTriY = 0;
            vTocY = 0;
        }
        if (vTriY >= Utils.HEIGHT - kichThuoc) {
            vTriY = Utils.HEIGHT - kichThuoc;
            vTocY = 0;
        }
    }

    public void draw(Graphics2D gr) {
        gr.setColor(Color.YELLOW);
        gr.fillRect((int) vTriX, (int) vTriY, (int) kichThuoc, (int) kichThuoc);
    }

    public void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            huongDiChuyen = -1; // Ấn nút qua trái
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            huongDiChuyen = 1;  // Ấn nút qua phải
        }


    }

    // Xử lý khi NGƯỜI CHƠI THẢ TAY KHỎI PHÍM
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
}

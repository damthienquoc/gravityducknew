package com.gravityducknew;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public float vTriX, vTriY;
    public float vTocX, vTocY;
    public final float gtTrongLuc = 0.5f; // Trọng lực vừa phải
    public final float tocDo = 4f;
    public final float kichThuoc = 32;
    public int huongDiChuyen = 0; // -1: Trái, 0: Đứng yên, 1: Phải
    public boolean daoChieu = false;

    // Biến chống lặp xoay khi dính vào ô Rotate
    private boolean daXoayTaiTile = false;

    public Utils.Gravity gravity = Utils.Gravity.DOWN;
    public final float vTriBatDauX, vTriBatDauY;

    public Player(float viTriBatDauX, float vTriBatDauY) {
        this.vTriBatDauX = viTriBatDauX;
        this.vTriBatDauY = vTriBatDauY;
        reset();
    }

    public void update(LevelManager levelManager) {

        // 1. Cập nhật vận tốc dựa theo Trọng Lực hiện tại
        switch (gravity) {
            case DOWN:
                vTocY += gtTrongLuc;
                if (vTocY > 10f) vTocY = 10f;
                vTocX = huongDiChuyen * tocDo; // Di chuyển ngang
                break;
            case UP:
                vTocY -= gtTrongLuc;
                if (vTocY < -10f) vTocY = -10f;
                vTocX = huongDiChuyen * tocDo; // Di chuyển ngang
                break;
            case LEFT:
                vTocX -= gtTrongLuc;
                if (vTocX < -10f) vTocX = -10f;
                vTocY = huongDiChuyen * tocDo; // Di chuyển dọc
                break;
            case RIGHT:
                vTocX += gtTrongLuc;
                if (vTocX > 10f) vTocX = 10f;
                vTocY = huongDiChuyen * tocDo; // Di chuyển dọc
                break;
        }

        // 2. Di chuyển X và kiểm tra va chạm X
        vTriX += vTocX;
        handleHorizontalCollision(levelManager.mapHienTai);

        // 3. Di chuyển Y và kiểm tra va chạm Y
        vTriY += vTocY;
        handleVerticalCollision(levelManager.mapHienTai);

        // 4. Kiểm tra các sự kiện ô đặc biệt
        handleRotateTileCollision(levelManager);
        handleTrapCollision(levelManager);
        handleEggCollision(levelManager);
    }

    public void draw(Graphics2D gd) {
        gd.setColor(Color.YELLOW);
        gd.fillRect((int) vTriX, (int) vTriY, (int) kichThuoc, (int) kichThuoc);
    }

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
                flipGravity180();
                daoChieu = false;
            }
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && huongDiChuyen == -1) {
            huongDiChuyen = 0;
        }
        if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && huongDiChuyen == 1) {
            huongDiChuyen = 0;
        }
    }

    // Va chạm Tường Ngang
    public void handleHorizontalCollision(int[][] mapData) {
        for (int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for (int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++) {
                if (hang >= 0 && hang < mapData.length && cot >= 0 && cot < mapData[0].length) {
                    if (mapData[hang][cot] == Utils.TILE_WALL1 || mapData[hang][cot] == Utils.TILE_WALL2 || mapData[hang][cot] == Utils.TILE_WALL3 || mapData[hang][cot] == Utils.TILE_WALL4) {
                        if (vTocX > 0) {
                            vTriX = cot * Utils.TILE_SIZE - kichThuoc;
                        } else if (vTocX < 0) {
                            vTriX = (cot + 1) * Utils.TILE_SIZE;
                        }
                        vTocX = 0;

                        // Nếu trọng lực đang đẩy sang Trái hoặc Phải -> Cho phép nhảy 180 độ
                        if (gravity == Utils.Gravity.LEFT || gravity == Utils.Gravity.RIGHT) {
                            daoChieu = true;
                        }
                    }
                }
            }
        }
    }

    // Va chạm Tường Dọc
    public void handleVerticalCollision(int[][] mapData) {
        for (int hang = (int)(vTriY / Utils.TILE_SIZE); hang <= (vTriY + kichThuoc - 1) / Utils.TILE_SIZE; hang++) {
            for (int cot = (int)(vTriX / Utils.TILE_SIZE); cot <= (vTriX + kichThuoc - 1) / Utils.TILE_SIZE; cot++) {
                if (hang >= 0 && hang < mapData.length && cot >= 0 && cot < mapData[0].length) {
                    if (mapData[hang][cot] == Utils.TILE_WALL1 || mapData[hang][cot] == Utils.TILE_WALL2 || mapData[hang][cot] == Utils.TILE_WALL3 || mapData[hang][cot] == Utils.TILE_WALL4) {
                        if (vTocY > 0) {
                            vTriY = hang * Utils.TILE_SIZE - kichThuoc;
                        } else if (vTocY < 0) {
                            vTriY = (hang + 1) * Utils.TILE_SIZE;
                        }
                        vTocY = 0;

                        // Nếu trọng lực đang đẩy Lên hoặc Down -> Cho phép nhảy 180 độ
                        if (gravity == Utils.Gravity.UP || gravity == Utils.Gravity.DOWN) {
                            daoChieu = true;
                        }
                    }
                }
            }
        }
    }

    // XỬ LÝ XOAY TRỌNG LỰC (ĐÃ SỬA HẾT LAG/GIẬT)
    public void handleRotateTileCollision(LevelManager levelManager) {
        int[][] mapData = levelManager.mapHienTai;
        boolean dangDungTrenRotateTile = false;

        // Chỉ kiểm tra các ô xung quanh vị trí Vịt để tối ưu hiệu năng
        int startHang = Math.max(0, (int)(vTriY / Utils.TILE_SIZE));
        int endHang = Math.min(mapData.length - 1, (int)((vTriY + kichThuoc - 1) / Utils.TILE_SIZE));
        int startCot = Math.max(0, (int)(vTriX / Utils.TILE_SIZE));
        int endCot = Math.min(mapData[0].length - 1, (int)((vTriX + kichThuoc - 1) / Utils.TILE_SIZE));

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_ROTATE) {
                    dangDungTrenRotateTile = true;

                    if (!daXoayTaiTile) {
                        rotateGravity(); // Xoay 90 độ

                        // Reset triệt để vận tốc cũ để tránh bị cộng dồn giật giật
                        vTocX = 0;
                        vTocY = 0;

                        // Đẩy nhẹ Vịt đi ngay lập tức theo hướng mới
                        float lucKich = 2.0f;
                        switch (gravity) {
                            case DOWN:  vTocY = lucKich;  break;
                            case UP:    vTocY = -lucKich; break;
                            case LEFT:  vTocX = -lucKich; break;
                            case RIGHT: vTocX = lucKich;  break;
                        }

                        daXoayTaiTile = true; // Khóa không cho xoay tiếp trong lúc đang ở trên ô này
                        daoChieu = false;     // Đang bay trong không trung -> Chưa cho đập SPACE
                        return;
                    }
                }
            }
        }

        // Nếu Vịt đã thoát ra khỏi ô Rotate -> Unlock để có thể xoay lần kế tiếp
        if (!dangDungTrenRotateTile) {
            daXoayTaiTile = false;
        }
    }

    public void handleEggCollision(LevelManager levelManager) {
        int[][] mapData = levelManager.mapHienTai;
        int startHang = Math.max(0, (int)(vTriY / Utils.TILE_SIZE));
        int endHang = Math.min(mapData.length - 1, (int)((vTriY + kichThuoc - 1) / Utils.TILE_SIZE));
        int startCot = Math.max(0, (int)(vTriX / Utils.TILE_SIZE));
        int endCot = Math.min(mapData[0].length - 1, (int)((vTriX + kichThuoc - 1) / Utils.TILE_SIZE));

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_EGG1) {
                    levelManager.nextLevel();
                    reset();
                    return;
                }
            }
        }
    }

    public void handleTrapCollision(LevelManager levelManager) {
        int[][] mapData = levelManager.mapHienTai;
        int startHang = Math.max(0, (int)(vTriY / Utils.TILE_SIZE));
        int endHang = Math.min(mapData.length - 1, (int)((vTriY + kichThuoc - 1) / Utils.TILE_SIZE));
        int startCot = Math.max(0, (int)(vTriX / Utils.TILE_SIZE));
        int endCot = Math.min(mapData[0].length - 1, (int)((vTriX + kichThuoc - 1) / Utils.TILE_SIZE));

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_TRAP1 || mapData[hang][cot] == Utils.TILE_TRAP2 || mapData[hang][cot] == Utils.TILE_TRAP3 || mapData[hang][cot] == Utils.TILE_TRAP4) {
                    reset();
                    return;
                }
            }
        }
    }

    public void rotateGravity() {
        switch (gravity) {
            case DOWN:  gravity = Utils.Gravity.LEFT; break;
            case LEFT:  gravity = Utils.Gravity.UP; break;
            case UP:    gravity = Utils.Gravity.RIGHT; break;
            case RIGHT: gravity = Utils.Gravity.DOWN; break;
        }
    }

    public void flipGravity180() {
        switch (gravity) {
            case DOWN:  gravity = Utils.Gravity.UP; break;
            case UP:    gravity = Utils.Gravity.DOWN; break;
            case LEFT:  gravity = Utils.Gravity.RIGHT; break;
            case RIGHT: gravity = Utils.Gravity.LEFT; break;
        }
    }

    public void reset() {
        vTriX = vTriBatDauX;
        vTriY = vTriBatDauY;
        vTocX = 0;
        vTocY = 0;
        huongDiChuyen = 0;
        daoChieu = false;
        daXoayTaiTile = false;
        gravity = Utils.Gravity.DOWN;
    }
}
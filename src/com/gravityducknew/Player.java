package com.gravityducknew;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player {
    public float vTriX, vTriY;
    public float vTocX, vTocY;
    public final float gtTrongLuc = 0.5f;
    public final float tocDo = 4f;
    public final float kichThuoc = 32;
    public int huongDiChuyen = 0; // -1: Trái, 0: Đứng yên, 1: Phải
    public boolean daoChieu = false;

    // Hướng nhìn ngang của Vịt (1: Phải, -1: Trái)
    private int huongNhin = 1;

    // Ảnh Spritesheet
    private BufferedImage idleImage;
    private BufferedImage walkImage;

    // Biến chống lặp xoay khi dính vào ô Rotate
    private boolean daXoayTaiTile = false;

    public Utils.Gravity gravity = Utils.Gravity.DOWN;
    public final float vTriBatDauX, vTriBatDauY;

    public Player(float viTriBatDauX, float vTriBatDauY) {
        this.vTriBatDauX = viTriBatDauX;
        this.vTriBatDauY = vTriBatDauY;
        loadPlayerSprites();
        reset();
    }

    private void loadPlayerSprites() {
        try {
            BufferedImage player = ImageIO.read(getClass().getResourceAsStream("/image/Player.png"));
            int size = Utils.TILE_SIZE;

            // Cắt ảnh lùi vào +5px và -10px đồng bộ với Map
            walkImage = player.getSubimage(0 , 0 , size, size);
            idleImage = player.getSubimage(size * 6, size , size , size );
        } catch (Exception e) {
            System.out.println("Lỗi: Không thể tải sprite Duck.png!");
            e.printStackTrace();
        }
    }

    public void update(LevelManager levelManager) {
        // Cập nhật hướng nhìn ngang khi di chuyển
        if (huongDiChuyen != 0) {
            huongNhin = huongDiChuyen;
        }

        // 1. Cập nhật vận tốc dựa theo Trọng Lực hiện tại
        switch (gravity) {
            case DOWN:
                vTocY += gtTrongLuc;
                if (vTocY > 10f) vTocY = 10f;
                vTocX = huongDiChuyen * tocDo;
                break;
            case UP:
                vTocY -= gtTrongLuc;
                if (vTocY < -10f) vTocY = -10f;
                vTocX = huongDiChuyen * tocDo;
                break;
            case LEFT:
                vTocX -= gtTrongLuc;
                if (vTocX < -10f) vTocX = -10f;
                vTocY = huongDiChuyen * tocDo;
                break;
            case RIGHT:
                vTocX += gtTrongLuc;
                if (vTocX > 10f) vTocX = 10f;
                vTocY = huongDiChuyen * tocDo;
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
        gd.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        BufferedImage imgHienTai = (huongDiChuyen != 0) ? walkImage : idleImage;
        if (imgHienTai == null) return;

        AffineTransform oldTransform = gd.getTransform();

        // 1. Tính tâm xoay
        float tamX = vTriX + kichThuoc / 2f;
        float tamY = vTriY + kichThuoc / 2f;
        gd.translate(tamX, tamY);

        // Biến xác định xem có cần lật ngược mặt con vịt hay không
        boolean latAnh = false;

        // 2. Xoay ma trận & Tính toán hướng lật mặt chuẩn theo góc nhìn màn hình
        switch (gravity) {
            case DOWN:
                // Chân bám Sàn (Dưới)
                // Di chuyển sang Trái (huongNhin = -1) -> Lật ảnh
                if (huongNhin == -1) latAnh = true;
                break;

            case UP:
                gd.rotate(Math.toRadians(180));
                // Chân bám Trần (Trên) - Tọa độ X bị đảo ngược
                // Khi đi sang Trái màn hình, Vịt đang tiến về hướng dương của trục cục bộ -> KHÔNG lật
                // Khi đi sang Phải màn hình (huongNhin = 1) -> Lật ảnh
                if (huongNhin == 1) latAnh = true;
                break;

            case LEFT:
                gd.rotate(Math.toRadians(90));
                // Chân bám Tường Trái - Vịt đứng dọc
                // Đi Lên (huongNhin = -1) / Đi Xuống (huongNhin = 1)
                if (huongNhin == -1) latAnh = true;
                break;

            case RIGHT:
                gd.rotate(Math.toRadians(270));
                // Chân bám Tường Phải - Tọa độ Y bị đảo ngược
                // Đi Xuống màn hình (huongNhin = 1) -> Lật ảnh
                if (huongNhin == 1) latAnh = true;
                break;
        }

        // 3. Vẽ ảnh
        int drawX = (int) (-kichThuoc / 2f);
        int drawY = (int) (-kichThuoc / 2f);
        int drawW = (int) kichThuoc;
        int drawH = (int) kichThuoc;

        if (latAnh) {
            // Lật ảnh theo chiều ngang cục bộ
            gd.drawImage(imgHienTai, drawX + drawW, drawY, -drawW, drawH, null);
        } else {
            gd.drawImage(imgHienTai, drawX, drawY, drawW, drawH, null);
        }

        gd.setTransform(oldTransform);
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
//                SoundManager.playSound("/sound/Space.wav");
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

                        if (gravity == Utils.Gravity.UP || gravity == Utils.Gravity.DOWN) {
                            daoChieu = true;
                        }
                    }
                }
            }
        }
    }

    public void handleRotateTileCollision(LevelManager levelManager) {
        int[][] mapData = levelManager.mapHienTai;
        boolean dangDungTrenRotateTile = false;

        int startHang = Math.max(0, (int)(vTriY / Utils.TILE_SIZE));
        int endHang = Math.min(mapData.length - 1, (int)((vTriY + kichThuoc - 1) / Utils.TILE_SIZE));
        int startCot = Math.max(0, (int)(vTriX / Utils.TILE_SIZE));
        int endCot = Math.min(mapData[0].length - 1, (int)((vTriX + kichThuoc - 1) / Utils.TILE_SIZE));

        // 1. Tính tâm hiện tại của Vịt
        float tamVitX = vTriX + kichThuoc / 2f;
        float tamVitY = vTriY + kichThuoc / 2f;

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_ROTATE) {
                    dangDungTrenRotateTile = true;

                    // 2. Tính tâm của ô Rotate Tile
                    float tamTileX = cot * Utils.TILE_SIZE + Utils.TILE_SIZE / 2f;
                    float tamTileY = hang * Utils.TILE_SIZE + Utils.TILE_SIZE / 2f;

                    // 3. Kiểm tra tâm Vịt đã đi vào vùng trung tâm của Tile chưa (Sai số 4px)
                    boolean daVaoTrungTam = Math.abs(tamVitX - tamTileX) <= 4f &&
                            Math.abs(tamVitY - tamTileY) <= 4f;

                    if (daVaoTrungTam && !daXoayTaiTile) {
                        rotateGravity();
//                        SoundManager.playSound("/sound/Rotate.wav");

                        // Căn chỉnh Vịt nằm chính giữa ô Rotate Tile ngay khi xoay
                        vTriX = tamTileX - kichThuoc / 2f;
                        vTriY = tamTileY - kichThuoc / 2f;

                        vTocX = 0;
                        vTocY = 0;

                        // Đẩy nhẹ Vịt bay tiếp theo hướng trọng lực mới
                        float lucKich = 2.0f;
                        switch (gravity) {
                            case DOWN:  vTocY = lucKich;  break;
                            case UP:    vTocY = -lucKich; break;
                            case LEFT:  vTocX = -lucKich; break;
                            case RIGHT: vTocX = lucKich;  break;
                        }

                        daXoayTaiTile = true;
                        daoChieu = false;
                        return;
                    }
                }
            }
        }

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
//                    SoundManager.playSound("/sound/egg.wav");
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
//                    SoundManager.playSound("/sound/Dead.wav");
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
        huongNhin = 1;
        daoChieu = false;
        daXoayTaiTile = false;
        gravity = Utils.Gravity.DOWN;
    }
}
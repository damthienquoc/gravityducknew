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


    public int huongDiChuyen = 0;
    public boolean daoChieu = false;


    private int huongNhin = 1;


    private boolean dangAnTrung = false;
    private int timerDelay = 0;
    private final int THOI_GIAN_DELAY = 30; // ~0.5s ở 60 FPS
    private LevelManager levelManagerTam;


    private BufferedImage idleImage;
    private BufferedImage walkImage;


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

            walkImage = player.getSubimage(0, 0, size, size);
            idleImage = player.getSubimage(size * 6, size, size, size);
        } catch (Exception e) {
            System.out.println("Lỗi: Không thể tải sprite Player.png!");
            e.printStackTrace();
        }
    }

    public void update(LevelManager levelManager) {
        if (dangAnTrung) {
            timerDelay++;
            vTocX = 0;
            vTocY = 0;
            huongDiChuyen = 0;

            if (timerDelay >= THOI_GIAN_DELAY) {
                dangAnTrung = false;
                timerDelay = 0;

                if (levelManagerTam.isLastLevel()) {
                    Utils.gameState = Utils.GameState.WIN;
                } else {
                    levelManagerTam.nextLevel();
                    reset();
                }
            }
            return;
        }


        if (huongDiChuyen != 0) {
            huongNhin = huongDiChuyen;
        }


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


        vTriX += vTocX;
        handleHorizontalCollision(levelManager.mapHienTai);


        vTriY += vTocY;
        handleVerticalCollision(levelManager.mapHienTai);

        handleRotateTileCollision(levelManager);
        handleTrapCollision(levelManager);
        handleEggCollision(levelManager);
    }

    public void draw(Graphics2D gd) {
        gd.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        BufferedImage imgHienTai = (huongDiChuyen != 0) ? walkImage : idleImage;
        if (imgHienTai == null) return;

        AffineTransform oldTransform = gd.getTransform();

        float tamX = vTriX + kichThuoc / 2f;
        float tamY = vTriY + kichThuoc / 2f;
        gd.translate(tamX, tamY);

        boolean latAnh = false;

        switch (gravity) {
            case DOWN:
                if (huongNhin == -1) latAnh = true;
                break;

            case UP:
                gd.rotate(Math.toRadians(180));
                if (huongNhin == 1) latAnh = true;
                break;

            case LEFT:
                gd.rotate(Math.toRadians(90));
                if (huongNhin == -1) latAnh = true;
                break;

            case RIGHT:
                gd.rotate(Math.toRadians(270));
                if (huongNhin == 1) latAnh = true;
                break;
        }

        int drawX = (int) (-kichThuoc / 2f);
        int drawY = (int) (-kichThuoc / 2f);
        int drawW = (int) kichThuoc;
        int drawH = (int) kichThuoc;

        if (latAnh) {
            gd.drawImage(imgHienTai, drawX + drawW, drawY, -drawW, drawH, null);
        } else {
            gd.drawImage(imgHienTai, drawX, drawY, drawW, drawH, null);
        }

        gd.setTransform(oldTransform);
    }

    public void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gravity == Utils.Gravity.DOWN || gravity == Utils.Gravity.UP) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                huongDiChuyen = -1;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                huongDiChuyen = 1;
            }
        } else {

            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                huongDiChuyen = -1;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                huongDiChuyen = 1;
            }
        }

        if (key == KeyEvent.VK_SPACE) {
            if (daoChieu) {
                flipGravity180();
                SoundManager.playSound("/sound/Space.wav");
                daoChieu = false;
            }
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (gravity == Utils.Gravity.DOWN || gravity == Utils.Gravity.UP) {
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && huongDiChuyen == -1) {
                huongDiChuyen = 0;
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && huongDiChuyen == 1) {
                huongDiChuyen = 0;
            }
        } else {
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && huongDiChuyen == -1) {
                huongDiChuyen = 0;
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && huongDiChuyen == 1) {
                huongDiChuyen = 0;
            }
        }
    }

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

        float tamVitX = vTriX + kichThuoc / 2f;
        float tamVitY = vTriY + kichThuoc / 2f;

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_ROTATE) {
                    dangDungTrenRotateTile = true;

                    float tamTileX = cot * Utils.TILE_SIZE + Utils.TILE_SIZE / 2f;
                    float tamTileY = hang * Utils.TILE_SIZE + Utils.TILE_SIZE / 2f;

                    boolean daVaoTrungTam = Math.abs(tamVitX - tamTileX) <= 4f &&
                            Math.abs(tamVitY - tamTileY) <= 4f;

                    if (daVaoTrungTam && !daXoayTaiTile) {
                        rotateGravity();
                        SoundManager.playSound("/sound/Rotate.wav");

                        vTriX = tamTileX - kichThuoc / 2f;
                        vTriY = tamTileY - kichThuoc / 2f;

                        vTocX = 0;
                        vTocY = 0;
                        huongDiChuyen = 0;

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
        if (dangAnTrung) return;

        int[][] mapData = levelManager.mapHienTai;
        int startHang = Math.max(0, (int)(vTriY / Utils.TILE_SIZE));
        int endHang = Math.min(mapData.length - 1, (int)((vTriY + kichThuoc - 1) / Utils.TILE_SIZE));
        int startCot = Math.max(0, (int)(vTriX / Utils.TILE_SIZE));
        int endCot = Math.min(mapData[0].length - 1, (int)((vTriX + kichThuoc - 1) / Utils.TILE_SIZE));

        for (int hang = startHang; hang <= endHang; hang++) {
            for (int cot = startCot; cot <= endCot; cot++) {
                if (mapData[hang][cot] == Utils.TILE_EGG1) {
                    SoundManager.playSound("/sound/egg.wav");
                    mapData[hang][cot] = Utils.TILE_EGG2; // Đổi sang ảnh vỡ

                    dangAnTrung = true;
                    timerDelay = 0;
                    levelManagerTam = levelManager;
                    stopMovement();
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
                    SoundManager.playSound("/sound/Dead.wav");
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
        dangAnTrung = false;
        timerDelay = 0;
        gravity = Utils.Gravity.DOWN;
    }

    public void stopMovement() {
        this.huongDiChuyen = 0;
        this.vTocX = 0;
        this.vTocY = 0;
    }
}
package com.gravityducknew;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Map {
    private BufferedImage empty, wall1, wall2, wall3, wall4, egg1, egg2, trap1, trap2, trap3, trap4, rotate;
    private boolean isLoaded = false;

    private void loadImgMap() {
        try {
            BufferedImage tiles = ImageIO.read(getClass().getResourceAsStream("/image/Tiles.png"));
            BufferedImage eggs = ImageIO.read(getClass().getResourceAsStream("/image/Egg.png"));
            rotate = ImageIO.read(getClass().getResourceAsStream("/image/Switch.png"));

            int size = Utils.TILE_SIZE;

            // Đồng bộ toàn bộ thông số cắt: x + 5, y + 5, width - 10, height - 10
            wall1 = tiles.getSubimage(size + 5, 0 + 5, size - 10, size - 10);
            empty = tiles.getSubimage(0 + 5, 0 + 5, size - 10, size - 10);
            wall2 = tiles.getSubimage(0 + 5, size + 5, size - 10, size - 10);
            wall3 = tiles.getSubimage(size * 2 + 5, size * 3 + 5, size - 10, size - 10);
            wall4 = tiles.getSubimage(size + 5, size + 5, size - 10, size - 10);

            egg1 = eggs.getSubimage(0 , 0 , size , size );
            egg2 = eggs.getSubimage(0 , size , size , size );

            trap1 = tiles.getSubimage(size * 9 , size * 3 + 16 , size , size - 16 );
            trap2 = tiles.getSubimage(size * 8 , size * 3 , size , size );
            trap3 = tiles.getSubimage(size * 7 , size * 3, size , size );
            trap4 = tiles.getSubimage(size * 10 , size * 3 , size , size );

            isLoaded = true;
        } catch (java.io.IOException e) {
            System.out.println("Lỗi: Không thể đọc file ảnh!");
            e.printStackTrace();
        }
    }

    public void drawMap(Graphics2D gd, LevelManager levelManager) {
        if (!isLoaded) {
            loadImgMap();
        }

        // Đảm bảo đồ họa Pixel Art hiển thị sắc nét khi scale lên màn hình
        gd.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int[][] mapData = levelManager.mapHienTai;

        // Vẽ map duyệt qua từng hàng và cột của ma trận
        for (int dong = 0; dong < mapData.length; dong++) {
            for (int cot = 0; cot < mapData[dong].length; cot++) {
                int x = cot * Utils.TILE_SIZE;
                int y = dong * Utils.TILE_SIZE;
                int tileType = mapData[dong][cot];

                if (tileType == Utils.TILE_WALL1 && wall1 != null) {
                    gd.drawImage(wall1, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_WALL2 && wall2 != null) {
                    gd.drawImage(wall2, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_WALL3 && wall3 != null) {
                    gd.drawImage(wall3, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_WALL4 && wall4 != null) {
                    gd.drawImage(wall4, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_ROTATE && rotate != null) {
                    gd.drawImage(rotate, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_TRAP1 && trap1 != null) {
                    gd.drawImage(trap1, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_TRAP2 && trap2 != null) {
                    gd.drawImage(trap2, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_TRAP3 && trap3 != null) {
                    gd.drawImage(trap3, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_TRAP4 && trap4 != null) {
                    gd.drawImage(trap4, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_EGG1 && egg1 != null) {
                    gd.drawImage(egg1, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_EGG2 && egg2 != null) {
                    gd.drawImage(egg2, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                } else if (tileType == Utils.TILE_EMPTY && empty != null) {
                    gd.drawImage(empty, x, y, Utils.TILE_SIZE, Utils.TILE_SIZE, null);
                }
            }
        }
    }
}
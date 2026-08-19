package com.gravityducknew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean running = false;

    private Player player = new Player(96, 352);
    private Map map = new Map();
    private LevelManager levelManager = new LevelManager();

    // 1. MỚI: Biến lưu trữ ảnh nền Background
    private BufferedImage bgImage;

    public GamePanel() {
        this.setPreferredSize(new Dimension(Utils.WIDTH, Utils.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        // 2. MỚI: Load ảnh nền từ thư mục res
        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/image/BG.png"));
        } catch (Exception e) {
            System.out.println("Lỗi: Không tìm thấy ảnh");
            e.printStackTrace();
        }

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Bấm Enter -> Vào game
                if (Utils.gameState == Utils.GameState.MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Utils.gameState = Utils.GameState.PLAYING;
                }

                // Phím bấm sang cho Player xử lý
                if (Utils.gameState == Utils.GameState.PLAYING) {
                    player.handleKeyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Utils.gameState == Utils.GameState.PLAYING) {
                    player.handleKeyReleased(e);
                }
            }
        });
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        running = true;
        gameThread.start();
    }

    @Override
    public void run() {
        long targetTime = 1000000000L / Utils.TARGET_FPS;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - lastTime >= targetTime) {
                update();
                repaint();
                lastTime += targetTime;
            }
        }
    }

    private void update() {
        if (Utils.gameState == Utils.GameState.PLAYING) {
            player.update(levelManager);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;

        // BƯỚC A: VẼ BACKGROUND ĐẦU TIÊN (Cho cả MENU lẫn PLAYING)
        if (bgImage != null) {
            // Co giãn ảnh phủ kín màn hình Game
            gd.drawImage(bgImage, 0, 0, Utils.WIDTH, Utils.HEIGHT, null);
        } else {
            // Dự phòng nếu lỗi load ảnh
            gd.setColor(Color.BLACK);
            gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);
        }

        // BƯỚC B: VẼ GIAO DIỆN THEO TRẠNG THÁI
        if (Utils.gameState == Utils.GameState.MENU) {
            // Tô lớp phủ đen mờ để chữ Menu dễ nhìn hơn trên nền ảnh
            gd.setColor(new Color(0, 0, 0, 150));
            gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);

            gd.setColor(Color.WHITE);
            gd.setFont(new Font("Arial", Font.BOLD, 24));
            gd.drawString("      GRAVITY DUCK", 180, 200);
            gd.setFont(new Font("Arial", Font.PLAIN, 16));
            gd.drawString("Bấm ENTER để bắt đầu chơi", 210, 260);
        }
        else if (Utils.gameState == Utils.GameState.PLAYING) {
            // BƯỚC C: Vẽ Map đè lên Background
            map.drawMap(gd, levelManager);

            // BƯỚC D: Vẽ Player đè lên trên cùng
            player.draw(gd);
        }

        gd.dispose();
    }
}
package com.gravityducknew;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean running = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(Utils.WIDTH, Utils.HEIGHT));
        this.setBackground(Color.BLACK); // Nền đen huyền bí
        this.setFocusable(true); // Nhận tín hiệu từ bàn phím

        // Sự kiện chuyển từ MENU vào game khi bấm ENTER
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (Utils.gameState == Utils.GameState.MENU && e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    Utils.gameState = Utils.GameState.PLAYING;
                }
            }
        });
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        running = true;
        gameThread.start(); // Kích hoạt luồng chạy hàm run() dưới đây
    }

    @Override
    public void run() {
        // Tính toán khoảng thời gian cần thiết cho mỗi khung hình (tính bằng nano giây)
        long targetTime = 1000000000L / Utils.TARGET_FPS;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - lastTime >= targetTime) {
                update();  // 1. Cập nhật logic (vị trí nhân vật, va chạm...)
                repaint(); // 2. Yêu cầu vẽ lại màn hình (gọi hàm paintComponent)
                lastTime += targetTime;
            }
        }
    }

    private void update() {
        // Giai đoạn sau sẽ cập nhật nhân vật tại đây
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (Utils.gameState == Utils.GameState.MENU) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("GRAVITY DUCK CLONE", 180, 200);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString("Bấm ENTER để bắt đầu chơi", 210, 260);
        } else if (Utils.gameState == Utils.GameState.PLAYING) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("Đã vào game! Thiết lập nhân vật ở bước tiếp theo...", 50, 50);
        }
        g2d.dispose();
    }
}

package com.gravityducknew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean running = false;

    // [SỬA ĐỔI 1]: Khởi tạo một đối tượng Player tại tọa độ (300, 100)
    private Player player = new Player(300, 100);

    public GamePanel() {
        this.setPreferredSize(new Dimension(Utils.WIDTH, Utils.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        // [SỬA ĐỔI 2]: Lắng nghe sự kiện bàn phím và chuyển tiếp cho Player
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Nếu đang ở Menu và bấm Enter -> Vào game
                if (Utils.gameState == Utils.GameState.MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Utils.gameState = Utils.GameState.PLAYING;
                }

                // Nếu đang chơi game -> Gửi phím bấm sang cho Player xử lý
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
        // [SỬA ĐỔI 3]: Cập nhật chuyển động của Player liên tục khi đang chơi
        if (Utils.gameState == Utils.GameState.PLAYING) {
            player.update();
        }
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
        }
        // [SỬA ĐỔI 4]: Vẽ Player lên màn hình khi đang chơi
        else if (Utils.gameState == Utils.GameState.PLAYING) {
            player.draw(g2d);
        }
        g2d.dispose();
    }
}
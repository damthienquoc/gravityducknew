package com.gravityducknew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean running = false;

    private Player player = new Player(300, 100);
    private Map map = new Map();
    private LevelManager levelManager = new LevelManager();

    public GamePanel() {
        this.setPreferredSize(new Dimension(Utils.WIDTH, Utils.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // bấm Enter -> Vào game
                if (Utils.gameState == Utils.GameState.MENU && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Utils.gameState = Utils.GameState.PLAYING;
                }

                //phím bấm sang cho Player xử lý
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

        if (Utils.gameState == Utils.GameState.MENU) {
            gd.setColor(Color.WHITE);
            gd.setFont(new Font("Arial", Font.BOLD, 24));
            gd.drawString("      GRAVITY DUCK", 180, 200);
            gd.setFont(new Font("Arial", Font.PLAIN, 16));
            gd.drawString("Bấm ENTER để bắt đầu chơi", 210, 260);
        }
        else if (Utils.gameState == Utils.GameState.PLAYING) {
            map.drawMap(gd,levelManager);
            player.draw(gd);
        }
        gd.dispose();
    }
}
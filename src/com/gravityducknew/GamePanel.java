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

    private BufferedImage bgImage;

    // Quản lý lựa chọn Menu chính (0: PLAY GAME, 1: SOUND, 2: EXIT)
    private int selectedOption = 0;

    // Quản lý lựa chọn Menu Pause (0: RESUME, 1: MAIN MENU)
    private int selectedPauseOption = 0;

    public GamePanel() {
        SoundManager.playBGM("/sound/BG.wav");
        this.setPreferredSize(new Dimension(Utils.WIDTH, Utils.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/image/BG.png"));
        } catch (Exception e) {
            System.out.println("Lỗi: Không tìm thấy ảnh nền BG.png");
            e.printStackTrace();
        }

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                // Phím M: Bật/Tắt âm thanh nhanh
                if (key == KeyEvent.VK_M) {
                    SoundManager.toggleMute();
                }

                // 1. ĐANG Ở MENU CHÍNH
                if (Utils.gameState == Utils.GameState.MENU) {
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                        selectedOption--;
                        if (selectedOption < 0) selectedOption = 2;
                    }
                    if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                        selectedOption++;
                        if (selectedOption > 2) selectedOption = 0;
                    }
                    if (key == KeyEvent.VK_ENTER) {
                        if (selectedOption == 0) {
                            Utils.gameState = Utils.GameState.PLAYING;
                        } else if (selectedOption == 1) {
                            SoundManager.toggleMute();
                        } else if (selectedOption == 2) {
                            System.exit(0);
                        }
                    }
                }
                // 2. ĐANG CHƠI GAME
                else if (Utils.gameState == Utils.GameState.PLAYING) {
                    // Bấm ESC hoặc P để Bật PAUSE
                    if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_P) {
                        Utils.gameState = Utils.GameState.PAUSE;
                        player.stopMovement();
                        selectedPauseOption = 0; // Reset con trỏ về RESUME
                        return;
                    }
                    player.handleKeyPressed(e);
                }
                // 3. ĐANG Ở TRẠNG THÁI PAUSE
                else if (Utils.gameState == Utils.GameState.PAUSE) {
                    // Bấm ESC hoặc P để Hủy PAUSE (Tiếp tục chơi)
                    if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_P) {
                        Utils.gameState = Utils.GameState.PLAYING;
                        return;
                    }

                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                        selectedPauseOption--;
                        if (selectedPauseOption < 0) selectedPauseOption = 1;
                    }
                    if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                        selectedPauseOption++;
                        if (selectedPauseOption > 1) selectedPauseOption = 0;
                    }
                    if (key == KeyEvent.VK_ENTER) {
                        if (selectedPauseOption == 0) {
                            Utils.gameState = Utils.GameState.PLAYING; // Tiếp tục chơi
                        } else if (selectedPauseOption == 1) {
                            player.reset(); // Reset vị trí Player
                            Utils.gameState = Utils.GameState.MENU; // Trở về Menu chính
                        }
                    }
                }
                else if (Utils.gameState == Utils.GameState.WIN) {
                    if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                        levelManager.resetToFirstLevel();
                        player.reset();
                        Utils.gameState = Utils.GameState.MENU;
                    }
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
        // Chỉ cập nhật nhân vật khi đang chơi (không chạy khi Pause)
        if (Utils.gameState == Utils.GameState.PLAYING) {
            player.update(levelManager);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;

        // Bật làm mượt nét chữ và hình vẽ
        gd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // VẼ BACKGROUND
        if (bgImage != null) {
            gd.drawImage(bgImage, 0, 0, Utils.WIDTH, Utils.HEIGHT, null);
        } else {
            gd.setColor(Color.BLACK);
            gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);
        }

        // VẼ THEO TRẠNG THÁI
        if (Utils.gameState == Utils.GameState.MENU) {
            drawMenu(gd);
        } else if (Utils.gameState == Utils.GameState.PLAYING) {
            map.drawMap(gd, levelManager);
            player.draw(gd);
            drawAudioHUD(gd);
        } else if (Utils.gameState == Utils.GameState.PAUSE) {
            // Giữ nguyên khung cảnh game ở dưới, đè menu Pause lên trên
            map.drawMap(gd, levelManager);
            player.draw(gd);
            drawAudioHUD(gd);
            drawPauseMenu(gd);
        }
        else if (Utils.gameState == Utils.GameState.WIN) { // THÊM MÀN HÌNH WIN
            map.drawMap(gd, levelManager);
            player.draw(gd);
            drawWinScreen(gd);
        }


        gd.dispose();
    }

    // --- HÀM VẼ MENU CHÍNH ---
    private void drawMenu(Graphics2D gd) {
        gd.setColor(new Color(0, 0, 0, 180));
        gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);

        gd.setFont(new Font("Arial", Font.BOLD, 36));
        gd.setColor(Color.YELLOW);
        drawCenteredString(gd, "GRAVITY DUCK", Utils.HEIGHT / 5);

        String soundStatus = SoundManager.isMuted() ? "SOUND: OFF" : "SOUND: ON";
        String[] menuOptions = {"PLAY GAME", soundStatus, "EXIT"};

        drawButtonList(gd, menuOptions, selectedOption, Utils.HEIGHT / 2 - 40);

        gd.setFont(new Font("Arial", Font.ITALIC, 13));
        gd.setColor(Color.LIGHT_GRAY);
        drawCenteredString(gd, "Dùng W/S hoặc Mũi tên để di chuyển, ENTER để chọn", Utils.HEIGHT - 55);
        drawCenteredString(gd, "Phím M: Bật/Tắt nhanh âm thanh", Utils.HEIGHT - 30);
    }

    // --- HÀM VẼ PAUSE MENU ---
    private void drawPauseMenu(Graphics2D gd) {
        // Lớp phủ đen mờ đè lên game
        gd.setColor(new Color(0, 0, 0, 190));
        gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);

        gd.setFont(new Font("Arial", Font.BOLD, 36));
        gd.setColor(Color.ORANGE);
        drawCenteredString(gd, "GAME PAUSED", Utils.HEIGHT / 4);

        String[] pauseOptions = {"RESUME", "MAIN MENU"};
        drawButtonList(gd, pauseOptions, selectedPauseOption, Utils.HEIGHT / 2 - 20);

        gd.setFont(new Font("Arial", Font.ITALIC, 13));
        gd.setColor(Color.LIGHT_GRAY);
        drawCenteredString(gd, "Nhấn ESC hoặc P để tiếp tục chơi", Utils.HEIGHT - 50);
    }

    // --- HÀM DÙNG CHUNG ĐỂ VẼ NÚT BẤM ---
    private void drawButtonList(Graphics2D gd, String[] options, int selectedIdx, int startY) {
        int btnWidth = 220;
        int btnHeight = 45;
        int btnX = (Utils.WIDTH - btnWidth) / 2;
        int spacing = 60;

        gd.setFont(new Font("Arial", Font.BOLD, 18));

        for (int i = 0; i < options.length; i++) {
            int btnY = startY + (i * spacing);

            if (i == selectedIdx) {
                gd.setColor(new Color(255, 215, 0, 220));
                gd.fillRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

                gd.setColor(Color.WHITE);
                gd.setStroke(new BasicStroke(3));
                gd.drawRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

                gd.setColor(Color.BLACK);
            } else {
                gd.setColor(new Color(40, 40, 40, 200));
                gd.fillRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

                gd.setColor(Color.GRAY);
                gd.setStroke(new BasicStroke(1));
                gd.drawRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

                gd.setColor(Color.WHITE);
            }

            FontMetrics metrics = gd.getFontMetrics(gd.getFont());
            int textX = btnX + (btnWidth - metrics.stringWidth(options[i])) / 2;
            int textY = btnY + ((btnHeight - metrics.getHeight()) / 2) + metrics.getAscent();
            gd.drawString(options[i], textX, textY);
        }
    }

    private void drawWinScreen(Graphics2D gd) {
        // Lớp phủ tối mờ
        gd.setColor(new Color(0, 0, 0, 200));
        gd.fillRect(0, 0, Utils.WIDTH, Utils.HEIGHT);

        // Dòng chữ chúc mừng
        gd.setFont(new Font("Arial", Font.BOLD, 36));
        gd.setColor(Color.YELLOW);
        drawCenteredString(gd, "YOU WIN!", Utils.HEIGHT / 3);

        gd.setFont(new Font("Arial", Font.PLAIN, 18));
        gd.setColor(Color.WHITE);
        drawCenteredString(gd, "Chúc mừng bạn đã hoàn thành tất cả các màn chơi!", Utils.HEIGHT / 2);

        gd.setFont(new Font("Arial", Font.ITALIC, 14));
        gd.setColor(Color.LIGHT_GRAY);
        drawCenteredString(gd, "Nhấn ENTER để về Main Menu", Utils.HEIGHT - 80);
    }

    // Hiển thị trạng thái âm thanh ở góc khi đang
    private void drawAudioHUD(Graphics2D gd) {
        gd.setFont(new Font("Arial", Font.BOLD, 12));
        if (SoundManager.isMuted()) {
            gd.setColor(Color.RED);
            gd.drawString("🔊 SOUND: OFF (M)", Utils.WIDTH - 130, 25);
        } else {
            gd.setColor(Color.GREEN);
            gd.drawString("🔊 SOUND: ON (M)", Utils.WIDTH - 130, 25);
        }
    }

    // Hàm phụ trợ căn giữa dòng chữ theo chiều ngang màn hình
    private void drawCenteredString(Graphics2D gd, String text, int y) {
        FontMetrics metrics = gd.getFontMetrics(gd.getFont());
        int x = (Utils.WIDTH - metrics.stringWidth(text)) / 2;
        gd.drawString(text, x, y);
    }
}
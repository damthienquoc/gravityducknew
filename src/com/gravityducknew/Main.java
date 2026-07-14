package com.gravityducknew;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Gravity Duck");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Tự co giãn vừa khít màn hình 640x480 của GamePanel

        window.setLocationRelativeTo(null); // Đưa cửa sổ ra chính giữa màn hình
        window.setVisible(true);

        gamePanel.startGameThread(); // Bắt đầu vòng lặp
    }
}
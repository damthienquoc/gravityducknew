# 🦆 Gravity Duck Java

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Swing%20%2F%20AWT-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 📌 1. Mô tả bài toán & Phạm vi hệ thống

### 🎯 Mô tả bài toán
**Gravity Duck Java** là dự án game giải đố platformer 2D được phát triển dựa trên ý tưởng của tựa game nổi tiếng *Gravity Duck*. 
* Trong game, nhân vật chính là một chú vịt cần di chuyển qua các chướng ngại vật phức tạp để thu thập trứng vàng ở mỗi màn chơi.
* Bài toán cốt lõi đặt ra là xử lý **động học nhân vật trong không gian trọng lực đa hướng** (4 hướng: Sàn, Trần, Tường Trái, Tường Phải), biến đổi hệ trục tọa độ render (`AffineTransform`), kiểm tra va chạm Tile-based chính xác và tự động điều chỉnh phím di chuyển tương ứng với góc quay của nhân vật.

### 🌐 Phạm vi hệ thống
* **Loại ứng dụng:** Ứng dụng Desktop chạy offline (Single-player).
* **Quản lý Màn chơi:** Hệ thống màn chơi (Level) tăng dần độ khó, tự động chuyển màn khi hoàn thành mục tiêu.
* **Đồ họa & Âm thanh:** Xử lý đồ họa 2D dựa trên Spritesheet, tích hợp hệ thống âm thanh hiệu ứng (SFX) cho từng tương tác.

---

## 🛠️ 2. Công nghệ sử dụng, Môi trường chạy & Yêu cầu cài đặt

### Công nghệ sử dụng
* **Ngôn ngữ lập trình:** Java (JDK 8+)
* **Thư viện đồ họa:** Java Swing & AWT (`Graphics2D`, `AffineTransform`, `BufferedImage`)
* **Xử lý tài nguyên:** `javax.imageio.ImageIO`, `javax.sound.sampled.Clip`

### Môi trường chạy & Yêu cầu cài đặt
* **Java Development Kit (JDK):** Version 8 trở lên.
* **Hệ điều hành:** Windows, macOS, hoặc Linux.
* **Công cụ biên dịch/IDE đề xuất:** IntelliJ IDEA, Eclipse, NetBeans hoặc Command Line (Terminal/CMD).

---

## 📂 3. Cấu trúc thư mục & Module chính

```text
com.gravityducknew/
│
├── Player.java            # Module Nhân vật: Xử lý vị trí, vận tốc, trọng lực,
│                          # xoay ma trận Render, lật Sprite và va chạm Tile.
│
├── LevelManager.java      # Module Màn chơi: Quản lý ma trận bản đồ, nạp dữ liệu
│                          # các level và xử lý logic chuyển level/thắng game.
│
├── SoundManager.java      # Module Âm thanh: Nạp và phát các tệp hiệu ứng SFX (.wav).
│
├── Utils.java             # Module Tiện ích: Lưu trữ hằng số (Tile size, IDs),
│                          # Enum trạng thái GameState & các hướng Gravity.
│
├── Main.java              # Module Khởi chạy: Khởi tạo Frame, Game Loop và lắng nghe phím.
│
└── resources/             # Thư mục Tài nguyên (Resources)
    ├── image/             # Hình ảnh Spritesheet (Player.png, Map tiles)
    └── sound/             # Hiệu ứng âm thanh (.wav: Rotate, Space, Dead, Egg)

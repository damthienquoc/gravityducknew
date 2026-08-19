#  Gravity Duck Java

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Swing%20%2F%20AWT-blue?style=for-the-badge)

---

##  1. Mô tả bài toán & Phạm vi hệ thống

###  Mô tả bài toán & Mục tiêu của dự án
**Gravity Duck Java** là dự án game giải đố platformer 2D được phát triển dựa trên ý tưởng của tựa game nổi tiếng *Gravity Duck*.

* **Mục tiêu chính:**
  * Giúp nhân vật chính (chú vịt) vượt qua các địa hình và chướng ngại vật phức tạp để thu thập quả trứng vàng ở mỗi màn chơi.
  * Nghiên cứu và áp dụng mô hình toán học biến đổi không gian đồ họa 2D trong lập trình game thuần Java.
* **Bài toán kỹ thuật cốt lõi:** 
  * Xử lý **động học nhân vật trong không gian trọng lực 4 hướng** (Sàn, Trần, Tường Trái, Tường Phải).
  * Biến đổi hệ trục tọa độ render bằng `AffineTransform` theo thời gian thực.
  * Xử lý va chạm chuẩn xác theo lưới bản đồ (Tile-based Collision Detection).
  * Tự động điều khiển phím di chuyển tương ứng theo góc quay của trọng lực.

###  Phạm vi hệ thống
* **Loại ứng dụng:** Ứng dụng Desktop chạy offline (Single-player).
* **Quản lý Màn chơi:** Hệ thống màn chơi (Level) tăng dần độ khó, tự động chuyển màn khi hoàn thành mục tiêu.
* **Đồ họa & Âm thanh:** Xử lý đồ họa 2D dựa trên Spritesheet, tích hợp hệ thống âm thanh hiệu ứng (SFX) cho từng tương tác.

---

##  2. Công nghệ sử dụng, Môi trường chạy & Yêu cầu cài đặt

### Công nghệ sử dụng
* **Ngôn ngữ lập trình:** Java (JDK 8+)
* **Thư viện đồ họa:** Java Swing & AWT (`Graphics2D`, `AffineTransform`, `BufferedImage`)
* **Xử lý tài nguyên:** `javax.imageio.ImageIO`, `javax.sound.sampled.Clip`

### Môi trường chạy & Yêu cầu cài đặt
* **Java Development Kit (JDK):** Version 8 trở lên.
* **Hệ điều hành:** Windows, macOS, hoặc Linux.
* **Công cụ biên dịch/IDE đề xuất:** IntelliJ IDEA, Eclipse, NetBeans hoặc Command Line (Terminal/CMD).

---

##  3. Cấu trúc thư mục & Module chính

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

---

##  4. Hướng dẫn cách chạy chương trình

Bạn có thể chạy dự án trực tiếp trên các IDE (IntelliJ IDEA, Eclipse) hoặc sử dụng dòng lệnh (Terminal/CMD):

### Cách 1: Chạy trực tiếp từ IDE (Khuyên dùng)
1. Mở dự án trong IntelliJ IDEA / Eclipse.
2. Đảm bảo cấu hình JDK 8 trở lên cho Project SDK.
3. Tìm đến file `src/com/gravityducknew/Main.java`.
4. Nhấp chuột phải và chọn Run 'Main.main()'.

### Cách 2: Chạy bằng Dòng lệnh (CLI)
1. Mở Terminal/CMD tại thư mục gốc của dự án.
2. Biên dịch mã nguồn Java:
   `javac -d bin -sourcepath src src/com/gravityducknew/*.java`
3. Sao chép tài nguyên vào thư mục đầu ra `bin`:
   * Trên Windows (Command Prompt): `xcopy /E /I /Y src\resources bin\resources`
   * Trên Linux / macOS: `cp -r src/resources bin/`
4. Khởi chạy game:
   `java -cp bin com.gravityducknew.Main`

---

##  5. Hướng dẫn điều khiển

| Trạng thái Trọng lực | Phím di chuyển | Hành động tương ứng |
| :--- | :--- | :--- |
| **DOWN** (Chân bám Sàn) | `A` / `D` hoặc `←` / `→` | Sang Trái / Sang Phải |
| **UP** (Chân bám Trần) | `A` / `D` hoặc `←` / `→` | Sang Trái / Sang Phải |
| **LEFT** (Chân bám Tường Trái) | `W` / `S` hoặc `↑` / `↓` | Leo Lên / Bò Xuống |
| **RIGHT** (Chân bám Tường Phải) | `W` / `S` hoặc `↑` / `↓` | Leo Lên / Bò Xuống |
| **Bất kỳ (DOWN / UP / LEFT / RIGHT)** | `SPACE` | Lật ngược trọng lực 180° (Chỉ kích hoạt khi bám tường/sàn) |

---

##  6. Các chức năng chính đã hoàn thành

*  **Cơ chế Đổi Trọng lực 4 Hướng:**
  * Hỗ trợ 4 trạng thái trọng lực: Sàn (`DOWN`), Trần (`UP`), Tường Trái (`LEFT`), Tường Phải (`RIGHT`).
  * Phím `SPACE`: Đảo ngược trọng lực 180° tức thì khi nhân vật đang đứng bám trên bề mặt.
*  **Hệ thống Điều khiển Thông minh (Context-Aware Controls):**
  * Tự động thay đổi phím điều khiển theo trọng lực: Dùng `A/D` khi đứng trên Sàn/Trần và chuyển sang `W/S` khi leo trên Tường Trái/Phải.
*  **Xử lý Ma trận Đồ họa & Sprite (Graphics & Render):**
  * Xoay ảnh nhân vật theo góc trọng lực chuẩn xác bằng `AffineTransform`.
  * Xử lý lật mặt ảnh (`Flip Sprite`) đúng theo hướng di chuyển thực tế trên màn hình.
  * Cắt ảnh hoạt họa (Animation) mượt mà từ Spritesheet.
*  **Tương tác Ô Đặc Biệt (Tile Collision & Events):**
  * **Rotate Tile:** Tự động căn vào tâm và xoay góc trọng lực 90° theo chiều kim đồng hồ khi nhân vật đi vào ô.
  * **Trap Tile:** Bẫy chướng ngại vật ngắt màn chơi, phát âm thanh tử vong và tự động reset về điểm xuất phát.
  * **Egg Tile:** Ăn trứng vàng mục tiêu, hiển thị sprite trứng vỡ và tự động chuyển level sau khoảng delay ngắn.
*  **Hệ thống Âm thanh (SFX):**
  * Tích hợp đầy đủ tiếng động tương tác: Lật trọng lực (`Space.wav`), Xoay ô (`Rotate.wav`), Ăn trứng (`egg.wav`), Chết/Reset (`Dead.wav`).
*  **Quản lý Màn chơi & Trạng thái Game:**
  * Tự động chuyển màn khi hoàn thành mục tiêu.
  * Hiển thị màn hình Thắng cuộc (`WIN State`) khi vượt qua màn chơi cuối cùng.

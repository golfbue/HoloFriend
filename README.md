# Holo-Fan

แอปพลิเคชันสำหรับแฟนคลับ Hololive ชาวไทย เพื่อให้สามารถติดตามข่าวสารและตารางไลฟ์ของไอดอลคนโปรดได้ง่ายขึ้นในที่เดียว!

![App Mockup](docs/mockup.png)

## เกี่ยวกับโปรเจกต์ (Overview)
**Holo-Fan** ถูกสร้างขึ้นจากความต้องการที่จะช่วยเหลือแฟนคลับชาวไทยที่อาจจะมีข้อจำกัดด้านภาษา (ญี่ปุ่น/อังกฤษ) ให้สามารถเข้าถึงข้อมูลและตารางเวลาของเมมเบอร์ Hololive ได้อย่างสะดวกและรวดเร็ว

## คุณสมบัติหลัก (Key Features)
- **ตารางไลฟ์ (Live Schedule)**: ติดตามตารางการไลฟ์สดแบบ Real-time โดยดึงข้อมูลจาก Holodex API
- **การแจ้งเตือน (Notifications)**: แจ้งเตือนทันทีเมื่อเมมเบอร์ที่ติดตามเริ่มไลฟ์ เพื่อไม่ให้คุณพลาดทุกโมเมนต์สำคัญ
- **ข้อมูลเมมเบอร์ (Talent Profiles)**: รวบรวมข้อมูลรายละเอียดของเมมเบอร์แต่ละคน (อ้างอิงจากเว็บไซต์หลัก)
- **ร้านค้าสินค้า (Merch Store)**: เข้าถึงหน้าจำหน่ายสินค้า Official ของ Hololive ได้โดยตรง
- **โหมดมืด (Dark Mode)**: รองรับการปรับเปลี่ยนธีมตามความชอบ
- **รองรับสองภาษา**: สามารถเลือกใช้งานได้ทั้งภาษาไทยและภาษาอังกฤษ

## เทคโนโลยีที่ใช้ (Tech Stack)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern Android UI)
- **Networking**: Retrofit & Gson (สำหรับดึงข้อมูลจาก API)
- **Image Loading**: Coil (สำหรับแสดงรูปภาพเมมเบอร์)
- **Background Task**: WorkManager (สำหรับการตรวจสอบสถานะไลฟ์และแจ้งเตือนในเบื้องหลัง)
- **API**: Holodex API

## เริ่มต้นใช้งาน (Getting Started)

### สิ่งที่ต้องเตรียม (Prerequisites)
- Android Studio Jellyfish (หรือเวอร์ชันใหม่กว่า)
- JDK 11
- Holodex API Key ([ขอได้ที่นี่](https://holodex.net/))

### การติดตั้ง (Installation)
1. Clone โปรเจกต์นี้ลงในเครื่องของคุณ:
   ```bash
   git clone https://github.com/golfbue/Holo-Fan.git
   ```
2. เปิดโปรเจกต์ด้วย Android Studio
3. สร้างไฟล์ `local.properties` ในโฟลเดอร์ root (ถ้ายังไม่มี) และเพิ่ม API Key ของคุณ:
   ```properties
   HOLODEX_API_KEY=your_api_key_here
   ```
4. กด **Sync Project with Gradle Files**
5. กด **Run** เพื่อเริ่มใช้งานบน Emulator หรืออุปกรณ์จริง

## ธีมและการออกแบบ (Design System)
ตัวแอปออกแบบโดยใช้โทนสี **Blue-White** ซึ่งเป็นสีเอกลักษณ์ของ Hololive ให้ความรู้สึกสะอาดตา ทันสมัย และพรีเมียม

## การมีส่วนร่วม (Contribution)
หากคุณมีไอเดียในการพัฒนาหรือพบข้อผิดพลาด สามารถเปิด **Issue** หรือส่ง **Pull Request** เข้ามาได้เลยครับ!

---
*Created with care for Hololive Fans.*

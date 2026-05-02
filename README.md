# HoloFriend 

**HoloFriend** คือแอปพลิเคชันสำหรับแฟนคลับ Hololive ที่สร้างขึ้นด้วย Jetpack Compose โดยเน้นความสวยงาม ทันสมัย และ **"ความถูกต้องของเนื้อหา"** เป็นสำคัญ เพื่อให้คุณไม่พลาดทุกความเคลื่อนไหวจากสมาชิก Hololive ตัวจริง

##  ฟีเจอร์หลัก (Key Features)

- **Official Content Only**: ระบบคัดกรองอัจฉริยะที่คัดเลือกเฉพาะเนื้อหาจากสมาชิก Hololive และ DEV_IS (ReGLOSS, FLOW GLOW) ตัวจริงเท่านั้น โดยทำการกรอง Clipper และช่องแฟนคลับออก 100%
- **Live & Archive Streams**: ติดตามไลฟ์ที่กำลังฉายอยู่และไลฟ์ย้อนหลังได้ทันทีจากหน้า Home
- **Complete Schedule**: ตารางการไลฟ์ล่วงหน้าแบบละเอียด แยกสัดส่วนชัดเจนระหว่างไลฟ์สดและไลฟ์ที่จบแล้ว
- **Talent Directory**: รายชื่อเมมเบอร์ทุกคนพร้อมระบบคัดกรองตามรุ่น (Generation) ที่แม่นยำ พร้อมป้ายระบุรุ่น (Gen Tags) บนการ์ดสมาชิก
- **Official Shop Integration**: เข้าถึงร้านค้าทางการ (Hololive Official Shop) ได้อย่างรวดเร็วผ่านหน้า Merch Store
- **Premium Design**: อินเตอร์เฟซแบบ Dark Mode ที่ดูพรีเมียม พร้อมไอคอนแบบ Vector ที่คมชัดในทุกความละเอียด

##  เทคโนโลยีที่ใช้ (Tech Stack)

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Network**: Retrofit & OkHttp
- **Image Loading**: Coil
- **Data Source**: Holodex API (v2)
- **Architecture**: MVVM (Model-View-ViewModel)

##  การออกแบบ (Design Identity)

แอปพลิเคชันเลือกใช้โทนสี **Primary Blue (#2196F3)** ซึ่งเป็นสีเอกลักษณ์ของ Hololive ผสมผสานกับดีไซน์แบบ Glassmorphism และการใช้ขอบมน (Rounded Corners) เพื่อให้ความรู้สึกที่เป็นมิตรและทันสมัย

---

###  เอกสารโครงการ (Documentation)
- [Project Report & Wireframes](docs/Project_Report.md) - รายละเอียดโครงสร้างแอป, Wireframes และ UI Mockup

---

##  เริ่มต้นใช้งาน (Getting Started)

1. Clone โปรเจกต์นี้ลงในเครื่อง
2. ตรวจสอบให้มั่นใจว่ามี `HOLODEX_API_KEY` ใน `BuildConfig`
3. Build และรันแอปผ่าน Android Studio

---
*Created with  by Antigravity AI & The Hololive Fan Community*

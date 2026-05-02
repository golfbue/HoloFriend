# 📄 Project Report & Wireframes: HoloFriend 💙

รายงานสรุปโครงสร้างและระบบการทำงานของแอปพลิเคชัน **HoloFriend** เพื่อใช้เป็นแนวทางในการพัฒนาและออกแบบประสบการณ์ผู้ใช้ (UX/UI)

---

## 1. Project Overview (ภาพรวมโครงการ)

**HoloFriend** คือแอปพลิเคชัน Android ที่ออกแบบมาเพื่อเป็นศูนย์กลางสำหรับแฟนคลับ Hololive โดยเฉพาะ โดยเน้นที่ความรวดเร็ว แม่นยำ และดีไซน์ที่พรีเมียม

### Key Objectives:
- **Accuracy**: กรองเฉพาะคอนเทนต์ทางการ (Official) 100%
- **Engagement**: แสดงตารางไลฟ์ที่กำลังจะมาถึงแบบ Real-time
- **Accessibility**: เข้าถึงร้านค้าและการระบุรุ่น (Generations) ของเมมเบอร์ได้ง่าย

---

## 2. Wireframe Structures (โครงร่าง UI)

เราใช้ระบบนำทางแบบ **Bottom Navigation** เพื่อให้ผู้ใช้สลับหน้าหลักได้สะดวกที่สุด

### A. Home Screen (หน้าหลัก)
เน้นการแสดงผลไลฟ์ที่กำลังเกิดขึ้น (Live Now) และวิดีโอล่าสุด

```mermaid
graph TD
    A[Header: HoloFriend Logo & Search] --> B[Section: Live Now - Horizontal Scroll]
    B --> C[Section: Recent Archives - Vertical Feed]
    C --> D[Card: Video Thumbnail]
    D --> E[Video Title & Talent Name]
    E --> F[Bottom Nav: Home, Schedule, Talent, Merch]
```

### B. Schedule Screen (ตารางไลฟ์)
ตารางเวลาที่ชัดเจน แบ่งตามช่วงเวลา (Morning, Afternoon, Evening)

```mermaid
graph LR
    A[Date Selector] --> B[Timeline View]
    B --> C{Upcoming Streams}
    C --> D[Card: Time + Talent Icon + Title]
    D --> E[Status: Wait / Starting]
```

---

## 3. User Flow (ลำดับการใช้งาน)

```mermaid
sequenceDiagram
    participant User
    participant App
    participant HolodexAPI

    User->>App: เปิดแอป (Splash Screen)
    App->>HolodexAPI: ดึงข้อมูล Live & Schedule
    HolodexAPI-->>App: ข้อมูล JSON
    App->>User: แสดงหน้า Home (Live Now)
    User->>App: กดที่ Schedule Tab
    App->>User: แสดงตารางไลฟ์ล่วงหน้า
    User->>App: กดที่ Talent Card
    App->>User: แสดงรายละเอียดเมมเบอร์ & ลิงก์ YouTube
```

---

## 4. UI Mockup & Wireframe Concept

นี่คือภาพร่าง **Wireframe** พื้นฐานและภาพจำลองหน้าจอหลัก (**Home Screen**)

### Basic Wireframe
![HoloFriend Wireframe](images/wireframe.png)

### Visual Mockup
![HoloFriend Home Screen Mockup](images/mockup_home.png)

---

## 5. Design Guidelines (หลักการออกแบบ)

- **Typography**: ใช้ฟอนต์ 'Inter' หรือ 'Roboto'
- **Colors**:
    - Primary: `#2196F3` (Hololive Blue)
    - Background: `#0F172A` (Dark Navy)
- **Components**: ใช้ขอบมน (Rounded Corners 16dp) และ Glassmorphism

---
*จัดทำโดย Antigravity AI*

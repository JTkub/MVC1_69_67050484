# SUBMISSION - Exit Exam MVC 1/2569 (เสาร์บ่าย)

## 1. วิธีเปิดโปรแกรม
- ภาษา/เฟรมเวิร์ก: Java แบบ Console
- Entry point / คำสั่งเปิดโปรแกรม: Main.java / javac *.java แล้วรัน java Main.java
- หมายเหตุที่จำเป็น (ถ้ามี): 

## 2. ตารางเชื่อมโยง Requirements

| Requirement | Model / Domain | Controller / Action | View / Screen |\
| R1 | Member, RoleChangeRequest, Vote, DataStore, RequestService | RequestController | ConsoleView |\
| R2 | Member, RoleChangeRequest, Role, DataStore | RequestController.showMembers(), createRequest() | ConsoleView |\
| R3 | Member, RoleChangeRequest, Vote, VoteChoice | RequestController.voteOnRequest() | ConsoleView |\
| R4 | RoleChangeRequest, Vote, RequestStatus, Member | RequestController.voteOnRequest() | ConsoleView |\
| R5 | RoleChangeRequest, Vote, RequestStatus | RequestController.cancelRequest(), showSummary() | ConsoleView |\

## 3. ผลการทดสอบ

| กรณี | ผ่าน/ไม่ผ่าน | หมายเหตุ (เฉพาะที่จำเป็น) |\
| T1 | ผ่าน | สร้าง C05 ของ M01 เป็น EDITOR สำเร็จ และสถานะเป็น PENDING |\
| T2 | ผ่าน | ปฏิเสธการสร้างคำขอให้ M01 เพราะมี C05 สถานะ PENDING อยู่แล้ว |\
| T3 | ผ่าน | M04 เป็น APPROVE ลำดับที่ 2 ของ C01, C01 เป็น APPROVED และ M02 เปลี่ยนเป็น EDITOR |\
| T4 | ผ่าน | M05 เป็น REJECT ลำดับที่ 2 ของ C02, C02 เป็น REJECTED และ M03 ยังคง EDITOR |\
| T5 | ผ่าน | M03 ยกเลิก C03 สำเร็จ โดย C03 เป็น CANCELLED |\
| T6 | ผ่าน | ปฏิเสธ M05 เพราะเป็นสมาชิกเป้าหมายของ C04 |\

## 4. ความแตกต่างระหว่างแบบที่ออกกับโปรแกรมจริง (ถ้ามี)\
ระบุไม่เกิน 3 ข้อ
1. ไม่มีความแตกต่างที่มีสาระสำคัญจากแบบที่ออก\

## 5. บันทึกการใช้ Generative AI\
หากไม่ได้ใช้ ให้ระบุ **ไม่ได้ใช้ Generative AI**\

| เวลาโดยประมาณ | เครื่องมือ | ใช้เพื่ออะไร | นำคำแนะนำไปใช้อย่างไร |\
| 13:56 | ChatGPT | ดูว่า Controller ใน Class Diagram จำเป็นต้องมี Relations ต่อกับพวก Model, View ไหม | ไม่ได้เพิ่ม Relation กับตัวที่ต่อกับController |\
| 14:37 | ChatGPT | ถามว่า Sequence Diagram มีคนเป็น M นำหน้า คนที่เป็น Actor สามารถเป็น Participant ได้ไหม | นำ M02 ที่เป็นคนมาทำเป็น Participant |\
| 15:22 | ChatGPT | หาวิธีดึงไฟล์ json มาใช้ในโค้ด | ใช้ java.nio.file.Files เพื่อใช้ method readString ในการดึงข้อมูลจาก json |\
| 15:50 | ChatGPT | หาวิธีแก้ NullPointerException ของ equals | ใช้ equalsIgnoreCase แทนเพื่อ Ignore |\

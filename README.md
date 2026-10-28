<div dir="rtl">

# 📨 Inquiry Manager – מערכת לניהול פניות

מערכת **שרת–לקוח (Client–Server)** בשפת **Java** לניהול פניות של לקוחות: שאלות, בקשות ותלונות.
המערכת מאפשרת ללקוחות לפתוח פניות, לעקוב אחר הסטטוס שלהן ולבטל אותן, ומאפשרת לנהל נציגי שירות שמטפלים בפניות באופן אוטומטי ובמקביל.

הפרויקט מדגים עבודה עם תכנות מונחה עצמים, תקשורת ברשת באמצעות Sockets, ריבוי תהליכונים (Multithreading), שמירת נתונים בקבצים ו־Reflection.

---

## 📑 תוכן עניינים

- [סקירה כללית](#-סקירה-כללית)
- [יכולות המערכת](#-יכולות-המערכת)
- [ארכיטקטורה](#-ארכיטקטורה)
- [מבנה הפרויקט](#-מבנה-הפרויקט)
- [מודל הנתונים](#-מודל-הנתונים)
- [פרוטוקול התקשורת](#-פרוטוקול-התקשורת)
- [טיפול בפניות וריבוי תהליכונים](#-טיפול-בפניות-וריבוי-תהליכונים)
- [שמירת נתונים](#-שמירת-נתונים)
- [התקנה והרצה](#-התקנה-והרצה)
- [שימוש במערכת](#-שימוש-במערכת)
- [טכנולוגיות ונושאים מרכזיים](#-טכנולוגיות-ונושאים-מרכזיים)

---

## 🔎 סקירה כללית

המערכת בנויה משני חלקים עצמאיים:

| רכיב | תיקייה | תפקיד |
|------|--------|-------|
| **שרת** | `IntelliJTraining_inquirymanager_chanadavidov` | מאזין לחיבורים בפורט `8888`, מנהל את תור הפניות והנציגים, שומר נתונים לקבצים ומשבץ נציגים לפניות. |
| **לקוח** | `ClientSide` | אפליקציית קונסול שמתחברת לשרת, מציגה תפריט פעולות ושולחת בקשות לשרת. |

הלקוח והשרת משתפים את אותן מחלקות מודל (חבילת `Shared`), כך שהאובייקטים עוברים ביניהם ברשת באמצעות **Java Serialization**.

---

## ✨ יכולות המערכת

**ניהול פניות**
- יצירת פנייה חדשה מאחד משלושת הסוגים: שאלה, בקשה או תלונה.
- הצגת כל הפניות הפתוחות במערכת.
- בדיקת סטטוס של פנייה לפי קוד הפנייה.
- ביטול פנייה פתוחה.
- סטטיסטיקה: ספירת פניות שנפתחו בחודש מסוים.

**ניהול נציגים**
- הוספת נציג חדש למערכת (נשמר לקובץ).
- הסרת נציג מהמערכת.
- התחברות נציג (Login) – הנציג הופך לזמין לטיפול בפניות.
- התנתקות נציג (Logout) – הנציג מוסר מרשימת הנציגים הזמינים.

**טיפול אוטומטי**
- תהליכון רקע שמשבץ באופן אוטומטי נציג פנוי לפנייה הבאה בתור.
- כל טיפול בפנייה רץ בתהליכון נפרד, כך שכמה פניות מטופלות במקביל.
- פנייה שטופלה או בוטלה מועברת להיסטוריה.

---

## 🏗 ארכיטקטורה

<div dir="ltr">

```
┌─────────────────────┐        RequestObj         ┌──────────────────────────────┐
│   Client (Console)  │ ────────────────────────▶ │  InquiryManagerServer :8888  │
│ InquiryManagerClient│ ◀──────────────────────── │   └─ ClientHandler (Thread)  │
└─────────────────────┘        ResponseObj        │        └─ ServerService      │
                                                   │             └─ InquiryManager│
                                                   │                  ├─ Queues   │
                                                   │                  └─ HandleFiles
                                                   └──────────────────────────────┘
                                                                  │
                                                                  ▼
                                                     File System (txt / csv files)
```

</div>

**שכבות המערכת בצד השרת:**

1. **שכבת רשת** – `InquiryManagerServer` מקבל חיבורים, ולכל לקוח נפתח `ClientHandler` בתהליכון משלו.
2. **שכבת שירות** – `ServerService` מקבל `RequestObj`, מנתב לפי סוג הפעולה ומחזיר `ResponseObj`.
3. **שכבה עסקית** – `InquiryManager` מנהל את התורים, את שיבוץ הנציגים ואת הלוגיקה של הפניות.
4. **שכבת נתונים** – `HandleFiles` ו־`HandleFilesReflection` אחראיות על שמירה וטעינה מקבצים.

---

## 📂 מבנה הפרויקט

<div dir="ltr">

```
project-inquirymanager/
├── ClientSide/                         # Client application
│   └── src/
│       ├── Main.java                   # Client entry point
│       ├── clientData/
│       │   └── InquiryManagerClient.java   # Console menu + socket communication
│       ├── Shared/                     # Model classes shared with the server
│       ├── business/
│       └── HandleStoreFiles/
│
└── IntelliJTraining_inquirymanager_chanadavidov/   # Server application
    ├── nextVal.txt                     # Next inquiry code (persisted counter)
    └── src/
        ├── Main.java                   # Server entry point
        ├── Server/
        │   ├── InquiryManagerServer.java   # ServerSocket on port 8888
        │   ├── ClientHandler.java          # One thread per connected client
        │   └── ServerService.java          # Routes each request to its action
        ├── business/
        │   ├── InquiryManager.java         # Core logic: queues, agents, statistics
        │   ├── InquiryHandling.java        # Thread that handles a single inquiry
        │   └── InquiryHandlingTask.java    # Runnable pairing an agent with an inquiry
        ├── Shared/
        │   ├── Inquiry.java                # Abstract base class
        │   ├── Question.java
        │   ├── Request.java
        │   ├── Complaint.java
        │   ├── Representative.java         # Service agent
        │   ├── RequestObj.java             # Client → Server message
        │   └── ResponseObj.java            # Server → Client message
        ├── HandleStoreFiles/
        │   ├── IForSaving.java             # Interface for savable objects
        │   ├── HandleFiles.java            # Save / read / delete text files
        │   └── HandleFilesReflection.java  # Generic CSV save/load via Reflection
        └── testSaveFile/                   # Manual tests for the file layer
```

</div>

---

## 🧩 מודל הנתונים

### פנייה – `Inquiry` (מחלקה אבסטרקטית)

| שדה | סוג | תיאור |
|-----|-----|-------|
| `code` | `Integer` | מזהה ייחודי, נוצר אוטומטית מ־`nextCodeVal` |
| `description` | `String` | תיאור הפנייה |
| `creationDate` | `LocalDateTime` | מועד יצירה |
| `status` | `Status` | סטטוס נוכחי |

**סוגי פניות (ירושה מ־`Inquiry`):**

| מחלקה | תיאור | שדות נוספים | זמן טיפול מדומה | עדיפות |
|-------|-------|-------------|------------------|--------|
| `Question` | שאלה | – | 5 שניות | **גבוהה** (Priority 10) |
| `Request` | בקשה | – | 3 שניות | רגילה |
| `Complaint` | תלונה | `assignedBranch` – הסניף המטפל | 5 שניות | רגילה |

**מחזור החיים של פנייה (`Status`):**

<div dir="ltr">

```
OPEN ──▶ IN_TREATMENT ──▶ TREATED   (moved to history)
  │
  └────────────────────▶ CANCELED   (moved to history)
```

</div>

### נציג – `Representative`

| שדה | תיאור |
|-----|-------|
| `code` | מספר רץ פנימי |
| `name` | שם הנציג |
| `id` | תעודת זהות / מזהה הנציג |
| `handledInquiriesIds` | רשימת קודי הפניות שהנציג טיפל בהן |

---

## 🔌 פרוטוקול התקשורת

הלקוח שולח אובייקט `RequestObj` הכולל **פעולה** ו**פרמטר**, והשרת מחזיר `ResponseObj` הכולל **קוד סטטוס**, **הודעה** ו**תוצאה**.

| פעולה (`Action`) | פרמטר שנשלח | תוצאה שמוחזרת |
|------------------|-------------|----------------|
| `ADD_INQUIRY` | אובייקט `Inquiry` | קוד הפנייה שנוצרה |
| `GET_ALL` | – | רשימת כל הפניות |
| `GET_INQUIRY_STATUS` | קוד פנייה (`int`) | הסטטוס של הפנייה |
| `GET_COUNT_BY_MONTH` | מספר חודש (`1–12`) | מספר הפניות באותו חודש |
| `CANCEL_INQUIRY` | קוד פנייה (`int`) | הצלחה / פנייה לא נמצאה |
| `AGENT_LOGIN` | אובייקט `Representative` | אישור התחברות |
| `AGENT_LOGOUT` | מזהה נציג (`int`) | אישור התנתקות |
| `ADD_AGENT` | אובייקט `Representative` | אישור הוספה |
| `REMOVE_AGENT` | מזהה נציג (`int`) | אישור הסרה |
| `TEST` | – | `SERVER_READY` – בדיקת זמינות |

**קודי סטטוס בתשובה:**

| קוד | משמעות |
|-----|--------|
| `200` | הפעולה הצליחה |
| `400` | בקשה לא תקינה או פעולה לא נתמכת |
| `404` | הפריט המבוקש לא נמצא |
| `500` | שגיאה פנימית בשרת |

---

## ⚙️ טיפול בפניות וריבוי תהליכונים

`InquiryManager` מנהל מספר תורים:

| תור | תוכן |
|-----|------|
| `QInquiry` | פניות פתוחות שממתינות לטיפול |
| `QRepresentative` | כל הנציגים הרשומים במערכת |
| `QActiveRepresentative` | נציגים מחוברים שזמינים לטיפול |

**תהליך השיבוץ:**

1. בעת עליית המערכת נטענים מהקבצים הפניות, הנציגים והמונה `nextVal`.
2. תהליכון רקע (`mergingInquiriesToAgent`) בודק כל הזמן אם יש גם פנייה ממתינה וגם נציג זמין.
3. כשיש התאמה – נוצר `InquiryHandlingTask` שמחבר בין הנציג לפנייה, ומופעל בתהליכון נפרד.
4. הטיפול עצמו (`InquiryHandling`) מדמה זמן עבודה לפי סוג הפנייה, ושאלות מקבלות עדיפות גבוהה.
5. בסיום, הפנייה מסומנת כ־`TREATED`, עוברת להיסטוריה, והנציג חוזר לתור.

בנוסף, השרת עצמו מטפל בכמה לקוחות במקביל – כל לקוח מקבל `ClientHandler` בתהליכון משלו.

---

## 💾 שמירת נתונים

המערכת לא משתמשת במסד נתונים. כל הנתונים נשמרים בקבצי טקסט בתיקיית ההרצה של השרת:

| מיקום | תוכן |
|-------|------|
| `Shared.Question/` · `Shared.Request/` · `Shared.Complaint/` | קובץ לכל פנייה פתוחה (`<code>.txt`) |
| `history/` | פניות שטופלו או בוטלו |
| `Representative/` | קובץ לכל נציג (`<id>.txt`) |
| `nextVal.txt` | הקוד הבא שיינתן לפנייה חדשה, כדי שהמספור יישמר בין הרצות |

**שני מנגנוני שמירה:**

- **`HandleFiles`** – עובד מול הממשק `IForSaving`. כל מחלקה מגדירה בעצמה את שם התיקייה, שם הקובץ, איך להפוך את עצמה לטקסט (`getData`) ואיך לשחזר את עצמה ממנו (`parseFromFile`).
- **`HandleFilesReflection`** – שמירה וטעינה גנריות בפורמט CSV באמצעות **Reflection**. שם המחלקה נשמר בתחילת הקובץ, ובטעינה נוצר אובייקט מהמחלקה המתאימה והשדות שלו מתמלאים אוטומטית – בלי לכתוב קוד ייעודי לכל מחלקה.

---

## 🚀 התקנה והרצה

### דרישות מקדימות

- **JDK 17** ומעלה (בקוד יש שימוש ב־switch expressions וב־pattern matching ל־`instanceof`)
- IDE מומלץ: **IntelliJ IDEA**

### שכפול הפרויקט

<div dir="ltr">

```bash
git clone https://github.com/chanadavidov732-ship-it/project-inquirymanager.git
cd project-inquirymanager
```

</div>

### 1. הרצת השרת

1. פתחו את התיקייה `IntelliJTraining_inquirymanager_chanadavidov` כפרויקט ב־IntelliJ.
2. הריצו את `Server/InquiryManagerServer.java` (או את `Main.java`, שיוצרת קודם שתי פניות לדוגמה דרך הקונסול ואז מפעילה את השרת).
3. כשהשרת עולה תופיע ההודעה:

<div dir="ltr">

```
Inquiry Manager Server started on port 8888
```

</div>

### 2. הרצת הלקוח

1. פתחו את התיקייה `ClientSide` כפרויקט נפרד.
2. ב־`ClientSide/src/Main.java` עדכנו את כתובת השרת. להרצה על אותו מחשב:

<div dir="ltr">

```java
InquiryManagerClient i = new InquiryManagerClient("localhost", 8888);
```

</div>

3. הריצו את `Main.java`. אם החיבור הצליח תופיע ההודעה `client success connect to server`.

> 💡 אפשר להריץ כמה לקוחות במקביל מול אותו שרת.

> ⚠️ יש להפעיל את השרת **לפני** הלקוח. תיקיות הנתונים נוצרות בתיקיית ההרצה של השרת.

---

## 🖥 שימוש במערכת

לאחר ההתחברות יוצג ללקוח התפריט הבא:

<div dir="ltr">

```
press:
1 - get all inquiries
2 - create new inquiry
3 - get inquiry`s status
4 - get monthly count
5 - cancel inquiry
6 - Agent Login
7 - Agent Logout
8 - Add New Agent
9 - Remove Agent
10 - exit
```

</div>

**דוגמה – יצירת תלונה:**

<div dir="ltr">

```
> 2
press:
1 - create complaint inquiry
2 - create question inquiry
3 - create request inquiry
> 1
enter description
> The order arrived damaged
enter assignedBranch
> Jerusalem
your request over successfully
the server massage is :
SUCCESS
5
```

</div>

המספר שמוחזר (`5` בדוגמה) הוא קוד הפנייה, שבעזרתו אפשר לבדוק את הסטטוס שלה (אפשרות 3) או לבטל אותה (אפשרות 5).

---

## 🛠 טכנולוגיות ונושאים מרכזיים

| נושא | מימוש בפרויקט |
|------|----------------|
| **OOP – ירושה ופולימורפיזם** | `Question`, `Request`, `Complaint` יורשות מ־`Inquiry` ומממשות את `handling()` כל אחת בדרכה |
| **ממשקים** | `IForSaving` מגדיר חוזה אחיד לכל אובייקט שניתן לשמור לקובץ |
| **תקשורת רשת** | `ServerSocket` / `Socket` עם `ObjectInputStream` / `ObjectOutputStream` |
| **Serialization** | מחלקות המודל וההודעות מממשות `Serializable` |
| **Multithreading** | תהליכון לכל לקוח, תהליכון שיבוץ ברקע, תהליכון לכל טיפול בפנייה, עדיפויות תהליכונים |
| **Concurrency** | שימוש ב־`ConcurrentLinkedQueue` ובתורים לניהול משימות |
| **Reflection** | שמירה וטעינה גנריות של אובייקטים, ויצירת מופע לפי שם המחלקה |
| **File I/O** | `FileWriter`, `Scanner`, `java.nio.file.Files` |
| **Streams API** | חישוב סטטיסטיקות חודשיות באמצעות `Stream.concat` ו־`filter` |

---

## 👩‍💻 מפתחת

**Chana Davidov**
GitHub: [chanadavidov732-ship-it](https://github.com/chanadavidov732-ship-it)

</div>

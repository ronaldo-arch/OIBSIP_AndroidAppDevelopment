# OIBSIP — Android Development — Task 2
## Secure To-Do App (with Login / Sign-up)

A multi-user Android app for managing personal **Tasks**, **Events**, and **Notes**, protected by a secure local authentication system.

---

### 🎯 Objective
Build an Android To-Do application that requires the user to sign up and log in before they can manage their data, with each user seeing only their own items, and with passwords stored **securely** (never in plaintext).

---

### 🛠 Tools Used
| Tool / Library | Purpose |
|---|---|
| **Android Studio** | IDE |
| **Java** | Application logic |
| **XML** | UI layouts |
| **SQLite** (via `SQLiteOpenHelper`) | Local persistent storage |
| **PBKDF2-HMAC-SHA256** (`javax.crypto.SecretKeyFactory`) | Password hashing |
| **SharedPreferences** | Secure session storage |
| **Material Components 1.12.0** | Material 3 UI |
| **AndroidX RecyclerView / TabLayout / FloatingActionButton** | List + tabs + FAB |
| AGP 8.5.0 / Gradle 8.7 / JDK 17 | Build toolchain |
| Min SDK **24** • Target SDK **34** | API compatibility |

---

### 🔐 Security Decisions
- Passwords are hashed with **PBKDF2-HMAC-SHA256**, **120,000 iterations**, **16-byte random per-user salt**, 256-bit derived key.
- Stored format: `iterations:saltBase64:hashBase64`.
- Verification uses a **constant-time byte comparison** to defeat timing attacks.
- Session token = **24 random bytes** kept in private `SharedPreferences`; cleared on logout.
- `android:allowBackup="false"` in the manifest prevents `adb backup` from exfiltrating the user DB.

---

### 🔧 Steps Performed
1. **Project setup** — Android Studio project (Empty Activity, Java), Material 3 DayNight theme, AndroidX.
2. **Database** — created `DbHelper` extending `SQLiteOpenHelper` with two tables: `users(id, email UNIQUE, password_hash)` and `items(id, user_id FK, section, title, body, created_at)`, plus an index on `(user_id, section)` for fast lookups.
3. **Password hashing** — wrote `PasswordHasher` utility around `SecretKeyFactory("PBKDF2WithHmacSHA256")` with a per-user salt and constant-time verify.
4. **Session management** — wrote `SessionManager` to persist `userId`, `email`, and a random session token in `SharedPreferences` (private mode).
5. **Login & Signup screens** — Material `TextInputLayout` fields, email regex validation, 6-char minimum password, confirm-password match check, friendly error toasts.
6. **Home screen** — `MaterialToolbar` shows the logged-in email; a `TabLayout` switches between three sections (Tasks / Events / Notes); items render in a `RecyclerView` of Material cards; an `ExtendedFloatingActionButton` opens the editor for a new item.
7. **Editor activity** — single screen for both *create* and *edit*; **Delete** appears only when editing an existing item.
8. **Per-user isolation** — every list / insert / update / delete query is scoped by `user_id`, so user A can never see user B's data.
9. **Logout** — toolbar overflow menu clears the session and returns to the login screen.

---

### ✅ Outcome
A complete, internship-grade Android app with:
- Sign-up, log-in, and logout flows that **work offline**
- **Industry-standard password hashing** (PBKDF2-SHA256 + per-user salt)
- **Per-user data isolation** in SQLite
- Three sections (Tasks / Events / Notes) with **full CRUD**
- Modern Material 3 UI, Day/Night theming, FAB, empty-state hint
- Zero network permissions — 100% offline & private

---


### 📂 Architecture
```
java/com/intern/todoapp/
├── db/DbHelper.java          # SQLite schema, auth, CRUD
├── model/Item.java           # data class
├── util/
│   ├── PasswordHasher.java   # PBKDF2 + constant-time verify
│   └── SessionManager.java   # SharedPreferences session
└── ui/
    ├── LoginActivity.java
    ├── SignupActivity.java
    ├── HomeActivity.java     # tabs + RecyclerView + FAB
    └── EditorActivity.java   # create / edit / delete
```

### 🗄 Database Schema
```sql
users (id PK, email UNIQUE, password_hash TEXT)
items (id PK, user_id FK, section TEXT, title TEXT, body TEXT, created_at INT)
INDEX idx_items_user_section ON items(user_id, section)
```

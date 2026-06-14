# OIBSIP — Android App Development — Task 5
## Stopwatch

A precise Android stopwatch with start, pause, reset, lap-time recording, and rotation safety.

---

### 🎯 Objective
Build an Android stopwatch that can start, pause, resume, and reset, and that can record **lap times** showing both the split (since the previous lap) and the running total. The timer must continue correctly even if the device is rotated.

---

### 🛠 Tools Used
| Tool / Library | Purpose |
|---|---|
| **Android Studio** | IDE |
| **Java** | Application logic |
| **XML** | UI layouts |
| **AndroidX RecyclerView** | Lap times list |
| **`Handler` + `Runnable`** | Periodic UI tick (~31 ms) |
| **Material Components 1.12.0** | `MaterialButton`, `MaterialCardView`, `MaterialToolbar` |
| AGP 8.5.0 / Gradle 8.7 / JDK 17 | Build toolchain |
| Min SDK **24** • Target SDK **34** | API compatibility |

---

### 🔧 Steps Performed
1. **Project setup** — Empty Activity (Java), Material 3 DayNight theme.
2. **UI layout** — large monospace timer display in a Material card, a 3-button row (**Start / Hold / Resume**, **Lap**, **Reset**), a "Lap times" header, and a `RecyclerView` for laps with an empty-state text.
3. **Time-keeping model** — two long fields:
   - `elapsedBeforeStart` — accumulated milliseconds before the current run segment
   - `startedAt` — `System.currentTimeMillis()` when the current segment started
   - Current elapsed = `running ? elapsedBeforeStart + (now - startedAt) : elapsedBeforeStart`
4. **Tick loop** — a `Handler` re-posts a `Runnable` every **31 ms** (centisecond resolution) that re-formats `tvTime` while running.
5. **Primary button states** — the same button cycles: **Start → Hold → Resume**, label switching based on current state. Lap is enabled only while running. Reset is enabled while running or after stopping.
6. **Lap recording** — each Lap tap inserts the current elapsed time at the top of an `ArrayList<Long>`; the `RecyclerView` shows newest first with two columns per row: **+ split** (this total − previous total) and **total** in `HH:MM:SS.cs`.
7. **Rotation safety** — `onSaveInstanceState` persists `elapsedBeforeStart`, `startedAt`, `running` flag, and the lap array as a `long[]`. After rotation the activity rebuilds and resumes ticking from exactly the right moment.
8. **Format helper** — `format(long ms)` returns a fixed `HH:MM:SS.cs` string using `String.format(Locale.US, …)`.
9. **Cleanup** — `onDestroy` removes the tick callback so no leaks occur.

---

### ✅ Outcome
A working stopwatch app featuring:
- **Start / Hold (pause) / Resume / Reset** controls
- **Lap times** recorded with both **split** and **total**
- **HH:MM:SS.cs** monospace display (centisecond precision)
- **Rotation-safe** via `onSaveInstanceState`
- **Material 3** card UI + Day/Night auto theme
- Enabled/disabled button states matching the stopwatch state

---


### 📂 Project Structure
```
java/com/intern/stopwatch/
└── MainActivity.java        # tick handler + RecyclerView LapAdapter

res/layout/
├── activity_main.xml        # timer card + 3-button row + RV
└── item_lap.xml             # Lap n  |  +split  |  total
```

---

### ⏱ Quick demo flow
1. Tap **Start** → timer runs.
2. Tap **Lap** a few times → split & total appear in the list.
3. Tap **Hold** → freezes at the current time; button becomes **Resume**.
4. Tap **Resume** → continues exactly where it paused.
5. Rotate the screen → still ticking from the right instant.
6. Tap **Reset** → everything zeroed out.

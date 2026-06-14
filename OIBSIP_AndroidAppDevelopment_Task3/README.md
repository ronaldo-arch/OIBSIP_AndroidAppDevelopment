# OIBSIP — Android App Development — Task 3
## Calculator

A modern Android calculator that evaluates full mathematical expressions with operator precedence, parentheses, and scientific functions.

---

### 🎯 Objective
Build an Android calculator that goes beyond chained single-operation logic — it should accept a full expression (e.g. `3 + 4 * (2 - 1)`), respect operator precedence, support parentheses, and offer scientific operations (sin, cos, tan, √).

---

### 🛠 Tools Used
| Tool / Library | Purpose |
|---|---|
| **Android Studio** | IDE |
| **Java** | Application logic |
| **XML** | UI layouts |
| **exp4j 0.4.8** | Mathematical expression parser & evaluator |
| **AndroidX GridLayout** | 4×6 button keypad |
| **Material Components 1.12.0** | Material 3 buttons & toolbar |
| **`Vibrator` service** | Haptic feedback on keypress |
| AGP 8.5.0 / Gradle 8.7 / JDK 17 | Build toolchain |
| Min SDK **24** • Target SDK **34** | API compatibility |

---

### 🔧 Steps Performed
1. **Project setup** — Empty Activity (Java), Material 3 DayNight theme, added the dependency `net.objecthunter:exp4j:0.4.8` and `androidx.gridlayout:gridlayout:1.0.0`.
2. **UI layout** — large result display + smaller expression preview on top, then a 4-column `GridLayout` keypad (24 buttons across 6 rows: scientific row, clear/parentheses/÷, 7-8-9-×, 4-5-6-−, 1-2-3-+, ⌫-0-.-=).
3. **Button styles** — `CalcKey` (numbers, outlined), `CalcKeyTonal` (scientific & clear), `CalcKeyOp` (operators, filled), `CalcKeyEq` (equals, bold) defined in `values/styles.xml`.
4. **Expression buffer** — append each token to a `StringBuilder`; backspace erases function names like `sin(` in one tap.
5. **Pretty render** — display `*` as `×`, `/` as `÷`, `-` as `−` while typing, while keeping the raw operator chars in the buffer for the parser.
6. **Trigonometric helpers** — wrap `sin(`, `cos(`, `tan(` so the argument is interpreted in **degrees** (multiplied by π/180 before evaluation).
7. **Evaluation** — pass the prepared expression to **exp4j**'s `ExpressionBuilder` and call `.evaluate()`; show `Error` on `NaN`, infinity, or `Exception`.
8. **Haptic feedback** — every button calls `Vibrator.vibrate(15 ms, default amplitude)`.
9. **Continuation** — after pressing `=`, the next operator continues from the previous result; the next digit starts a fresh expression.
10. **Theming** — Day/Night auto themes, monospace result text for that calculator-feel.

---

### ✅ Outcome
A working calculator app with:
- **Full expression parsing** with correct precedence (no left-to-right hacks)
- **Parentheses, unary minus, decimals**
- **Scientific**: sin, cos, tan (degrees), √
- **Backspace** that handles whole function tokens
- **Haptic** key feedback
- **Error handling** for divide-by-zero, NaN, infinity, malformed input
- **Material 3** + Day/Night theming

---


### 🧪 Test inputs
| Input | Expected |
|---|---|
| `2 + 3 * 4` | `14` |
| `(5 + 5) / 2` | `5` |
| `sin(30)` | `0.5` |
| `√(144)` | `12` |
| `9.5 / 0` | `Error` (no crash) |

---

### 📂 Project Structure
```
app/src/main/
├── AndroidManifest.xml          # VIBRATE permission
├── java/com/intern/calculator/
│   └── MainActivity.java        # button wiring + exp4j eval
└── res/
    ├── layout/activity_main.xml # 4×6 GridLayout keypad
    ├── values/styles.xml        # button styles
    └── values/, values-night/   # day & night themes
```

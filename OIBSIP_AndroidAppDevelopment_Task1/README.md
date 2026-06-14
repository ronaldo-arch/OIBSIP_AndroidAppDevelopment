# OIBSIP — Android App Development — Task 1
## Unit Converter

A native Android application that converts values between different units across multiple measurement categories.

---

### 🎯 Objective
Build an Android app that lets the user pick a category (Length, Mass, Temperature, Volume, Time, Speed), select a *from* and *to* unit, enter a value, and instantly see the converted result. The conversions must be mathematically correct (especially for temperature, which is not a simple multiplier).

---

### 🛠 Tools Used
| Tool / Library | Purpose |
|---|---|
| **Android Studio** (Hedgehog or newer) | IDE |
| **Java** | Application logic |
| **XML** | UI layouts |
| **Material Components 1.12.0** | Material 3 widgets (`TextInputLayout`, `MaterialButton`, exposed-dropdown menus) |
| **AndroidX • ConstraintLayout** | Responsive UI |
| **Android Gradle Plugin 8.5.0 / Gradle 8.7 / JDK 17** | Build toolchain |
| Min SDK **24** • Target SDK **34** | API compatibility |

---

### 🔧 Steps Performed
1. **Project setup** — created a fresh Android Studio project (Empty Activity, Java), set compileSdk 34 / minSdk 24, applied Material 3 `Theme.Material3.DayNight.NoActionBar`.
2. **UI design** — built a single screen with a Material toolbar, category dropdown, value input field, two unit dropdowns (`From` / `To`) with a circular **Swap** button between them, Convert / Clear actions, and a result card.
3. **Conversion engine** — implemented a `LinkedHashMap<String, LinkedHashMap<String, Double>>` lookup table holding *factor-to-base-unit* for each unit; non-temperature conversions go through the base unit using `value * fromFactor / toFactor`.
4. **Temperature** — handled separately with explicit °C ↔ °F ↔ K formulas (normalising to Celsius then converting out).
5. **Input validation** — empty / non-numeric input is caught and shown as a Toast.
6. **Theming** — added `values/themes.xml` (light) and `values-night/themes.xml` (dark) so the app follows the system mode automatically.
7. **Polish** — formatted output with `DecimalFormat`, added an adaptive launcher icon, and tested all 6 categories.

---

### ✅ Outcome
A working Android app with:
- **6 categories**, **20+ units**
- **Accurate temperature math** (not just multiplication)
- One-tap **Swap** to reverse the conversion direction
- **Day / Night** auto theme, Material 3 look-and-feel
- **No internet permission** — fully offline & private
- Clean separation of UI and logic in a single, well-commented `MainActivity.java`

---


### 📂 Project Structure
```
app/src/main/
├── AndroidManifest.xml
├── java/com/intern/unitconverter/
│   └── MainActivity.java         # spinners + conversion logic
└── res/
    ├── layout/activity_main.xml  # Material 3 UI
    ├── values/, values-night/    # day & night themes
    └── drawable/, mipmap-anydpi-v26/  # adaptive launcher icon
```

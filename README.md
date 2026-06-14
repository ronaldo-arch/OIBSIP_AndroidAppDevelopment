# OIBSIP — Android App Development — All 5 Tasks

Five separate native Android Studio projects, submitted in the official **OIBSIP** format
(`OIBSIP_AndroidAppDevelopment_Task<n>`).

| Task | Folder | App |
|---|---|---|
| 1 | `OIBSIP_AndroidAppDevelopment_Task1` | **Unit Converter** |
| 2 | `OIBSIP_AndroidAppDevelopment_Task2` | **Secure To-Do App** (Login + PBKDF2) |
| 3 | `OIBSIP_AndroidAppDevelopment_Task3` | **Calculator** (with scientific ops) |
| 4 | `OIBSIP_AndroidAppDevelopment_Task4` | **Quiz App** (15 GK questions + timer) |
| 5 | `OIBSIP_AndroidAppDevelopment_Task5` | **Stopwatch** (with lap times) |

Each folder is its own complete Android Studio project and contains:
- `README.md` — Objective, Tools Used, Steps Performed, Outcome
- `app/build.gradle`, `settings.gradle`, `build.gradle`, `gradle.properties`
- Full `app/src/main/` (Java + XML + Material 3 day/night themes + adaptive launcher icon)

## How to submit each task to GitHub

1. Create a **new public repo** on GitHub named exactly `OIBSIP_AndroidAppDevelopment_Task1`
   (repeat for Task2 … Task5).
2. Copy the contents of the matching folder into that repo (`README.md` at the root).
3. Add the description and topics from the per-task README in the GitHub repo settings.
4. Push and you're done.

## Common stack
- Java • Android Studio (Hedgehog+)
- AGP **8.5.0** • Gradle **8.7** • JDK **17**
- compileSdk **34**, minSdk **24**
- Material Components 1.12.0, AndroidX
- DayNight (Auto) theming via `values/themes.xml` + `values-night/themes.xml`

## Note
The Gradle wrapper (`gradle/wrapper/gradle-wrapper.jar`) is intentionally omitted from the
zip to keep submissions small. On first open, Android Studio will offer to create the wrapper
automatically (or run `gradle wrapper` once inside the project folder).

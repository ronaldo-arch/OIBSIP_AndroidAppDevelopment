# OIBSIP — Android Development — Task 4
## Quiz App

A multiple-choice General Knowledge quiz Android app with a per-question timer, instant feedback, and a final score screen.

---

### 🎯 Objective
Build an Android quiz app that presents 15 multiple-choice general-knowledge questions, gives the user instant feedback on each answer, tracks the score, enforces a time limit per question, and shows a final result at the end.

---

### 🛠 Tools Used
| Tool / Library | Purpose |
|---|---|
| **Android Studio** | IDE |
| **Java** | Application logic |
| **XML** | UI layouts |
| **Material Components 1.12.0** | `MaterialButton`, `LinearProgressIndicator`, `MaterialToolbar` |
| **AndroidX • CountDownTimer** | Per-question 20-second timer |
| AGP 8.5.0 / Gradle 8.7 / JDK 17 | Build toolchain |
| Min SDK **24** • Target SDK **34** | API compatibility |

---

### 🔧 Steps Performed
1. **Project setup** — Empty Activity project (Java), Material 3 DayNight theme.
2. **Activities** — created 3 activities:
   - `MainActivity` (landing, **Start Quiz** button)
   - `QuizActivity` (the actual quiz loop)
   - `ResultActivity` (final score + retry / home)
3. **Question model** — `Question` POJO with `text`, `String[] options`, `int correctIndex`; a static `bank()` method returns 15 shuffled GK questions covering science, history, geography, languages, tech, art.
4. **Quiz screen UI** — counter (`Question 3 / 15`), `LinearProgressIndicator` for visual progress, the question text, dynamically generated outlined-button options, a feedback line, and a `Next / Finish` button.
5. **Dynamic options** — for each question, options are built at runtime as `MaterialButton` views inside a `LinearLayout`; each click calls `onOptionSelected(idx, correctIndex, button)`.
6. **Feedback rendering** — on answer, the *correct* button turns **green**, the wrong one (if any) turns **red**, and a one-line feedback (`✔ Correct!` / `✘ Wrong answer` / `⏱ Time's up`) appears.
7. **Timer** — a `CountDownTimer` (20 s, tick every 250 ms) updates a `tvTimer` label; on timeout, the correct answer is auto-revealed and the question is locked.
8. **Scoring** — `score++` only on a correct (non-timeout) answer; total questions and final score are passed via `Intent` extras to `ResultActivity`.
9. **Result screen** — emoji + message tiered by percentage: ≥80 % 🏆, ≥60 % 🎉, ≥40 % 🙂, else 📚; offers **Try Again** and **Back to Home**.
10. **Polish** — Day/Night theming, single-line truncation safe, no crashes if the user spam-taps an option (`answered` guard).

---

### ✅ Outcome
A complete quiz app featuring:
- **15-question** built-in GK bank (shuffled each play)
- **4 multiple-choice** options per question
- **20-second timer** per question with live countdown
- **Instant green/red** feedback + correct-answer reveal on time-out
- **Progress bar** showing position in the quiz
- **Final score screen** with retry & home actions
- **Material 3** + Day/Night auto theme
- No network calls — fully offline

---


### 📂 Project Structure
```
java/com/intern/quizapp/
├── MainActivity.java        # landing screen
├── Question.java            # POJO + static 15-Q bank
├── QuizActivity.java        # quiz loop, timer, scoring
└── ResultActivity.java      # final score + retry

res/layout/
├── activity_main.xml        # start screen
├── activity_quiz.xml        # question + options + timer + progress
└── activity_result.xml      # emoji + score + actions
```

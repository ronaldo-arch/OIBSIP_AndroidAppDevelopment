package com.intern.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final long TIME_PER_Q_MS = 20_000L;

    private List<Question> questions;
    private int index = 0;
    private int score = 0;
    private boolean answered = false;
    private CountDownTimer timer;

    private TextView tvCounter, tvTimer, tvQuestion, tvFeedback;
    private LinearLayout optionsContainer;
    private LinearProgressIndicator progress;
    private MaterialButton btnNext;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_quiz);

        tvCounter = findViewById(R.id.tvCounter);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback);
        optionsContainer = findViewById(R.id.optionsContainer);
        progress = findViewById(R.id.progress);
        btnNext = findViewById(R.id.btnNext);

        questions = Question.bank();
        showQuestion();

        btnNext.setOnClickListener(v -> {
            index++;
            if (index >= questions.size()) finishQuiz();
            else showQuestion();
        });
    }

    private void showQuestion() {
        answered = false;
        Question q = questions.get(index);
        tvCounter.setText("Question " + (index + 1) + " / " + questions.size());
        tvQuestion.setText(q.text);
        tvFeedback.setVisibility(View.INVISIBLE);
        btnNext.setEnabled(false);
        btnNext.setText(index == questions.size() - 1 ? R.string.finish : R.string.next);
        progress.setProgressCompat((index * 100) / questions.size(), true);

        optionsContainer.removeAllViews();
        int dp12 = dp(12);
        for (int i = 0; i < q.options.length; i++) {
            final int idx = i;
            MaterialButton b = new MaterialButton(this, null,
                    com.google.android.material.R.attr.materialButtonOutlinedStyle);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = dp12;
            b.setLayoutParams(lp);
            b.setText(q.options[i]);
            b.setAllCaps(false);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            b.setOnClickListener(v -> onOptionSelected(idx, q.correctIndex, b));
            optionsContainer.addView(b);
        }
        startTimer();
    }

    private void onOptionSelected(int chosen, int correct, MaterialButton chosenBtn) {
        if (answered) return;
        answered = true;
        stopTimer();
        revealAnswer(chosen, correct);
    }

    private void revealAnswer(int chosen, int correct) {
        // chosen == -1 means time out
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            MaterialButton b = (MaterialButton) optionsContainer.getChildAt(i);
            b.setEnabled(false);
            if (i == correct) {
                b.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#2E7D32")));
                b.setTextColor(Color.parseColor("#2E7D32"));
            } else if (i == chosen) {
                b.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#C62828")));
                b.setTextColor(Color.parseColor("#C62828"));
            }
        }
        tvFeedback.setVisibility(View.VISIBLE);
        if (chosen == correct) {
            score++;
            tvFeedback.setText("✔  Correct!");
            tvFeedback.setTextColor(Color.parseColor("#2E7D32"));
        } else if (chosen == -1) {
            tvFeedback.setText("⏱  Time's up — correct answer highlighted");
            tvFeedback.setTextColor(Color.parseColor("#C62828"));
        } else {
            tvFeedback.setText("✘  Wrong answer");
            tvFeedback.setTextColor(Color.parseColor("#C62828"));
        }
        btnNext.setEnabled(true);
    }

    private void startTimer() {
        stopTimer();
        timer = new CountDownTimer(TIME_PER_Q_MS, 250) {
            @Override public void onTick(long ms) {
                tvTimer.setText(getString(R.string.time_left, (ms + 999) / 1000));
            }
            @Override public void onFinish() {
                if (!answered) {
                    answered = true;
                    revealAnswer(-1, questions.get(index).correctIndex);
                }
            }
        }.start();
    }

    private void stopTimer() { if (timer != null) { timer.cancel(); timer = null; } }

    private void finishQuiz() {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("score", score);
        i.putExtra("total", questions.size());
        startActivity(i);
        finish();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    @Override protected void onDestroy() { stopTimer(); super.onDestroy(); }
}

package com.intern.quizapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);
        MaterialButton btn = findViewById(R.id.btnStart);
        btn.setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));
    }
}

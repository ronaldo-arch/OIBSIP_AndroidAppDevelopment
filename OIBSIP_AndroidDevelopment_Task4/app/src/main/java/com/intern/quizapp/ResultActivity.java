package com.intern.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        double pct = total == 0 ? 0 : (score * 100.0 / total);

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvMsg = findViewById(R.id.tvMessage);
        TextView tvEmoji = findViewById(R.id.tvEmoji);
        tvScore.setText(score + " / " + total);

        String emoji, msg;
        if (pct >= 80)       { emoji = "🏆"; msg = "Excellent! Quiz master."; }
        else if (pct >= 60)  { emoji = "🎉"; msg = "Nice work — solid score!"; }
        else if (pct >= 40)  { emoji = "🙂"; msg = "Not bad. Try again to beat it!"; }
        else                 { emoji = "📚"; msg = "Keep practising — you'll get there."; }
        tvEmoji.setText(emoji);
        tvMsg.setText(msg);

        MaterialButton btnRetry = findViewById(R.id.btnRetry);
        MaterialButton btnHome = findViewById(R.id.btnHome);
        btnRetry.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}

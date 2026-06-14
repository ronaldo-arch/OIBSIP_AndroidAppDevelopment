package com.intern.calculator;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;

/**
 * Calculator — internship task 3
 * - Full expression input with parentheses and operator precedence
 * - Scientific: sin, cos, tan (degrees), sqrt
 * - Uses exp4j for evaluation (handles precedence, unary minus, parentheses)
 * - Subtle haptic feedback on every key press
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvExpression, tvResult;
    private StringBuilder expr = new StringBuilder();
    private boolean justEvaluated = false;
    private Vibrator vibrator;
    private final DecimalFormat fmt = new DecimalFormat("#,##0.##########");

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        int[] digitIds = {R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4,
                R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9};
        for (int i = 0; i < digitIds.length; i++) {
            final String d = String.valueOf(i);
            findViewById(digitIds[i]).setOnClickListener(v -> press(d));
        }
        findViewById(R.id.bDot).setOnClickListener(v -> press("."));
        findViewById(R.id.bAdd).setOnClickListener(v -> press("+"));
        findViewById(R.id.bSub).setOnClickListener(v -> press("-"));
        findViewById(R.id.bMul).setOnClickListener(v -> press("*"));
        findViewById(R.id.bDiv).setOnClickListener(v -> press("/"));
        findViewById(R.id.bParenL).setOnClickListener(v -> press("("));
        findViewById(R.id.bParenR).setOnClickListener(v -> press(")"));
        findViewById(R.id.bSin).setOnClickListener(v -> press("sin("));
        findViewById(R.id.bCos).setOnClickListener(v -> press("cos("));
        findViewById(R.id.bTan).setOnClickListener(v -> press("tan("));
        findViewById(R.id.bSqrt).setOnClickListener(v -> press("sqrt("));
        findViewById(R.id.bClear).setOnClickListener(v -> clear());
        findViewById(R.id.bBack).setOnClickListener(v -> backspace());
        findViewById(R.id.bEq).setOnClickListener(v -> evaluate());
    }

    private void press(String token) {
        haptic();
        if (justEvaluated) {
            // continue from previous result, or start fresh on number / function
            if (isOperator(token)) {
                expr = new StringBuilder(tvResult.getText().toString().replace(",", ""));
            } else {
                expr.setLength(0);
            }
            justEvaluated = false;
        }
        // beautify: render * × and / as ÷ in the expression line, but keep raw
        expr.append(token);
        renderExpression();
    }

    private boolean isOperator(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/");
    }

    private void clear() {
        haptic();
        expr.setLength(0);
        justEvaluated = false;
        tvExpression.setText("");
        tvResult.setText("0");
    }

    private void backspace() {
        haptic();
        if (expr.length() > 0) {
            // erase whole function names if at end
            String s = expr.toString();
            for (String fn : new String[]{"sin(", "cos(", "tan(", "sqrt("}) {
                if (s.endsWith(fn)) {
                    expr.setLength(expr.length() - fn.length());
                    renderExpression();
                    return;
                }
            }
            expr.deleteCharAt(expr.length() - 1);
            renderExpression();
        }
    }

    private void evaluate() {
        haptic();
        if (expr.length() == 0) return;
        try {
            // wrap trig so input is in degrees
            String raw = expr.toString();
            String prepared = raw
                    .replace("sin(", "sin(toRad ")     // marker, replaced below
                    .replace("cos(", "cos(toRad ")
                    .replace("tan(", "tan(toRad ");
            // exp4j supports custom functions, but a simpler trick: multiply argument by PI/180
            prepared = prepared.replace("toRad ", "(3.141592653589793/180)*");

            Expression e = new ExpressionBuilder(prepared).build();
            double r = e.evaluate();
            if (Double.isNaN(r) || Double.isInfinite(r)) throw new ArithmeticException("undefined");
            tvResult.setText(fmt.format(r));
            justEvaluated = true;
        } catch (Exception ex) {
            tvResult.setText(getString(R.string.err));
        }
    }

    private void renderExpression() {
        String pretty = expr.toString()
                .replace("*", " × ")
                .replace("/", " ÷ ")
                .replace("+", " + ")
                .replace("-", " − ");
        tvExpression.setText(pretty);
    }

    private void haptic() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}

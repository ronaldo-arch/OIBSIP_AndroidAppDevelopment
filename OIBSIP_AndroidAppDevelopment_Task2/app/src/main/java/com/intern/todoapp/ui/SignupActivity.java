package com.intern.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.intern.todoapp.R;
import com.intern.todoapp.db.DbHelper;
import com.intern.todoapp.util.SessionManager;

public class SignupActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_signup);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPwd = findViewById(R.id.etPassword);
        TextInputEditText etConfirm = findViewById(R.id.etConfirm);
        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        MaterialButton btnSwitch = findViewById(R.id.btnSwitch);
        DbHelper db = new DbHelper(this);
        SessionManager sm = new SessionManager(this);

        btnSignup.setOnClickListener(v -> {
            String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
            String pwd = etPwd.getText() == null ? "" : etPwd.getText().toString();
            String confirm = etConfirm.getText() == null ? "" : etConfirm.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(this, R.string.err_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.err_invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }
            if (pwd.length() < 6) {
                Toast.makeText(this, R.string.err_short_pwd, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pwd.equals(confirm)) {
                Toast.makeText(this, R.string.err_pwd_mismatch, Toast.LENGTH_SHORT).show();
                return;
            }
            long uid = db.signup(email, pwd);
            if (uid == -1) {
                Toast.makeText(this, R.string.err_email_taken, Toast.LENGTH_SHORT).show();
                return;
            }
            sm.login(uid, email);
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        btnSwitch.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}

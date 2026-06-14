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

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        SessionManager sm = new SessionManager(this);
        if (sm.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPwd = findViewById(R.id.etPassword);
        MaterialButton btnPrimary = findViewById(R.id.btnPrimary);
        MaterialButton btnSwitch = findViewById(R.id.btnSwitch);
        DbHelper db = new DbHelper(this);

        btnPrimary.setOnClickListener(v -> {
            String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
            String pwd = etPwd.getText() == null ? "" : etPwd.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, R.string.err_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.err_invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }
            long uid = db.login(email, pwd);
            if (uid == -1) {
                Toast.makeText(this, R.string.err_bad_credentials, Toast.LENGTH_SHORT).show();
                return;
            }
            sm.login(uid, email);
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        btnSwitch.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }
}

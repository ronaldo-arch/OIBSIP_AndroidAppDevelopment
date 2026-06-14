package com.intern.todoapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.intern.todoapp.R;
import com.intern.todoapp.db.DbHelper;
import com.intern.todoapp.util.SessionManager;

public class EditorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText etTitle = findViewById(R.id.etTitle);
        TextInputEditText etBody = findViewById(R.id.etBody);
        MaterialButton btnSave = findViewById(R.id.btnSave);
        MaterialButton btnDelete = findViewById(R.id.btnDelete);

        String section = getIntent().getStringExtra("section");
        long id = getIntent().getLongExtra("id", -1);
        boolean editing = id != -1;

        if (editing) {
            etTitle.setText(getIntent().getStringExtra("title"));
            etBody.setText(getIntent().getStringExtra("body"));
            btnDelete.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Edit");
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("New " + section);
        }

        DbHelper db = new DbHelper(this);
        SessionManager sm = new SessionManager(this);

        btnSave.setOnClickListener(v -> {
            String t = etTitle.getText() == null ? "" : etTitle.getText().toString().trim();
            String b = etBody.getText() == null ? "" : etBody.getText().toString().trim();
            if (TextUtils.isEmpty(t)) {
                Toast.makeText(this, R.string.err_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (editing) db.updateItem(id, t, b);
            else db.insertItem(sm.userId(), section, t, b);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            if (editing) {
                db.deleteItem(id);
                finish();
            }
        });
    }
}

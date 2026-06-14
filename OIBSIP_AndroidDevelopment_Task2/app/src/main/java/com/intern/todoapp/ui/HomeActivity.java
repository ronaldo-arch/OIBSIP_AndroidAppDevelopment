package com.intern.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.intern.todoapp.R;
import com.intern.todoapp.db.DbHelper;
import com.intern.todoapp.model.Item;
import com.intern.todoapp.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final String[] SECTIONS = {"tasks", "events", "notes"};
    private static final int[] LABELS = {R.string.tab_tasks, R.string.tab_events, R.string.tab_notes};

    private TabLayout tabs;
    private RecyclerView rv;
    private TextView tvEmpty;
    private ExtendedFloatingActionButton fab;
    private final List<Item> data = new ArrayList<>();
    private CardAdapter adapter;
    private DbHelper db;
    private SessionManager sm;
    private int section = 0;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_home);
        sm = new SessionManager(this);
        if (!sm.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        db = new DbHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(sm.email());

        tabs = findViewById(R.id.tabs);
        rv = findViewById(R.id.rv);
        tvEmpty = findViewById(R.id.tvEmpty);
        fab = findViewById(R.id.fab);

        for (int label : LABELS) tabs.addTab(tabs.newTab().setText(label));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                section = tab.getPosition();
                refresh();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        adapter = new CardAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        fab.setOnClickListener(v -> openEditor(null));
    }

    @Override protected void onResume() {
        super.onResume();
        if (sm != null && sm.isLoggedIn()) refresh();
    }

    private void refresh() {
        data.clear();
        data.addAll(db.listItems(sm.userId(), SECTIONS[section]));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openEditor(Item item) {
        Intent i = new Intent(this, EditorActivity.class);
        i.putExtra("section", SECTIONS[section]);
        if (item != null) {
            i.putExtra("id", item.id);
            i.putExtra("title", item.title);
            i.putExtra("body", item.body);
        }
        startActivity(i);
    }

    @Override public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.add(0, 1, 0, R.string.logout)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            sm.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.VH> {
        private final SimpleDateFormat fmt = new SimpleDateFormat("d MMM yyyy • HH:mm", Locale.getDefault());
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card, parent, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Item it = data.get(pos);
            h.tvTitle.setText(it.title);
            h.tvBody.setText(it.body);
            h.tvBody.setVisibility(it.body == null || it.body.isEmpty() ? View.GONE : View.VISIBLE);
            h.tvDate.setText(fmt.format(new Date(it.createdAt)));
            h.itemView.setOnClickListener(v -> openEditor(it));
        }
        @Override public int getItemCount() { return data.size(); }
        class VH extends RecyclerView.ViewHolder {
            final TextView tvTitle, tvBody, tvDate;
            VH(View v) {
                super(v);
                tvTitle = v.findViewById(R.id.tvTitle);
                tvBody = v.findViewById(R.id.tvBody);
                tvDate = v.findViewById(R.id.tvDate);
            }
        }
    }
}

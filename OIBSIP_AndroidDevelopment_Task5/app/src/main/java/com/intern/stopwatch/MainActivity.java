package com.intern.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stopwatch — internship task 5
 * - Start / Hold (pause) / Resume / Reset
 * - Lap recording with split + total
 * - Uses SystemClock-style elapsedRealtime (via System.nanoTime) and a Handler tick every 30ms
 * - Survives rotation through onSaveInstanceState
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_ELAPSED = "elapsedBeforeStart";
    private static final String KEY_STARTED_AT = "startedAt";
    private static final String KEY_RUNNING = "running";
    private static final String KEY_LAPS = "laps";

    private TextView tvTime, tvEmpty;
    private MaterialButton btnPrimary, btnLap, btnReset;
    private RecyclerView rvLaps;

    private long elapsedBeforeStart = 0L;   // total accumulated millis before current run segment
    private long startedAt = 0L;            // start timestamp of current segment (ms since epoch)
    private boolean running = false;

    private final List<Long> laps = new ArrayList<>();
    private LapAdapter adapter;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            tvTime.setText(format(currentElapsed()));
            handler.postDelayed(this, 31);
        }
    };

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnPrimary = findViewById(R.id.btnPrimary);
        btnLap = findViewById(R.id.btnLap);
        btnReset = findViewById(R.id.btnReset);
        rvLaps = findViewById(R.id.rvLaps);

        adapter = new LapAdapter(laps);
        rvLaps.setLayoutManager(new LinearLayoutManager(this));
        rvLaps.setAdapter(adapter);

        if (s != null) {
            elapsedBeforeStart = s.getLong(KEY_ELAPSED, 0L);
            startedAt = s.getLong(KEY_STARTED_AT, 0L);
            running = s.getBoolean(KEY_RUNNING, false);
            long[] arr = s.getLongArray(KEY_LAPS);
            if (arr != null) for (long v : arr) laps.add(v);
        }
        refreshUi();
        if (running) handler.post(tick);

        btnPrimary.setOnClickListener(v -> {
            if (running) hold(); else start();
        });
        btnLap.setOnClickListener(v -> {
            laps.add(0, currentElapsed());
            adapter.notifyItemInserted(0);
            rvLaps.scrollToPosition(0);
            tvEmpty.setVisibility(View.GONE);
        });
        btnReset.setOnClickListener(v -> reset());
    }

    private long currentElapsed() {
        return running ? (elapsedBeforeStart + (System.currentTimeMillis() - startedAt)) : elapsedBeforeStart;
    }

    private void start() {
        startedAt = System.currentTimeMillis();
        running = true;
        handler.post(tick);
        refreshUi();
    }

    private void hold() {
        elapsedBeforeStart = currentElapsed();
        running = false;
        handler.removeCallbacks(tick);
        tvTime.setText(format(elapsedBeforeStart));
        refreshUi();
    }

    private void reset() {
        running = false;
        elapsedBeforeStart = 0L;
        startedAt = 0L;
        handler.removeCallbacks(tick);
        laps.clear();
        adapter.notifyDataSetChanged();
        tvTime.setText(format(0));
        refreshUi();
    }

    private void refreshUi() {
        btnPrimary.setText(running ? R.string.btn_hold : (elapsedBeforeStart > 0 ? R.string.btn_resume : R.string.btn_start));
        btnLap.setEnabled(running);
        btnReset.setEnabled(running || elapsedBeforeStart > 0);
        tvEmpty.setVisibility(laps.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putLong(KEY_ELAPSED, running ? currentElapsed() : elapsedBeforeStart);
        out.putLong(KEY_STARTED_AT, System.currentTimeMillis());
        out.putBoolean(KEY_RUNNING, running);
        long[] arr = new long[laps.size()];
        for (int i = 0; i < laps.size(); i++) arr[i] = laps.get(i);
        out.putLongArray(KEY_LAPS, arr);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(tick);
        super.onDestroy();
    }

    /** HH:MM:SS.cs */
    static String format(long ms) {
        long cs = (ms / 10) % 100;
        long s = (ms / 1000) % 60;
        long m = (ms / 60000) % 60;
        long h = (ms / 3600000);
        return String.format(Locale.US, "%02d:%02d:%02d.%02d", h, m, s, cs);
    }

    /** RecyclerView adapter for lap times. */
    private class LapAdapter extends RecyclerView.Adapter<LapAdapter.VH> {
        private final List<Long> data;
        LapAdapter(List<Long> data) { this.data = data; }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lap, parent, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            // data is in reverse-chronological order (newest first)
            int totalCount = data.size();
            int lapNumber = totalCount - pos;       // 1-based, oldest = 1
            long total = data.get(pos);
            // split = this total minus previous lap total (the one just older = pos+1)
            long previousTotal = (pos + 1 < totalCount) ? data.get(pos + 1) : 0;
            long split = total - previousTotal;
            h.tvIdx.setText(String.format(Locale.US, "Lap %d", lapNumber));
            h.tvSplit.setText("+" + format(split));
            h.tvTotal.setText(format(total));
        }
        @Override public int getItemCount() { return data.size(); }

        class VH extends RecyclerView.ViewHolder {
            final TextView tvIdx, tvSplit, tvTotal;
            VH(View v) {
                super(v);
                tvIdx = v.findViewById(R.id.tvIdx);
                tvSplit = v.findViewById(R.id.tvSplit);
                tvTotal = v.findViewById(R.id.tvTotal);
            }
        }
    }
}

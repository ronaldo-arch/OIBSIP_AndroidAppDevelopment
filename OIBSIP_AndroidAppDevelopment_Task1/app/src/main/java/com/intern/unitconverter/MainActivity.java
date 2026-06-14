package com.intern.unitconverter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Unit Converter — internship task 1
 * - 6 categories (Length, Mass, Temperature, Volume, Time, Speed)
 * - All non-temperature conversions go via a base unit using a multiplier table
 * - Temperature is handled with explicit formulas (C/F/K)
 */
public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView spCategory, spFrom, spTo;
    private TextInputEditText etValue;
    private android.widget.TextView tvResult;

    /** category -> ordered map of unit name -> factor relative to base unit */
    private final Map<String, LinkedHashMap<String, Double>> table = new LinkedHashMap<>();

    private final DecimalFormat df = new DecimalFormat("#,##0.######");

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);

        buildTable();

        spCategory = findViewById(R.id.spinnerCategory);
        spFrom = findViewById(R.id.spinnerFrom);
        spTo = findViewById(R.id.spinnerTo);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);

        MaterialButton btnConvert = findViewById(R.id.btnConvert);
        MaterialButton btnClear = findViewById(R.id.btnClear);
        MaterialButton btnSwap = findViewById(R.id.btnSwap);

        // category dropdown
        String[] cats = table.keySet().toArray(new String[0]);
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cats));
        spCategory.setText(cats[0], false);
        loadUnits(cats[0]);

        spCategory.setOnItemClickListener((parent, view, position, id) ->
                loadUnits(parent.getItemAtPosition(position).toString()));

        btnConvert.setOnClickListener(v -> convert());
        btnClear.setOnClickListener(v -> {
            etValue.setText("");
            tvResult.setText("—");
        });
        btnSwap.setOnClickListener(v -> {
            String f = spFrom.getText().toString();
            spFrom.setText(spTo.getText().toString(), false);
            spTo.setText(f, false);
            convert();
        });
    }

    private void loadUnits(String category) {
        String[] units = table.get(category).keySet().toArray(new String[0]);
        spFrom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, units));
        spTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, units));
        spFrom.setText(units[0], false);
        spTo.setText(units.length > 1 ? units[1] : units[0], false);
        tvResult.setText("—");
    }

    private void convert() {
        String raw = etValue.getText() == null ? "" : etValue.getText().toString().trim();
        if (TextUtils.isEmpty(raw)) {
            Toast.makeText(this, R.string.error_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        double value;
        try { value = Double.parseDouble(raw); }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        String cat = spCategory.getText().toString();
        String from = spFrom.getText().toString();
        String to = spTo.getText().toString();
        if (from.equals(to)) {
            tvResult.setText(df.format(value) + " " + to);
            return;
        }
        double out;
        if ("Temperature".equals(cat)) {
            out = convertTemperature(value, from, to);
        } else {
            LinkedHashMap<String, Double> m = table.get(cat);
            double base = value * m.get(from);
            out = base / m.get(to);
        }
        tvResult.setText(df.format(out) + " " + to);
    }

    private double convertTemperature(double v, String from, String to) {
        // normalise to Celsius
        double c;
        switch (from) {
            case "Fahrenheit (°F)": c = (v - 32) * 5.0 / 9.0; break;
            case "Kelvin (K)":      c = v - 273.15; break;
            default:                c = v; // Celsius
        }
        switch (to) {
            case "Fahrenheit (°F)": return c * 9.0 / 5.0 + 32;
            case "Kelvin (K)":      return c + 273.15;
            default:                return c;
        }
    }

    private void buildTable() {
        // Length — base: metre
        LinkedHashMap<String, Double> length = new LinkedHashMap<>();
        length.put("Millimeter (mm)", 0.001);
        length.put("Centimeter (cm)", 0.01);
        length.put("Meter (m)", 1.0);
        length.put("Kilometer (km)", 1000.0);
        length.put("Inch (in)", 0.0254);
        length.put("Foot (ft)", 0.3048);
        length.put("Yard (yd)", 0.9144);
        length.put("Mile (mi)", 1609.344);
        table.put("Length", length);

        // Mass — base: kilogram
        LinkedHashMap<String, Double> mass = new LinkedHashMap<>();
        mass.put("Milligram (mg)", 0.000001);
        mass.put("Gram (g)", 0.001);
        mass.put("Kilogram (kg)", 1.0);
        mass.put("Tonne (t)", 1000.0);
        mass.put("Ounce (oz)", 0.0283495);
        mass.put("Pound (lb)", 0.453592);
        table.put("Mass", mass);

        // Temperature — handled separately, names matter for switch
        LinkedHashMap<String, Double> temp = new LinkedHashMap<>();
        temp.put("Celsius (°C)", 1.0);
        temp.put("Fahrenheit (°F)", 1.0);
        temp.put("Kelvin (K)", 1.0);
        table.put("Temperature", temp);

        // Volume — base: litre
        LinkedHashMap<String, Double> vol = new LinkedHashMap<>();
        vol.put("Milliliter (ml)", 0.001);
        vol.put("Liter (L)", 1.0);
        vol.put("Cubic meter (m³)", 1000.0);
        vol.put("Cup (US)", 0.2365882);
        vol.put("Pint (US)", 0.4731765);
        vol.put("Gallon (US)", 3.7854118);
        table.put("Volume", vol);

        // Time — base: second
        LinkedHashMap<String, Double> time = new LinkedHashMap<>();
        time.put("Millisecond (ms)", 0.001);
        time.put("Second (s)", 1.0);
        time.put("Minute (min)", 60.0);
        time.put("Hour (h)", 3600.0);
        time.put("Day (d)", 86400.0);
        time.put("Week (wk)", 604800.0);
        table.put("Time", time);

        // Speed — base: m/s
        LinkedHashMap<String, Double> speed = new LinkedHashMap<>();
        speed.put("Meter / second (m/s)", 1.0);
        speed.put("Kilometer / hour (km/h)", 0.2777778);
        speed.put("Mile / hour (mph)", 0.44704);
        speed.put("Knot (kn)", 0.5144444);
        table.put("Speed", speed);
    }
}

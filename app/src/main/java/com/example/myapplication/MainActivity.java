package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView resultText;
    private Button convertButton;
    private final Map<String, Double> conversionFactors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        resultText = findViewById(R.id.resultText);
        convertButton = findViewById(R.id.convertButton);

        // Populate conversion factors
        initializeConversionFactors();

        // Populate Spinners
        String[] units = {
                "inch", "cm", "foot", "yard", "mile", "km",
                "pound", "kg", "ounce", "g", "ton",
                "Celsius", "Fahrenheit", "Kelvin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Convert button click listener
        convertButton.setOnClickListener(view -> performConversion());
    }

    private void initializeConversionFactors() {
        // Length
        conversionFactors.put("inch to cm", 2.54);
        conversionFactors.put("cm to inch", 1 / 2.54);
        conversionFactors.put("foot to cm", 30.48);
        conversionFactors.put("cm to foot", 1 / 30.48);
        conversionFactors.put("yard to cm", 91.44);
        conversionFactors.put("cm to yard", 1 / 91.44);
        conversionFactors.put("mile to km", 1.60934);
        conversionFactors.put("km to mile", 1 / 1.60934);

        // Weight
        conversionFactors.put("pound to kg", 0.453592);
        conversionFactors.put("kg to pound", 1 / 0.453592);
        conversionFactors.put("ounce to g", 28.3495);
        conversionFactors.put("g to ounce", 1 / 28.3495);
        conversionFactors.put("ton to kg", 907.185);
        conversionFactors.put("kg to ton", 1 / 907.185);
    }

    private void performConversion() {
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();
        String inputText = inputValue.getText().toString();

        if (inputText.isEmpty()) {
            resultText.setText("Please enter a value.");
            return;
        }

        try {
            double value = Double.parseDouble(inputText);
            double convertedValue = convertUnit(value, fromUnit, toUnit);

            if (Double.isNaN(convertedValue)) {
                resultText.setText("Conversion between selected units is not supported.");
            } else {
                resultText.setText("Converted Value: " + convertedValue);
            }
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input. Please enter a numeric value.");
        }
    }

    private double convertUnit(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }

        String key = fromUnit + " to " + toUnit;
        if (conversionFactors.containsKey(key)) {
            return value * conversionFactors.get(key);
        }

        // Temperature conversions
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (value - 32) / 1.8;
        } else if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return value + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return value - 273.15;
        }

        // Unsupported conversion
        return Double.NaN;
    }
}

package com.example.converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;

public class Temperature extends AppCompatActivity {
    TextView currency, tempAnswer;
    Spinner spinner;
    TextInputEditText value;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_temperature);
        initializeViews();
        goToCurrency();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conversion_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTemperature();
            }
        });

    }
    private void initializeViews(){
        currency = findViewById(R.id.cc2);
        tempAnswer = findViewById(R.id.temp_answer);
        spinner =findViewById(R.id.temperatureSpinner);
        value = findViewById(R.id.temp_value);
        button =findViewById(R.id.convert_temp);
    }
    private  void goToCurrency(){
        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(Temperature.this, Home_screen.class));
                startActivity(intent);
            }
        });

    }
    private void convertTemperature(){
        String selectedConversion = spinner.getSelectedItem().toString();
        String inputTemp = Objects.requireNonNull(value.getText()).toString();
        if (!inputTemp.isEmpty()) {
            double temperature = Double.parseDouble(inputTemp);
            double result = 0;

            switch (selectedConversion) {
                case "Celsius to Fahrenheit":
                    result = celsiusToFahrenheit(temperature);
                    break;
                case "Fahrenheit to Celsius":
                    result = fahrenheitToCelsius(temperature);
                    break;
            }
            tempAnswer.setText(String.format(Locale.CANADA,"Result: %.2f", result));
        } else {
            tempAnswer.setText("Please enter a temperature.");
        }


    }
    private double celsiusToFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }

    private double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5/9;
    }

}
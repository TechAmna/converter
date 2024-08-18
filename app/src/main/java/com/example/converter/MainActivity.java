package com.example.converter;

import android.content.Intent;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH=3000; //duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

            new Handler().postDelayed(() -> {
                // After the splash screen duration, start the main activity
                Intent mainIntent = new Intent(MainActivity.this, Home_screen.class);
                startActivity(mainIntent);
                finish(); // finish splash so can't be return
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }, SPLASH_DISPLAY_LENGTH);

    }
}
package com.example.myecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingScreen extends AppCompatActivity {

    private Button btnStart;
    private ConstraintLayout startLayout;
    private TextView ecommerce;
    private ProgressBar progressBarStart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        btnStart = findViewById(R.id.btnStart);
        progressBarStart = findViewById(R.id.progressBarStart);
        startLayout = findViewById(R.id.startLayout);
        ecommerce = findViewById(R.id.myEcommerce);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start animation and launch next activity
                animateTextAndStartActivity();
//                progressBarStart.setVisibility(View.VISIBLE);
//                Intent startIntent = new Intent(LoadingScreen.this, Products.class);
//                startActivity(startIntent);
//                finish();
            }
        });
    }

    private void animateTextAndStartActivity() {
        // Fade in animation for the text view
        ecommerce.animate()
                .alpha(1f) // Set final alpha value (fully visible)
                .setDuration(1000) // Set animation duration in milliseconds
                .start(); // Start the animation

        // Delay launching the next activity after animation completes
        startLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Launch Products activity
                Intent startIntent = new Intent(LoadingScreen.this, Products.class);
                startActivity(startIntent);
                finish(); // Finish current activity
            }
        }, 1000); // Delay duration (in milliseconds) matches animation duration
    }

}
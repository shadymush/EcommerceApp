package com.example.myecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class LoadingScreen extends AppCompatActivity {

    private Button btnStart;
    private ProgressBar progressBarStart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        btnStart = findViewById(R.id.btnStart);
        progressBarStart = findViewById(R.id.progressBarStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarStart.setVisibility(View.VISIBLE);
                Intent startIntent = new Intent(LoadingScreen.this, Products.class);
                startActivity(startIntent);
                finish();
            }
        });
    }
}
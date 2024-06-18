package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView dashboardGreetings, loanBalance, dueDate;
    private ImageButton btnNotification, btnProfile, btnLogout;
    private Button btnRepay, btnWithdraw, btnLoanServices, btnCommunityEngagement, btnAgricProducts, btnArticles;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardGreetings = findViewById(R.id.dashboardGreetings);
        loanBalance = findViewById(R.id.loanBalance);
        dueDate = findViewById(R.id.dueDate);
        btnNotification = findViewById(R.id.btnNotification);
        btnProfile = findViewById(R.id.btnProfile);
        btnRepay = findViewById(R.id.btnRepay);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnLoanServices = findViewById(R.id.btnLoanServices);
        btnCommunityEngagement = findViewById(R.id.btnCommunityEngagement);
        btnAgricProducts = findViewById(R.id.btnAgricProducts);
        btnArticles = findViewById(R.id.btnArticles);
        btnLogout = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //display user name on dashboard
        loadUserName();

        //repay button on click listener
        btnRepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent repay = new Intent(Dashboard.this, RepayActivity.class);
                startActivity(repay);
               // finish();
            }
        });

        //withdraw button on click listener
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdraw = new Intent(Dashboard.this, WithdrawActivity.class);
                startActivity(withdraw);
               // finish();
            }
        });

        //clicklisteners for Image Buttons go here, logout, notification and account
//btnprofile listener
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Dashboard.this, ProfileSection.class);
                startActivity(profile);
                //finish();
            }
        });

        //btnlogout listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //services offered on click listeners

        //service 1
        btnLoanServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(Dashboard.this, LoanServices.class);
                startActivity(loans);
            }
        });

        //service 2 community engagements
        btnCommunityEngagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityIntent = new Intent(Dashboard.this, CommunityContributions.class);
                startActivity(communityIntent);
            }
        });

    }

    private void logoutUser() {
        mAuth.signOut();
        Intent loginIntent = new Intent(Dashboard.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish(); // Finish the Dashboard activity so the user cannot go back to it
    }

    private void loadUserName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        dashboardGreetings.setText("Welcome " + name);
                    } else {
                        Toast.makeText(Dashboard.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Dashboard.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(Dashboard.this, Login.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }
    }
}
package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView dashboardGreetings, loanBalance, dueDate;
    private ImageButton btnNotification, btnProfile, btnLogout;
    private Button btnRepay, btnWithdraw, btnLoanServices, btnCommunityEngagement, btnAgricProducts;

    // RecyclerView and Adapter for articles
    private RecyclerView recyclerViewArticles;
    private ArticleAdapter articleAdapter;
    private ArrayList<Article> articleList;
    private DatabaseReference databaseArticles;

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
        btnLogout = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Setup RecyclerView for articles
        recyclerViewArticles = findViewById(R.id.recyclerViewArticles);
        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleList);
        recyclerViewArticles.setAdapter(articleAdapter);

        databaseArticles = FirebaseDatabase.getInstance().getReference("articles");
        fetchArticles();



        // Display user name on dashboard
        loadUserName();

        // Repay button on click listener
        btnRepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent repay = new Intent(Dashboard.this, RepayActivity.class);
                startActivity(repay);
            }
        });

        // Withdraw button on click listener
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdraw = new Intent(Dashboard.this, WithdrawActivity.class);
                startActivity(withdraw);
            }
        });

        // Click listeners for Image Buttons go here, logout, notification and account
        // btnProfile listener
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Dashboard.this, ProfileSection.class);
                startActivity(profile);
            }
        });

        // btnLogout listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Services offered on click listeners
        // Service 1
        btnLoanServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loans = new Intent(Dashboard.this, LoanServices.class);
                startActivity(loans);
            }
        });

        // Service 2 community engagements
        btnCommunityEngagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityIntent = new Intent(Dashboard.this, CommunityContributions.class);
                startActivity(communityIntent);
            }
        });

        // Service 3 agricultural products
        btnAgricProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(Dashboard.this, ProductList.class);
                startActivity(productIntent);
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

    private void fetchArticles() {
        databaseArticles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articleList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Article article = dataSnapshot.getValue(Article.class);
                    articleList.add(article);
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Failed to load articles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

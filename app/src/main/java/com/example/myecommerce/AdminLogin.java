package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private Button btnAdminLogin;
    private FirebaseAuth auth;
    private DatabaseReference databasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        // Initialization for Firebase authentication and database
        auth = FirebaseAuth.getInstance();
        databasereference = FirebaseDatabase.getInstance().getReference("Admins");

        // Login button on click listener
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmail.getText().toString().trim();
                String pass = adminPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate user
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(AdminLogin.this, task -> {
                            if (task.isSuccessful()) {
                                // Check if the user is an admin
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    databasereference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                // User is an admin, redirect to AdminDashboardActivity
                                                startActivity(new Intent(AdminLogin.this, AdminDashboardActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(AdminLogin.this, "Access Denied! Not an Admin.", Toast.LENGTH_SHORT).show();
                                                auth.signOut();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(AdminLogin.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(AdminLogin.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

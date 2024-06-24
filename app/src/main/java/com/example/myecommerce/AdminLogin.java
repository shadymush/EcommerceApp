package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmail.getText().toString().trim();
                String password = adminPassword.getText().toString().trim();

                loginAdmin(email, password);
            }
        });
    }

    private void loginAdmin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkIfAdmin(user.getEmail());
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(AdminLogin.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfAdmin(String email) {
        mDatabase.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = false;
                for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                    String adminEmail = adminSnapshot.child("email").getValue(String.class);
                    if (adminEmail != null && adminEmail.equals(email)) {
                        isAdmin = true;
                        break;
                    }
                }

                if (isAdmin) {
                    // User is an admin, proceed to admin dashboard
                    Intent adminIntent = new Intent(AdminLogin.this, AdminDashboardActivity.class);
                    startActivity(adminIntent);
                    finish();
                } else {
                    // User is not an admin
                    Toast.makeText(AdminLogin.this, "You do not have admin privileges.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminLogin.this, "Failed to check admin data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

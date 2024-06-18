package com.example.myecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private EditText name, email, phone, password, confirmPassword;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = name.getText().toString().trim();
                String emailInput = email.getText().toString().trim();
                String phoneInput = phone.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                String confirmPasswordInput = confirmPassword.getText().toString().trim();

                if (nameInput.isEmpty() || emailInput.isEmpty() || phoneInput.isEmpty() ||
                        passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordInput.equals(confirmPasswordInput)) {
                    Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(nameInput, emailInput, phoneInput, passwordInput);
                }
            }
        });
    }

    private void registerUser(final String name, final String email, final String phone, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Store additional user information in Firebase Database
                            User userProfile = new User(name, email, phone);
                            mDatabase.child("users").child(user.getUid()).setValue(userProfile)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(Signup.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Signup.this, Login.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Signup.this, "Failed to store user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class User {
        public String name;
        public String email;
        public String phone;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }
}

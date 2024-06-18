package com.example.myecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //firebase authentication
    private FirebaseAuth mAuth;

    private EditText email, password;
    private Button btnLogin, btnSignup, btnAdmin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignUp);
        btnAdmin = findViewById(R.id.btnAdmin);

        //initialization of firebase
        mAuth = FirebaseAuth.getInstance();

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emailInput = email.getText().toString().trim();
//                String passwordInput = password.getText().toString().trim();
//
//                if (emailInput.isEmpty() || passwordInput.isEmpty()) {
//                    Toast.makeText(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
//                }
//                else {
//
//                    Intent dashboard = new Intent(Login.this, Dashboard.class);
//                    startActivity(dashboard);
//                    finish();
//                }
//            }
//        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();

                if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Firebase authentication logic
                    mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener(Login.this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, "Authentication successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent dashboard = new Intent(Login.this, Dashboard.class);
                                    startActivity(dashboard);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupPage = new Intent(Login.this, Signup.class);
                startActivity(signupPage);
                finish();
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminsIntent = new Intent(Login.this, AdminLogin.class);
                startActivity(adminsIntent);
            }
        });




    }
}
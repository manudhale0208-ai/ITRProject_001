package com.example.itrproject_001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itrproject_001.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    DBHelper dbHelper;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load login layout
        super.setContentView(R.layout.activity_login);

        // Connect XML views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Database
        dbHelper = new DBHelper(this);

        // SharedPreferences
        sharedPreferences = getSharedPreferences(
                "UserSession",
                MODE_PRIVATE
        );

        // Login button
        btnLogin.setOnClickListener(
                v -> loginUser()
        );

        // Register
        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }


    private void loginUser() {

        String email = etEmail
                .getText()
                .toString()
                .trim();

        String password = etPassword
                .getText()
                .toString()
                .trim();


        // Check email
        if (email.isEmpty()) {

            etEmail.setError(
                    "Enter your email"
            );

            etEmail.requestFocus();

            return;
        }


        // Check password
        if (password.isEmpty()) {

            etPassword.setError(
                    "Enter your password"
            );

            etPassword.requestFocus();

            return;
        }


        // Check database
        boolean result =
                dbHelper.checkUser(
                        email,
                        password
                );


        if (result) {

            // --------------------------------
            // SAVE LOGIN SESSION
            // --------------------------------

            sharedPreferences
                    .edit()
                    .putString(
                            "email",
                            email
                    )
                    .apply();


            // Login successful
            Toast.makeText(
                    this,
                    "Login successful!",
                    Toast.LENGTH_SHORT
            ).show();


            // Open MainActivity
            Intent intent = new Intent(
                    LoginActivity.this,
                    MainActivity.class
            );


            // Send email also
            intent.putExtra(
                    "userEmail",
                    email
            );


            startActivity(intent);

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
package com.example.itrproject_001;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itrproject_001.database.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvLogin;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword =
                etConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter your name");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Create a password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must contain at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        boolean inserted =
                dbHelper.insertUser(name, email, password);

        if (inserted) {

            Toast.makeText(
                    this,
                    "Registration successful!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Email already registered",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
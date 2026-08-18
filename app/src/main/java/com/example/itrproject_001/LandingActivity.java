package com.example.itrproject_001;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LandingActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LandingActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }
}
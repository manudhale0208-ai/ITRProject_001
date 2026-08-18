package com.example.itrproject_001;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchNotifications;
    Switch switchDarkMode;

    Button btnAbout;
    Button btnSettingsLogout;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(
                R.id.switchNotifications
        );

        switchDarkMode = findViewById(
                R.id.switchDarkMode
        );

        btnAbout = findViewById(
                R.id.btnAbout
        );

        btnSettingsLogout = findViewById(
                R.id.btnSettingsLogout
        );

        // Settings storage
        preferences = getSharedPreferences(
                "Settings",
                MODE_PRIVATE
        );

        loadSettings();

        // Notifications
        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "notifications",
                                    isChecked
                            )
                            .apply();

                    Toast.makeText(
                            SettingsActivity.this,
                            isChecked
                                    ? "Notifications enabled"
                                    : "Notifications disabled",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        // Dark Mode
        switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "darkMode",
                                    isChecked
                            )
                            .apply();

                    Toast.makeText(
                            SettingsActivity.this,
                            isChecked
                                    ? "Dark mode enabled"
                                    : "Dark mode disabled",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        // About
        btnAbout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    AboutActivity.class
            );

            startActivity(intent);
        });

        // Logout
        btnSettingsLogout.setOnClickListener(v -> {

            SharedPreferences userData =
                    getSharedPreferences(
                            "UserData",
                            MODE_PRIVATE
                    );

            userData.edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(
                    SettingsActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }

    private void loadSettings() {

        boolean notifications =
                preferences.getBoolean(
                        "notifications",
                        true
                );

        boolean darkMode =
                preferences.getBoolean(
                        "darkMode",
                        false
                );

        switchNotifications.setChecked(
                notifications
        );

        switchDarkMode.setChecked(
                darkMode
        );
    }
}
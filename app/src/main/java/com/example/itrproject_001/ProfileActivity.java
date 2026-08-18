package com.example.itrproject_001;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itrproject_001.database.DBHelper;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileName;

    TextView tvProfileEmail;

    Button btnEditProfile;

    DBHelper dbHelper;

    SharedPreferences sharedPreferences;

    String currentEmail;
    private ImageButton btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        btnSettings = findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

        // --------------------------------
        // Find views
        // --------------------------------

        tvProfileName = findViewById(R.id.tvProfileName);

        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        btnEditProfile = findViewById(R.id.btnEditProfile);


        // --------------------------------
        // Database
        // --------------------------------

        dbHelper = new DBHelper(this);


        // --------------------------------
        // SharedPreferences
        // --------------------------------

        sharedPreferences =
                getSharedPreferences(
                        "UserSession",
                        MODE_PRIVATE
                );


        // --------------------------------
        // Get logged-in user's email
        // --------------------------------

        currentEmail =
                sharedPreferences.getString(
                        "email",
                        ""
                );


        // --------------------------------
        // Load profile
        // --------------------------------

        loadProfile();


        // --------------------------------
        // Edit Profile button
        // --------------------------------

        btnEditProfile.setOnClickListener(
                v -> showEditProfileDialog()
        );
    }


    // ==========================================
    // LOAD PROFILE
    // ==========================================

    private void loadProfile() {

        if (currentEmail.isEmpty()) {

            Toast.makeText(
                    this,
                    "User information not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String name =
                dbHelper.getUserName(
                        currentEmail
                );


        tvProfileName.setText(name);

        tvProfileEmail.setText(currentEmail);
    }


    // ==========================================
    // EDIT PROFILE DIALOG
    // ==========================================

    private void showEditProfileDialog() {

        // Create layout
        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                40,
                20,
                40,
                10
        );


        // --------------------------------
        // Name EditText
        // --------------------------------

        EditText etName =
                new EditText(this);

        etName.setHint(
                "Enter your name"
        );


        // --------------------------------
        // Email EditText
        // --------------------------------

        EditText etEmail =
                new EditText(this);

        etEmail.setHint(
                "Enter your email"
        );

        etEmail.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );


        // --------------------------------
        // Get current data
        // --------------------------------

        String oldEmail =
                currentEmail;

        String oldName =
                dbHelper.getUserName(
                        oldEmail
                );


        // Put current data inside fields
        etName.setText(oldName);

        etEmail.setText(oldEmail);


        // --------------------------------
        // Add fields
        // --------------------------------

        layout.addView(etName);

        layout.addView(etEmail);


        // --------------------------------
        // Create dialog
        // --------------------------------

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Edit Profile")
                        .setView(layout)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Save",
                                null
                        )
                        .create();


        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String newName =
                                        etName.getText()
                                                .toString()
                                                .trim();


                                String newEmail =
                                        etEmail.getText()
                                                .toString()
                                                .trim();


                                // -------------------------
                                // Validation
                                // -------------------------

                                if (newName.isEmpty()) {

                                    etName.setError(
                                            "Enter your name"
                                    );

                                    return;
                                }


                                if (newEmail.isEmpty()) {

                                    etEmail.setError(
                                            "Enter your email"
                                    );

                                    return;
                                }


                                // -------------------------
                                // Update database
                                // -------------------------

                                boolean result =
                                        dbHelper.updateUser(
                                                oldEmail,
                                                newName,
                                                newEmail
                                        );


                                if (result) {

                                    // -------------------------
                                    // Update session email
                                    // -------------------------

                                    sharedPreferences
                                            .edit()
                                            .putString(
                                                    "email",
                                                    newEmail
                                            )
                                            .apply();


                                    // Update current email
                                    currentEmail =
                                            newEmail;


                                    // -------------------------
                                    // Update screen
                                    // -------------------------

                                    tvProfileName.setText(
                                            newName
                                    );

                                    tvProfileEmail.setText(
                                            newEmail
                                    );


                                    Toast.makeText(
                                            ProfileActivity.this,
                                            "Profile updated successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();


                                    dialog.dismiss();

                                } else {

                                    Toast.makeText(
                                            ProfileActivity.this,
                                            "Unable to update profile",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                    );
                }
        );


        dialog.show();
    }
}
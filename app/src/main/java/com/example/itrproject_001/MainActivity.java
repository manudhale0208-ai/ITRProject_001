package com.example.itrproject_001;

import com.example.itrproject_001.model.BookResponse;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itrproject_001.adapter.BookAdapter;
import com.example.itrproject_001.network.Api_Client;
import com.example.itrproject_001.network.Api_Service;
import com.example.itrproject_001.model.Doc;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;

    private MaterialButton btnSearch;

    private RecyclerView recyclerBooks;

    private BookAdapter bookAdapter;

    private List<Doc> bookList = new ArrayList<>();

    private TextView tvWelcome;
    private TextView tvResult;

    private ImageView btnProfile;
    private ImageView btnProfileBottom;
    private ImageView btnAI;
    private ImageView btnFavourite;

    private Api_Service apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeViews();

        setupRecyclerView();

        setupApi();

        setupListeners();

        loadPopularBooks();
    }

    private void initializeViews() {

        etSearch =
                findViewById(R.id.etSearch);

        btnSearch =
                findViewById(R.id.btnSearch);

        recyclerBooks =
                findViewById(R.id.recyclerBooks);

        tvWelcome =
                findViewById(R.id.tvWelcome);

        tvResult =
                findViewById(R.id.tvResult);

        btnProfile =
                findViewById(R.id.btnProfile);

        btnProfileBottom =
                findViewById(R.id.btnProfileBottom);

        btnAI =
                findViewById(R.id.btnAI);

        btnFavourite =
                findViewById(R.id.btnFavourite);
    }

    private void setupRecyclerView() {

        recyclerBooks.setLayoutManager(
                new LinearLayoutManager(this)
        );

        bookAdapter =
                new BookAdapter(
                        this,
                        bookList
                );

        recyclerBooks.setAdapter(
                bookAdapter
        );
    }

    private void setupApi() {

        apiService =
                Api_Client
                        .getClient()
                        .create(Api_Service.class);
    }

    private void setupListeners() {

        btnSearch.setOnClickListener(v -> {

            String query =
                    etSearch
                            .getText()
                            .toString()
                            .trim();

            if (query.isEmpty()) {

                etSearch.setError(
                        "Enter a book name"
                );

                return;
            }

            searchBooks(query);

            hideKeyboard();
        });

        // Category buttons

        findViewById(R.id.categoryFiction)
                .setOnClickListener(v ->
                        searchBooks("fiction"));

        findViewById(R.id.categoryScience)
                .setOnClickListener(v ->
                        searchBooks("science"));

        findViewById(R.id.categoryTechnology)
                .setOnClickListener(v ->
                        searchBooks("technology"));

        findViewById(R.id.categoryHistory)
                .setOnClickListener(v ->
                        searchBooks("history"));

        findViewById(R.id.categoryProgramming)
                .setOnClickListener(v ->
                        searchBooks("programming"));

        // PROFILE

        btnProfile.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });

        btnProfileBottom.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });

        // AI

        btnAI.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AIAssistantActivity.class
                    );

            startActivity(intent);
        });

        // FAVOURITES

        btnFavourite.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            FavouriteActivity.class
                    );

            startActivity(intent);
        });
    }

    private void loadPopularBooks() {

        tvResult.setText(
                "✨ Recommended for you"
        );

        searchBooks("computer science");
    }

    private void searchBooks(String query) {

        tvResult.setText(
                "Searching for \"" + query + "\"..."
        );

        Call<BookResponse> call =
                apiService.searchBooks(query);

        call.enqueue(new Callback<BookResponse>() {

            @Override
            public void onResponse(
                    Call<BookResponse> call,
                    Response<BookResponse> response) {

                if (response.isSuccessful() &&
                        response.body() != null) {

                    List<Doc> results =
                            response.body().getDocs();

                    if (results != null &&
                            !results.isEmpty()) {

                        bookAdapter.updateList(
                                results
                        );

                        tvResult.setText(
                                results.size()
                                        + " books found"
                        );

                    } else {

                        bookAdapter.updateList(
                                new ArrayList<>()
                        );

                        tvResult.setText(
                                "No books found"
                        );
                    }

                } else {

                    tvResult.setText(
                            "Unable to load books"
                    );

                    Toast.makeText(
                            MainActivity.this,
                            "Server response error",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<BookResponse> call,
                    Throwable t) {

                tvResult.setText(
                        "Connection problem"
                );

                Toast.makeText(
                        MainActivity.this,
                        "Unable to connect to Open Library",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void hideKeyboard() {

        InputMethodManager manager =
                (InputMethodManager)
                        getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );

        if (manager != null) {

            manager.hideSoftInputFromWindow(
                    etSearch.getWindowToken(),
                    0
            );
        }

        etSearch.clearFocus();
    }
}
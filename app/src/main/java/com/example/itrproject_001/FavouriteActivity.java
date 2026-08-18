package com.example.itrproject_001;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerFavorites;

    private TextView tvEmpty;

    private ImageView btnBack;

    private FavoriteAdapter adapter;

    private FavoriteManager favoriteManager;

    private List<FavoriteBook> favoriteBooks =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_favourite
        );

        initializeViews();

        favoriteManager =
                new FavoriteManager(this);

        setupRecyclerView();

        setupListeners();
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadFavorites();
    }

    private void initializeViews() {

        recyclerFavorites =
                findViewById(
                        R.id.recyclerFavorites
                );

        tvEmpty =
                findViewById(
                        R.id.tvEmptyFavorites
                );

        btnBack =
                findViewById(
                        R.id.btnBackFavorites
                );
    }

    private void setupRecyclerView() {

        recyclerFavorites.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter =
                new FavoriteAdapter(
                        this,
                        favoriteBooks
                );

        recyclerFavorites.setAdapter(
                adapter
        );
    }

    private void loadFavorites() {

        List<FavoriteBook> saved =
                favoriteManager.getFavorites();

        adapter.updateList(saved);

        if (saved.isEmpty()) {

            tvEmpty.setVisibility(
                    View.VISIBLE
            );

            recyclerFavorites.setVisibility(
                    View.GONE
            );

        } else {

            tvEmpty.setVisibility(
                    View.GONE
            );

            recyclerFavorites.setVisibility(
                    View.VISIBLE
            );
        }
    }

    private void setupListeners() {

        btnBack.setOnClickListener(v ->
                finish()
        );
    }
}
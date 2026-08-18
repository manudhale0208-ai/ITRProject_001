package com.example.itrproject_001;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class BookDetailsActivity extends AppCompatActivity {

    private ImageView ivBookCover;
    private ImageButton btnBack;

    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private TextView tvBookYear;
    private TextView tvBookEditions;
    private TextView tvBookLanguage;

    private Button btnPreview;
    private Button btnRecommendations;

    private MaterialButton btnFavourite;

    private FavoriteManager favoriteManager;

    private String title;
    private String author;
    private int coverId;
    private int year;
    private String key;

    private boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // IMPORTANT:
        // First load the XML layout
        setContentView(R.layout.activity_book_details);


        // Then find the buttons/views
        btnPreview = findViewById(R.id.btnPreview);

        btnRecommendations =
                findViewById(R.id.btnRecommendations);


        // Initialize all other views
        initializeViews();


        // Initialize FavoriteManager
        favoriteManager =
                new FavoriteManager(this);


        // Get book information
        getBookData();


        // Display book information
        displayBook();


        // Setup button clicks
        setupListeners();
    }


    // =====================================================
    // INITIALIZE VIEWS
    // =====================================================

    private void initializeViews() {

        ivBookCover =
                findViewById(R.id.ivBookCover);

        btnBack =
                findViewById(R.id.btnBack);

        tvBookTitle =
                findViewById(R.id.tvBookTitle);

        tvBookAuthor =
                findViewById(R.id.tvBookAuthor);

        tvBookYear =
                findViewById(R.id.tvBookYear);

        tvBookEditions =
                findViewById(R.id.tvBookEditions);

        tvBookLanguage =
                findViewById(R.id.tvBookLanguage);

        btnFavourite =
                findViewById(R.id.btnFavourite);
    }


    // =====================================================
    // GET BOOK DATA
    // =====================================================

    private void getBookData() {

        title =
                getIntent().getStringExtra("title");

        author =
                getIntent().getStringExtra("author");

        coverId =
                getIntent().getIntExtra(
                        "coverId",
                        0
                );

        year =
                getIntent().getIntExtra(
                        "year",
                        0
                );

        key =
                getIntent().getStringExtra("key");


        // Check title
        if (title == null || title.isEmpty()) {

            title = "Unknown Title";
        }


        // Check author
        if (author == null || author.isEmpty()) {

            author = "Unknown Author";
        }


        // If key is not available
        if (key == null || key.isEmpty()) {

            key =
                    title + "_" + author;
        }
    }


    // =====================================================
    // DISPLAY BOOK
    // =====================================================

    private void displayBook() {

        // Title
        tvBookTitle.setText(title);


        // Author
        tvBookAuthor.setText(
                "By " + author
        );


        // Year
        if (year != 0) {

            tvBookYear.setText(
                    "Published: " + year
            );

        } else {

            tvBookYear.setText(
                    "Published: N/A"
            );
        }


        // Book information
        tvBookEditions.setText(
                "📚 Available on Open Library"
        );

        tvBookLanguage.setText(
                "🌐 Multiple languages may be available"
        );


        // Book cover
        if (coverId != 0) {

            String imageUrl =
                    "https://covers.openlibrary.org/b/id/"
                            + coverId
                            + "-L.jpg";


            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(
                            R.drawable.book_placeholder
                    )
                    .error(
                            R.drawable.book_placeholder
                    )
                    .into(ivBookCover);

        } else {

            ivBookCover.setImageResource(
                    R.drawable.book_placeholder
            );
        }


        // Create FavoriteBook
        FavoriteBook book =
                createFavoriteBook();


        // Check favorite
        isFavorite =
                favoriteManager.isFavorite(book);


        // Update favorite button
        updateFavouriteButton();
    }


    // =====================================================
    // CREATE FAVORITE BOOK
    // =====================================================

    private FavoriteBook createFavoriteBook() {

        return new FavoriteBook(
                title,
                author,
                coverId,
                year,
                key
        );
    }


    // =====================================================
    // BUTTON LISTENERS
    // =====================================================

    private void setupListeners() {


        // -----------------------------------------------
        // BACK BUTTON
        // -----------------------------------------------

        btnBack.setOnClickListener(v -> {

            finish();

        });


        // -----------------------------------------------
        // PREVIEW BOOK
        // -----------------------------------------------

        btnPreview.setOnClickListener(v -> {

            openBookOnOpenLibrary();

        });


        // -----------------------------------------------
        // RECOMMENDED BOOKS
        // -----------------------------------------------

        btnRecommendations.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            BookDetailsActivity.this,
                            AIAssistantActivity.class
                    );


            intent.putExtra(
                    "recommendationBook",
                    title
            );


            startActivity(intent);

        });


        // -----------------------------------------------
        // FAVOURITE
        // -----------------------------------------------

        btnFavourite.setOnClickListener(v -> {

            FavoriteBook book =
                    createFavoriteBook();


            if (isFavorite) {

                // Remove favourite
                favoriteManager.removeFavorite(
                        key
                );

                isFavorite = false;


                Toast.makeText(
                        this,
                        "Removed from favourites",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                // Add favourite
                favoriteManager.addFavorite(
                        book
                );

                isFavorite = true;


                Toast.makeText(
                        this,
                        "Added to favourites ❤️",
                        Toast.LENGTH_SHORT
                ).show();
            }


            updateFavouriteButton();
        });
    }


    // =====================================================
    // OPEN BOOK ON OPEN LIBRARY
    // =====================================================

    private void openBookOnOpenLibrary() {

        if (title == null || title.isEmpty()) {

            Toast.makeText(
                    this,
                    "Book title not available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        try {

            // Create Open Library search URL
            String url =
                    "https://openlibrary.org/search?q="
                            + Uri.encode(title);


            // Open browser
            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                    );


            startActivity(intent);


        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to open Open Library",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =====================================================
    // UPDATE FAVOURITE BUTTON
    // =====================================================

    private void updateFavouriteButton() {

        if (isFavorite) {

            btnFavourite.setText(
                    "♥  Remove from Favourites"
            );

        } else {

            btnFavourite.setText(
                    "♡  Add to Favourites"
            );
        }
    }
}
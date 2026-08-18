package com.example.itrproject_001.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itrproject_001.BookDetailsActivity;
import com.example.itrproject_001.FavoriteBook;
import com.example.itrproject_001.FavoriteManager;
import com.example.itrproject_001.R;
import com.example.itrproject_001.model.Doc;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter
        extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private final List<Doc> bookList;

    private final FavoriteManager favoriteManager;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BookAdapter(
            Context context,
            List<Doc> bookList) {

        this.context = context;

        this.bookList =
                bookList != null
                        ? bookList
                        : new ArrayList<>();

        favoriteManager =
                new FavoriteManager(context);
    }


    // =========================================================
    // CREATE VIEW HOLDER
    // =========================================================

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_book,
                                parent,
                                false
                        );

        return new BookViewHolder(view);
    }


    // =========================================================
    // BIND DATA
    // =========================================================

    @Override
    public void onBindViewHolder(
            @NonNull BookViewHolder holder,
            int position) {

        Doc book =
                bookList.get(position);


        // =====================================================
        // TITLE
        // =====================================================

        String title =
                book.getTitle();

        if (title == null ||
                title.trim().isEmpty()) {

            title = "Unknown Title";
        }

        holder.tvTitle.setText(title);

        /*
         * Because title may have been changed above,
         * create a final value for the click listener.
         */
        final String finalTitle = title;


        // =====================================================
        // AUTHOR
        // =====================================================

        String author =
                "Unknown Author";

        if (book.getAuthor_name() != null &&
                !book.getAuthor_name().isEmpty() &&
                book.getAuthor_name().get(0) != null &&
                !book.getAuthor_name().get(0).trim().isEmpty()) {

            author =
                    book.getAuthor_name().get(0);
        }

        holder.tvAuthor.setText(author);

        final String finalAuthor = author;


        // =====================================================
        // YEAR
        // =====================================================

        Integer year =
                book.getFirst_publish_year();

        if (year != null) {

            holder.tvYear.setText(
                    String.valueOf(year)
            );

        } else {

            holder.tvYear.setText(
                    "N/A"
            );
        }

        /*
         * We use a normal int when sending through Intent.
         * If year is missing, send 0.
         */
        final int finalYear =
                year != null ? year : 0;


        // =====================================================
        // COVER ID
        // =====================================================

        Integer coverId =
                book.getCover_i();

        final int finalCoverId =
                coverId != null ? coverId : 0;


        // =====================================================
        // BOOK COVER
        // =====================================================

        if (coverId != null &&
                coverId > 0) {

            String imageUrl =
                    "https://covers.openlibrary.org/b/id/"
                            + coverId
                            + "-M.jpg";

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(
                            R.drawable.book_placeholder
                    )
                    .error(
                            R.drawable.book_placeholder
                    )
                    .into(holder.ivCover);

        } else {

            holder.ivCover.setImageResource(
                    R.drawable.book_placeholder
            );
        }


        // =====================================================
        // BOOK KEY
        // =====================================================

        String bookKey =
                book.getKey();

        if (bookKey == null ||
                bookKey.trim().isEmpty()) {

            /*
             * Fallback key if Open Library
             * doesn't provide one.
             */
            bookKey =
                    finalTitle + "_" + finalAuthor;
        }

        final String finalBookKey =
                bookKey;


        // =====================================================
        // CHECK FAVOURITE STATUS
        // =====================================================

        FavoriteBook currentBook =
                new FavoriteBook(
                        finalTitle,
                        finalAuthor,
                        finalCoverId,
                        finalYear,
                        finalBookKey
                );

        boolean isFavorite =
                favoriteManager.isFavorite(
                        currentBook
                );

        if (isFavorite) {

            holder.ivFavourite.setImageResource(
                    R.drawable.ic_favorite
            );

        } else {

            holder.ivFavourite.setImageResource(
                    R.drawable.ic_favorite_border
            );
        }


        // =====================================================
        // CARD CLICK
        // =====================================================

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            BookDetailsActivity.class
                    );


            // TITLE

            intent.putExtra(
                    "title",
                    finalTitle
            );


            // AUTHOR

            intent.putExtra(
                    "author",
                    finalAuthor
            );


            // COVER

            intent.putExtra(
                    "coverId",
                    finalCoverId
            );


            // YEAR

            intent.putExtra(
                    "year",
                    finalYear
            );


            // OPEN LIBRARY KEY

            intent.putExtra(
                    "key",
                    finalBookKey
            );


            // OPEN BOOK DETAILS

            context.startActivity(intent);
        });


        // =====================================================
        // FAVOURITE BUTTON
        // =====================================================

        holder.ivFavourite.setOnClickListener(v -> {

            /*
             * Create the FavoriteBook object
             * using the current book.
             */

            FavoriteBook favoriteBook =
                    new FavoriteBook(
                            finalTitle,
                            finalAuthor,
                            finalCoverId,
                            finalYear,
                            finalBookKey
                    );


            // -------------------------------------------------
            // ALREADY FAVOURITE
            // -------------------------------------------------

            if (favoriteManager.isFavorite(
                    favoriteBook)) {

                favoriteManager.removeFavorite(
                        finalBookKey
                );


                holder.ivFavourite.setImageResource(
                        R.drawable.ic_favorite_border
                );


                Toast.makeText(
                        context,
                        "Removed from favourites",
                        Toast.LENGTH_SHORT
                ).show();


            }

            // -------------------------------------------------
            // NOT FAVOURITE
            // -------------------------------------------------

            else {

                favoriteManager.addFavorite(
                        favoriteBook
                );


                holder.ivFavourite.setImageResource(
                        R.drawable.ic_favorite
                );


                Toast.makeText(
                        context,
                        "Added to favourites ❤️",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    // =========================================================
    // ITEM COUNT
    // =========================================================

    @Override
    public int getItemCount() {

        return bookList.size();
    }


    // =========================================================
    // UPDATE BOOK LIST
    // =========================================================

    public void updateList(
            List<Doc> newList) {

        bookList.clear();

        if (newList != null) {

            bookList.addAll(
                    newList
            );
        }

        notifyDataSetChanged();
    }


    // =========================================================
    // VIEW HOLDER
    // =========================================================

    static class BookViewHolder
            extends RecyclerView.ViewHolder {

        ImageView ivCover;
        ImageView ivFavourite;

        TextView tvTitle;
        TextView tvAuthor;
        TextView tvYear;


        public BookViewHolder(
                @NonNull View itemView) {

            super(itemView);


            // BOOK COVER

            ivCover =
                    itemView.findViewById(
                            R.id.ivBookCover
                    );


            // FAVOURITE ICON

            ivFavourite =
                    itemView.findViewById(
                            R.id.ivFavourite
                    );


            // TITLE

            tvTitle =
                    itemView.findViewById(
                            R.id.tvBookTitle
                    );


            // AUTHOR

            tvAuthor =
                    itemView.findViewById(
                            R.id.tvBookAuthor
                    );


            // YEAR

            tvYear =
                    itemView.findViewById(
                            R.id.tvBookYear
                    );
        }
    }
}
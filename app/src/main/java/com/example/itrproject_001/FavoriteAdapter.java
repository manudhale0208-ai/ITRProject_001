package com.example.itrproject_001;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter
        extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final Context context;

    private final List<FavoriteBook> favoriteBooks;

    private final FavoriteManager favoriteManager;

    public FavoriteAdapter(
            Context context,
            List<FavoriteBook> favoriteBooks) {

        this.context = context;

        this.favoriteBooks =
                favoriteBooks != null
                        ? favoriteBooks
                        : new ArrayList<>();

        favoriteManager =
                new FavoriteManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_favourite,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        FavoriteBook book =
                favoriteBooks.get(position);

        holder.tvTitle.setText(
                book.getTitle()
        );

        holder.tvAuthor.setText(
                book.getAuthor()
        );

        if (book.getYear() != 0) {

            holder.tvYear.setText(
                    String.valueOf(
                            book.getYear()
                    )
            );

        } else {

            holder.tvYear.setText("N/A");
        }

        if (book.getCoverId() != 0) {

            String imageUrl =
                    "https://covers.openlibrary.org/b/id/"
                            + book.getCoverId()
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

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            BookDetailsActivity.class
                    );

            intent.putExtra(
                    "title",
                    book.getTitle()
            );

            intent.putExtra(
                    "author",
                    book.getAuthor()
            );

            intent.putExtra(
                    "coverId",
                    book.getCoverId()
            );

            intent.putExtra(
                    "year",
                    book.getYear()
            );

            intent.putExtra(
                    "key",
                    book.getKey()
            );

            context.startActivity(intent);
        });

        holder.ivRemove.setOnClickListener(v -> {

            favoriteManager.removeFavorite(
                    book.getKey()
            );

            int currentPosition =
                    holder.getAdapterPosition();

            if (currentPosition !=
                    RecyclerView.NO_POSITION) {

                favoriteBooks.remove(
                        currentPosition
                );

                notifyItemRemoved(
                        currentPosition
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        return favoriteBooks.size();
    }

    public void updateList(
            List<FavoriteBook> newList) {

        favoriteBooks.clear();

        if (newList != null) {

            favoriteBooks.addAll(
                    newList
            );
        }

        notifyDataSetChanged();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView ivCover;
        ImageView ivRemove;

        TextView tvTitle;
        TextView tvAuthor;
        TextView tvYear;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            ivCover =
                    itemView.findViewById(
                            R.id.ivFavouriteCover
                    );

            ivRemove =
                    itemView.findViewById(
                            R.id.ivRemoveFavourite
                    );

            tvTitle =
                    itemView.findViewById(
                            R.id.tvFavouriteTitle
                    );

            tvAuthor =
                    itemView.findViewById(
                            R.id.tvFavouriteAuthor
                    );

            tvYear =
                    itemView.findViewById(
                            R.id.tvFavouriteYear
                    );
        }
    }
}
package com.example.itrproject_001;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {

    private static final String PREF_NAME =
            "OpenLibraryFavorites";

    private static final String FAVORITES_KEY =
            "favorites";

    private final SharedPreferences preferences;

    private final Gson gson;

    public FavoriteManager(Context context) {

        preferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        gson = new Gson();
    }

    public List<FavoriteBook> getFavorites() {

        String json =
                preferences.getString(
                        FAVORITES_KEY,
                        null
                );

        if (json == null || json.isEmpty()) {

            return new ArrayList<>();
        }

        Type type =
                new TypeToken<List<FavoriteBook>>() {
                }.getType();

        List<FavoriteBook> favorites =
                gson.fromJson(json, type);

        if (favorites == null) {

            return new ArrayList<>();
        }

        return favorites;
    }

    public void addFavorite(FavoriteBook book) {

        List<FavoriteBook> favorites =
                getFavorites();

        if (!isFavorite(book)) {

            favorites.add(book);

            saveFavorites(favorites);
        }
    }

    public void removeFavorite(String key) {

        List<FavoriteBook> favorites =
                getFavorites();

        for (int i = favorites.size() - 1;
             i >= 0;
             i--) {

            FavoriteBook book =
                    favorites.get(i);

            if (book.getKey() != null &&
                    book.getKey().equals(key)) {

                favorites.remove(i);
            }
        }

        saveFavorites(favorites);
    }

    public boolean isFavorite(FavoriteBook book) {

        if (book == null ||
                book.getKey() == null) {

            return false;
        }

        for (FavoriteBook savedBook :
                getFavorites()) {

            if (book.getKey().equals(
                    savedBook.getKey())) {

                return true;
            }
        }

        return false;
    }

    private void saveFavorites(
            List<FavoriteBook> favorites) {

        String json =
                gson.toJson(favorites);

        preferences.edit()
                .putString(
                        FAVORITES_KEY,
                        json
                )
                .apply();
    }
}
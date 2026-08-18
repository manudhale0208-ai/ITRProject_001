package com.example.itrproject_001;


public class FavoriteBook {

    private String title;
    private String author;
    private int coverId;
    private int year;
    private String key;

    public FavoriteBook() {
        // Required by Gson
    }

    public FavoriteBook(
            String title,
            String author,
            int coverId,
            int year,
            String key) {

        this.title = title;
        this.author = author;
        this.coverId = coverId;
        this.year = year;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCoverId() {
        return coverId;
    }

    public int getYear() {
        return year;
    }

    public String getKey() {
        return key;
    }
}
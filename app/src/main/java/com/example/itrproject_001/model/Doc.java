package com.example.itrproject_001.model;

import java.util.List;

public class Doc {

    private String title;

    private List<String> author_name;

    private Integer cover_i;

    private Integer first_publish_year;

    private Integer edition_count;

    private List<String> language;

    private String key;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthor_name() {
        return author_name;
    }

    public Integer getCover_i() {
        return cover_i;
    }

    public Integer getFirst_publish_year() {
        return first_publish_year;
    }

    public Integer getEdition_count() {
        return edition_count;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getKey() {
        return key;
    }
}
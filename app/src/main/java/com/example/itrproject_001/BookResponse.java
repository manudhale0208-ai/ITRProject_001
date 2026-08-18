package com.example.itrproject_001;

import com.example.itrproject_001.model.Doc;

import java.util.List;

public class BookResponse {

    private int numFound;

    private List<Doc> docs;

    public int getNumFound() {
        return numFound;
    }

    public List<Doc> getDocs() {
        return docs;
    }
}
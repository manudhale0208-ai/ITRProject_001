package com.example.itrproject_001.model;



import java.util.List;

public class BookResponse {

    // Number of books found
    private int numFound;

    // List of books returned by Open Library
    private List<Doc> docs;


    // Getter for numFound
    public int getNumFound() {
        return numFound;
    }


    // Setter for numFound
    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }


    // Getter for docs
    public List<Doc> getDocs() {
        return docs;
    }


    // Setter for docs
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }
}
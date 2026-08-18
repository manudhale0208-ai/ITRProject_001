package com.example.itrproject_001.network;


import com.example.itrproject_001.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api_Service {

    // SEARCH BOOKS
    @GET("search.json")
    Call<BookResponse> searchBooks(
            @Query("q") String query
    );



}
package com.example.itrproject_001.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_Client {

    private static final String BASE_URL =
            "https://openlibrary.org/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if (retrofit == null) {

            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .connectTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )
                            .readTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )
                            .writeTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )
                            .build();

            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();
        }

        return retrofit;
    }
}
package com.example.itrproject_001.network;


import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GeminiApi {

    private static final String API_KEY = "AQ.Ab8RN6IzFFd9OjoxyaRvlrwvtafhbUh9iTjJK8P-OX0dWYgIrQ";


    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public interface GeminiCallback {
        void onSuccess(String answer);
        void onError(String error);
    }

    public void askQuestion(
            String question,
            GeminiCallback callback) {

        try {

            // Create text part
            JSONObject textPart = new JSONObject();
            textPart.put("text", question);

            // Create parts array
            JSONArray parts = new JSONArray();
            parts.put(textPart);

            // Create content
            JSONObject content = new JSONObject();
            content.put("parts", parts);

            // Create contents array
            JSONArray contents = new JSONArray();
            contents.put(content);

            // Complete request JSON
            JSONObject requestJson = new JSONObject();
            requestJson.put("contents", contents);

            MediaType mediaType =
                    MediaType.parse("application/json; charset=utf-8");

            RequestBody body =
                    RequestBody.create(
                            requestJson.toString(),
                            mediaType
                    );

            Request request =
                    new Request.Builder()
                            .url(API_URL)
                            .addHeader(
                                    "x-goog-api-key",
                                    API_KEY
                            )
                            .addHeader(
                                    "Content-Type",
                                    "application/json"
                            )
                            .post(body)
                            .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(
                        Call call,
                        IOException e) {

                    new Handler(
                            Looper.getMainLooper()
                    ).post(() ->
                            callback.onError(
                                    "Network error: "
                                            + e.getMessage()
                            )
                    );
                }

                @Override
                public void onResponse(
                        Call call,
                        Response response)
                        throws IOException {

                    String responseBody =
                            response.body() != null
                                    ? response.body().string()
                                    : "";

                    if (!response.isSuccessful()) {

                        new Handler(
                                Looper.getMainLooper()
                        ).post(() ->
                                callback.onError(
                                        "API Error: "
                                                + response.code()
                                )
                        );

                        return;
                    }

                    try {

                        JSONObject jsonResponse =
                                new JSONObject(responseBody);

                        JSONArray candidates =
                                jsonResponse.getJSONArray(
                                        "candidates"
                                );

                        JSONObject candidate =
                                candidates.getJSONObject(0);

                        JSONObject responseContent =
                                candidate.getJSONObject(
                                        "content"
                                );

                        JSONArray responseParts =
                                responseContent.getJSONArray(
                                        "parts"
                                );

                        JSONObject firstPart =
                                responseParts.getJSONObject(0);

                        String answer =
                                firstPart.getString("text");

                        new Handler(
                                Looper.getMainLooper()
                        ).post(() ->
                                callback.onSuccess(answer)
                        );

                    } catch (Exception e) {

                        new Handler(
                                Looper.getMainLooper()
                        ).post(() ->
                                callback.onError(
                                        "Could not read AI response."
                                )
                        );
                    }
                }
            });

        } catch (Exception e) {

            callback.onError(
                    "Could not create request."
            );
        }
    }
}
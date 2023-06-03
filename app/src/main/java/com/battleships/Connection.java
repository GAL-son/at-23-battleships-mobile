package com.battleships;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Connection {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();

    String post(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    CompletableFuture<String> get(String url){
        CompletableFuture<String> future = new CompletableFuture<>();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    future.complete(responseBody.string());
                }
            }
        });

        return future;
    }


    JSONObject stringToJson(String response){
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    JSONArray stringToJsonArray(String response){
        try{
            return new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    RequestBody playerRequestBody(String login, String password){

        return new FormBody.Builder()
                .add("login", login)
                .add("password",password)
                .build();
    }

    RequestBody registerPlayerBody(String login, String password, String email){
        return new FormBody.Builder()
                .add("login", login)
                .add("password",password)
                .add("email",email)
                .build();
    }
}

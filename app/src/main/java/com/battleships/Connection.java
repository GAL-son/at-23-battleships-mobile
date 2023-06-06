package com.battleships;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Connection {
    public static final String serverUrl = "http://10.0.2.2:8080";
    final OkHttpClient client = new OkHttpClient();

    /**
     * Method used for POST requests
     * @param endpoint -
     * @param body - request body
     * @return String representation of json object/array
     * @throws IOException
     */
    String post(String endpoint, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl+endpoint)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Method used for GET requests
     * @param endpoint -
     * @return String representation of json object/array
     */
    protected String get(String endpoint) throws IOException, SocketTimeoutException {
        CompletableFuture<String> future = new CompletableFuture<>();
        Request request = new Request.Builder()
                .url(serverUrl + endpoint)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    future.complete(responseBody.string());
                }
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SocketTimeoutException) {
                throw (SocketTimeoutException) cause;
            } else {
                throw new IOException("Request failed", e);
            }
        }
    }


    /**
     * Method used for GET requests with parameters (/game/start request)
     * @param endpoint -
     * @param params - Map with request parameters
     * @return String representation of json object/array
     */
    String get(String endpoint, Map<String,String> params) {
        Map.Entry<String, String> entry = params.entrySet().iterator().next();
        CompletableFuture<String> future = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(serverUrl + endpoint + "/?" + entry.getKey()+"="+entry.getValue())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    future.complete(responseBody.string());
                }
            }
        });
        return future.join();
    }
    /**
     * Method used for converting String to JSONObject
     * @param response - String representation of json object
     * @return JSONObject
     */
    static JSONObject stringToJson(String response){
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Method used for converting String to JSONArray
     * @param response - String representation of json array
     * @return JSONArray
     */
    static JSONArray stringToJsonArray(String response){
        try{
            return new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method creating body for logging in and logging out requests
     * @param login - user login
     * @param password - user password
     * @return request body
     */
    RequestBody playerRequestBody(String login, String password){

        return new FormBody.Builder()
                .add("login", login)
                .add("password",password)
                .build();
    }

    /**
     * Method creating body for register request
     * @param login - user login
     * @param password - user password
     * @param email - user email
     * @return request body
     */
    RequestBody registerPlayerBody(String login, String password, String email){
        return new FormBody.Builder()
                .add("login", login)
                .add("password",password)
                .add("email",email)
                .build();
    }

    /**
     * Method creating body for join and queue requests
     * @param uid - player id
     * @return request body
     */
    RequestBody searchingGameBody(int uid){
        return new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();
    }

    /**
     * Method creating body for set game request
     * @param uid - player id
     * @param shipsJsonString - String with ships positions
     * @return request body
     */
    RequestBody setGameBody(int uid, String shipsJsonString){
        return new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .add("shipsJsonString",shipsJsonString)
                .build();
    }

    /**
     * Method creating body for move requests
     * @param uid - player id
     * @param x - x value of move
     * @param y - y value of move
     * @return request body
     */
    RequestBody moveBody(int uid, int x, int y){
        return new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .add("x", String.valueOf(x))
                .add("y", String.valueOf(y))
                .build();
    }

}

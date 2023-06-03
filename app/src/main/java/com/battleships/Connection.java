package com.battleships;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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


    JSONObject stringToJson(String response){
        try {
            return new JSONObject(response);
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

package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        Button loginButton = findViewById(R.id.buttonLogin);
        Button cancelButton = findViewById(R.id.buttonCancelRegister);
        Button goToRegisterButton = findViewById(R.id.buttonCreateAccount);

        EditText login = findViewById(R.id.editTextLogin);
        EditText password = findViewById(R.id.editTextPasswordRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogged = login(login.getText().toString(), password.getText().toString());

                if(isLogged){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("login", login.getText().toString());
                    editor.apply();

                    goToMainMenu();
                    Toast.makeText( LoginActivity.this, "You have been successfully authorized", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText( LoginActivity.this, "Incorrect login or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartScreen();
            }
        });

        goToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void goToStartScreen(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private void goToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private boolean login(String login, String password){
        Connection connection = new Connection();
        RequestBody body = connection.playerRequestBody(login, password);
        AtomicBoolean isAuthorized = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.post("http://10.0.2.2:8080/api/login",body);
                JSONObject json = connection.stringToJson(response);

                Log.i("json", String.valueOf(json));
                if(json.has("status")){
                    String status = json.getString("status");
                    Log.i("status",status);
                }else{
                    isAuthorized.set(true);
                }

            }catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                synchronized (lock) {
                    lock.notify();
                }
            }
        }).start();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return isAuthorized.get();
    }
}
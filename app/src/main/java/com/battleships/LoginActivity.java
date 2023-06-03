package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.buttonLogin);
        Button cancelButton = findViewById(R.id.buttonCancelRegister);
        Button goToRegisterButton = findViewById(R.id.buttonCreateAccount);

        EditText login = findViewById(R.id.editTextLogin);
        EditText password = findViewById(R.id.editTextPasswordRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: "+password.getText());
                Log.e("passwd", String.valueOf(password.getText()));
                login(login.getText().toString(), password.getText().toString());
                goToMainMenu();
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

    private void login(String login, String password){
        Connection connection = new Connection();
        RequestBody body = connection.playerRequestBody(login, password);

        new Thread(() -> {
            try{
                String response = connection.post("http://10.0.2.2:8080/api/login",body);
                JSONObject json = connection.stringToJson(response);
                Log.i("response", response);
                Log.i("json", String.valueOf(json));
                if(json.has("status")){
                    String status = json.getString("status");
                    Log.i("status",status);
                }

            }catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
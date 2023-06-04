package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        Button buttonCancelRegister = findViewById(R.id.buttonCancelRegister);

        EditText login = findViewById(R.id.editTextLogin);
        EditText password = findViewById(R.id.editTextPassword);
        EditText passwordConfirmed = findViewById(R.id.editTextPasswordRegister);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(passwordConfirmed.getText().toString())) {
                    register(login.getText().toString(), password.getText().toString());
                    goToStartScreen();
                    Toast.makeText( RegisterActivity.this, "Your account has been created, now you can log in", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText( RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartScreen();
            }
        });
    }

    private void goToStartScreen(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private void register(String login, String password ){
        Connection connection = new Connection();
        RequestBody body = connection.registerPlayerBody(login, password, "");

        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.post("http://10.0.2.2:8080/api/register",body);
                JSONObject json = connection.stringToJson(response);

                Log.i("json", String.valueOf(json));

            }catch (IOException e) {
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
    }
}
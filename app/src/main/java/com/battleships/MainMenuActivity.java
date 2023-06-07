package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.sax.RootElement;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Button playSingle = findViewById(R.id.buttonLogin);
        Button playMulti = findViewById(R.id.buttonPlayAsGuest);
        Button logOutButton = findViewById(R.id.buttonLogOut);
        Button buttonRankings = findViewById(R.id.buttonRankings);

        playSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingShips(0);
            }
        });
        playMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLoggedUserLogin() != null) {
                    goToSettingShips(1);
                    Log.i("userLogged",getLoggedUserLogin());
                }
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                goToLoginScreen();
                Toast.makeText(MainMenuActivity.this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRankings();
            }
        });
    }

    private void goToSettingShips(int type) {
        Intent intent = new Intent(this, SetShipsActivity.class);
        intent.putExtra("newGameType", type);
        startActivity(intent);
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private void goToRankings(){
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    private void logOut(){
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login","null");
        String password = sharedPreferences.getString("password","null");

        if(!login.equals("null")){
            Connection connection = new Connection();
            RequestBody body = connection.playerRequestBody(login, password);
            Object lock = new Object();
            new Thread(() -> {
                try{
                    String response = connection.post(Endpoints.LOGOUT.getEndpoint(),body);
                    Log.i("isLoggedOut", response);

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

        sharedPreferences.edit()
                .remove("login")
                .remove("password")
                .remove("uid")
                .apply();
    }

    private String getLoggedUserLogin() {
        SharedPreferences sharedPred = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String LogedUser = sharedPred.getString("login", null);
        return LogedUser;
    }
}
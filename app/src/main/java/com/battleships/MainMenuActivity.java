package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.RootElement;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    private void goToRankings() {
        Intent intent = new Intent(this, RankingActvity.class);
        startActivity(intent);
    }

    private void logOut(){
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .remove("login")
                .remove("uid")
                .apply();
    }

    private String getLoggedUserLogin() {
        SharedPreferences sharedPred = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String LogedUser = sharedPred.getString("login", null);
        return LogedUser;
    }
}
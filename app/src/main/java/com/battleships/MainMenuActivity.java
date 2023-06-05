package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Button playSingle = findViewById(R.id.buttonLogin);
        Button logOutButton = findViewById(R.id.buttonLogOut);
        Button buttonRankings = findViewById(R.id.buttonRankings);

        playSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingShips();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                goToLoginScreen();
                Toast.makeText( MainMenuActivity.this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRankings();
            }
        });
    }

    private void goToSettingShips(){
        Intent intent = new Intent(this, SetShipsActivity.class);
        startActivity(intent);
    }

    private void goToLoginScreen(){
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
        sharedPreferences.edit()
                .remove("login")
                .remove("uid")
                .apply();
    }
}
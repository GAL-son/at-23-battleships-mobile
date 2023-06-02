package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        playSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingShips();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginScreen();
                Toast.makeText( MainMenuActivity.this, "You have been successfully logged out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToSettingShips(){
        Intent intent = new Intent(this, SetShipsActivity.class);
        startActivity(intent);
    }

    private void goToLoginScreen(){
        Intent intent = new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
    }
}
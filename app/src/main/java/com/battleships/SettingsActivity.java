package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        Button buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        Button buttonGoBack = findViewById(R.id.buttonGoBack);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
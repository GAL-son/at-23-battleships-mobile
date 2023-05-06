package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button playSingle = findViewById(R.id.buttonPlaySingle);

        playSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingShips();
            }
        });
    }

    private void goToSettingShips(){
        Intent intent = new Intent(this, SetShipsActivity.class);
        startActivity(intent);
    }
}
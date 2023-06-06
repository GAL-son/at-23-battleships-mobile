package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, StartScreenActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
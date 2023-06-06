package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.SocketTimeoutException;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Button loginButton = findViewById(R.id.buttonLogin);
        Button playAsGuestButton = findViewById(R.id.buttonPlayAsGuest);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isServerAvailable = isServerAvailable();
                Log.i("server", String.valueOf(isServerAvailable));
                if(isServerAvailable){
                    goToLoginActivity();
                }
            }
        });

        playAsGuestButton.setOnClickListener(new View.OnClickListener() {
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

    private void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isServerAvailable() {
        Connection connection = new Connection();
        String response;
        try {
            response = connection.get(Endpoints.SERVER.getEndpoint());
            if (response.equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText( StartScreenActivity.this, "Server is currently unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
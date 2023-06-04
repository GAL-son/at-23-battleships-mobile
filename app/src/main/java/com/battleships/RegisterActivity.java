package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        Button buttonCancelRegister = findViewById(R.id.buttonCancelRegister);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
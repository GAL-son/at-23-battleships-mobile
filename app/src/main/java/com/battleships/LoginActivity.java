package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.security.auth.login.LoginException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.buttonLogin);
        Button cancelButton = findViewById(R.id.buttonCancelRegister);
        Button goToRegisterButton = findViewById(R.id.buttonCreateAccount);

        EditText password = findViewById(R.id.editTextPasswordRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: "+password.getText());
                Log.e("passwd", String.valueOf(password.getText()));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartScreen();
            }
        });

        goToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void goToStartScreen(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private void goToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
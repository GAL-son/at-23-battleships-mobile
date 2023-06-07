package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String loginsp = sharedPreferences.getString("login", "null");
        String passwordsp = sharedPreferences.getString("password","null");

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        Button buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        Button buttonGoBack = findViewById(R.id.buttonGoBack);


        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingsActivity.this);
                dialog.setContentView(R.layout.dialog_reset_password);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                Button buttonConfirmChangePassword = dialog.findViewById(R.id.buttonConfirmChangePassword);
                Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

                dialog.show();
                buttonConfirmChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText oldPassword = dialog.findViewById(R.id.editTextOldPassword);
                        EditText newPassword = dialog.findViewById(R.id.editTextNewPassword);

                        if(passwordsp.equals(oldPassword.getText().toString())){
                            changePassword(loginsp,oldPassword.getText().toString(), newPassword.getText().toString());
                            dialog.dismiss();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password",newPassword.getText().toString());
                            editor.apply();
                            Toast.makeText( SettingsActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            Toast.makeText( SettingsActivity.this, "Incorrect old password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingsActivity.this);
                dialog.setContentView(R.layout.dialog_delete_account);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                Button buttonConfirmDelete = dialog.findViewById(R.id.buttonConfirmDelete);
                Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

                dialog.show();
                buttonConfirmDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = sharedPreferences.getString("password", "null");
                        EditText confirmPassword = dialog.findViewById(R.id.editTextConfirmPassword);
                        if(password.equals(confirmPassword.getText().toString())){
                            deleteAccount(loginsp,password);
                            dialog.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            goToStartScreen();
                            Toast.makeText( SettingsActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            Toast.makeText( SettingsActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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

    private void goToStartScreen(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    private void deleteAccount(String login, String password){

        Connection connection = new Connection();
        RequestBody body = connection.playerRequestBody(login, password);
        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.post(Endpoints.DELETE_ACCOUNT.getEndpoint(),body);
                Log.i("isDeleted", response);

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

    private void changePassword(String login, String oldPassword, String newPassword){

        Connection connection = new Connection();
        RequestBody body = connection.changePassword(login, oldPassword, newPassword);
        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.post(Endpoints.CHANGE_PASSWORD.getEndpoint(),body);
                Log.i("isChanged", response);

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
}
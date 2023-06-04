package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class RankingActvity extends AppCompatActivity {

    ArrayList<JSONObject> rankingInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_actvity);

        Button buttonGoBack = findViewById(R.id.buttonGoBackToMainMenu);

        setUpRankingModels();
        for(JSONObject o : rankingInfo){
            try {
                Log.i("asd", String.valueOf(o.get("login")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    private void setUpRankingModels(){
        Connection connection = new Connection();
        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.get("http://10.0.2.2:8080/api/ranking");
                JSONArray json = connection.stringToJsonArray(response);

                for(int i = 0; i < json.length(); i++){
                    rankingInfo.add(json.getJSONObject(i));
                }

                rankingInfo.sort((o1, o2) -> {
                    Double score1 = 0.0;
                    Double score2 = 0.0;
                    try {
                        score1 = (Double) o1.get("gammerScore");
                        score2 = (Double) o2.get("gammerScore");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return score1.compareTo(score2);
                });
            } catch (JSONException e) {
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
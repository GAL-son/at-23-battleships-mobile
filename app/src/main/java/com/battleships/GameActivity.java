package com.battleships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    private boolean isOurBoardDisplayed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Button changeBoard = findViewById(R.id.buttonChangeBoard);

        changeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (isOurBoardDisplayed) {
                    OurBoardFragment ourBoardFragment = new OurBoardFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, ourBoardFragment);
                    isOurBoardDisplayed = false;
                } else {
                    EnemyBoardFragment opponentBoardFragment = new EnemyBoardFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, opponentBoardFragment);
                    isOurBoardDisplayed = true;
                }

                fragmentTransaction.commit();
            }
        });
    }

    public Integer[] getFieldId(int pos){
        Integer[] posId = new Integer[2];
        if (pos < 9){
            posId[0] = pos;
            posId[1] = 0;
        }else{
            posId[0]=pos/10;
            posId[1]=pos%10;
        }
        return posId;
    }
}
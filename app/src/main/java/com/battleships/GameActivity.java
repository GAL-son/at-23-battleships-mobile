package com.battleships;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private boolean isOurBoardDisplayed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Button changeBoard = findViewById(R.id.buttonChangeBoard);
        Button exitGame = findViewById(R.id.buttonExitGame);

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

        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                goBackToMainMenu();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

                builder.setMessage("Are you sure you want to exit game? You won't be able to come back")
                        .setPositiveButton("Yes i want to leave",dialogClickListener)
                        .setNegativeButton("No i want to continue game", dialogClickListener)
                        .show();
            }
        });
    }

    private void goBackToMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
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
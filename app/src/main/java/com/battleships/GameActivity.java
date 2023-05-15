package com.battleships;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.battleships.model.client.Game;
import com.battleships.model.client.board.Field;

public class GameActivity extends AppCompatActivity {

    private boolean isOurBoardDisplayed = true;
    Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        //odebranie obiektu game z setShipsActivity
        try {
           game = (Game) getIntent().getSerializableExtra("game");
            Log.i("GameActivity", "game transferred");
        }catch (Exception e)
        {
            Log.i("GameActivity", e.getMessage());
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //to jest gówno rozwiązujące problemy, z tym ze to jesszce nie działa edit: teraz działa
        FragmentManager fragmentManagerPom = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionPom = fragmentManagerPom.beginTransaction();
        OurBoardFragment ourBoardFragmentPom = new OurBoardFragment();
        //ourBoardFragmentPom.setGame(game);
        getIntent().putExtra("game",game);
        fragmentTransactionPom.replace(R.id.fragmentContainerView, ourBoardFragmentPom);
        isOurBoardDisplayed = false;
        fragmentTransactionPom.commit();
        //koniec gówna


//        String test="";
//
//        for(int x=0;x<=9;x++)
//        {
//            for(int y=0;y<=9;y++)
//            {
//                if (((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied())
//                {
//                    test+="X";
//                }
//                else
//                {
//                    test+="O";
//                }
//            }
//            Log.i("field", test);
//            test="";
//        }
        Button changeBoard = findViewById(R.id.buttonChangeBoard);
        Button exitGame = findViewById(R.id.buttonExitGame);



        changeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (isOurBoardDisplayed) {
                    OurBoardFragment ourBoardFragment = new OurBoardFragment();
                        Log.i("transfer do fragmenty", "fragment otrzymał dane");
                      // ourBoardFragment.setGame(game);
                    getIntent().putExtra("game",game);

                    fragmentTransaction.replace(R.id.fragmentContainerView, ourBoardFragment);
                    isOurBoardDisplayed = false;
                } else {
                    EnemyBoardFragment opponentBoardFragment = new EnemyBoardFragment();
                    //opponentBoardFragment.setGame(game);
                    getIntent().putExtra("game",game);
                    fragmentTransaction.replace(R.id.fragmentContainerView, opponentBoardFragment);
                    isOurBoardDisplayed = true;
                }
                fragmentTransaction.commit();
            }
        });

        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(GameActivity.this);
                dialog.setContentView(R.layout.dialog_abort_game);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                Button buttonAbortGame = dialog.findViewById(R.id.buttonAbortGame);
                Button buttonContinueGame = dialog.findViewById(R.id.buttonContinueGame);

                dialog.show();
                buttonAbortGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });

                buttonContinueGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public static Integer[] getFieldId(int pos){
        Integer[] posId = new Integer[2];
        if (pos < 9){
            posId[0] = pos;
            posId[1] = 0;
        }else{
            posId[0]=pos%10;
            posId[1]=pos/10;
        }
        return posId;
    }
}
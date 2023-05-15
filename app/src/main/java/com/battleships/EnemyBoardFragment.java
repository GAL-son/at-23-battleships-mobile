package com.battleships;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.Move;
import com.battleships.model.client.board.Field;
import com.battleships.model.client.players.PlayerAi;
import com.battleships.model.client.ship.Ship;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnemyBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnemyBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Integer player1HP = 0;
    private Integer player2HP = 0;

    Game game;
//    public void setGame(Game game_)
//    {
//        game=game_;
//    }

    public EnemyBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnemyBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnemyBoardFragment newInstance(String param1, String param2) {
        EnemyBoardFragment fragment = new EnemyBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void drawBoardGameLoopEnemy(View rootView) {

        TextView turn = getActivity().findViewById(R.id.textViewTurn);
        if (game.getState() != 2)
            turn.setText("Turn:" + game.getTurn());
        else{
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_game_won);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            Button buttonExitToMainMenu = dialog.findViewById(R.id.buttonExitToMainMenu);
            Button buttonPlayAgain = dialog.findViewById(R.id.buttonPlayAgain);

            dialog.show();
            buttonExitToMainMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                    startActivity(intent);
                }
            });

            buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SetShipsActivity.class);
                    startActivity(intent);
                }
            });

        }

        ImageView pom;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).getWasHit() == true) {
                    pom = rootView.findViewById(10 * x + y);
                    pom.setImageResource(R.drawable.field_without_ship);
                    if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).isOccupied()) {
                        pom = rootView.findViewById(10 * x + y);

                        pom.setImageResource(R.drawable.enemy_ship_hit);
                    }
                }
            }
        }
    }


    private void hitingProcedure(Move move, int player) {

        countHP();
        TextView turn = getActivity().findViewById(R.id.textViewTurn);
        game.hitField(move, player);
        Log.i("hiting", "field hit" + String.valueOf(move.positionX) + ", " + String.valueOf(move.positionY));
        game.nextTurn();
        turn.setText("Turn:" + game.getTurn());
        countHP();


    }

    private void countHP() {
        Integer pom1 = 0, pom2 = 0;
        for (Ship s : game.getPlayer1().ships) {
            pom1 += s.getHealth();
        }
        for (Ship s : game.getPlayer2().ships) {
            pom2 += s.getHealth();
        }
        Log.i("countHP", "countHP: " + "hp p1= " + pom1 + " p2 = " + pom2);
        if (pom1 == 0) {
            GameEndProcedure(2);
        }
        if (pom2 == 0) {
            GameEndProcedure(1);
        }
    }

    private void GameEndProcedure(int winner) {
        game.setState(2);
        Log.i("koniec", "gre wygrał gracz: " + winner + "zajeło mu to " + game.getTurnFull());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        game = (Game) (intent.getSerializableExtra("game"));
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_our_board, container, false);
        FrameLayout frameLayout = rootView.findViewById(R.id.frameLayout);

        // Tworzenie TableLayout
        TableLayout tableLayout = new TableLayout(getContext());
        int imageResourceField = R.drawable.field;
        int imageResourceFieldWithoutShip = R.drawable.field_without_ship;

        // Dodawanie TextView do TableLayout
        TextView textViewYourBoard = new TextView(getContext());
        textViewYourBoard.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textViewYourBoard.setText("Enemy Board");
        textViewYourBoard.setTextColor(getResources().getColor(R.color.light_green));
        textViewYourBoard.setTextSize(30);
        textViewYourBoard.setGravity(Gravity.CENTER_HORIZONTAL);
        tableLayout.addView(textViewYourBoard);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            for (int j = 0; j < 10; j++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));


                imageView.setImageResource(imageResourceField);

                int marginInPixels = 2; // W pikselach
                float density = getResources().getDisplayMetrics().density;
                int marginInDp = (int) (marginInPixels / density);

                TableRow.LayoutParams tableLayoutParams = (TableRow.LayoutParams) imageView.getLayoutParams();
                tableLayoutParams.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
                imageView.setLayoutParams(tableLayoutParams);

                // Nadanie unikalnego id dla każdego ImageView
                imageView.setId(i * 10 + j);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView turn = getActivity().findViewById(R.id.textViewTurn);


                        ImageView clickedImageView = (ImageView) v;
                        int pos = clickedImageView.getId();
                        Integer[] posId = new Integer[0];
                        if (getActivity() instanceof GameActivity) {
                            posId = ((GameActivity) getActivity()).getFieldId(pos);

                        }
                        ImageView pom;
                        //   if (game.getTurn() == 0)// intergracja z serwerem zmieni sens tej instrukcji, nazrazie zawieszona dla testów
                        //    {
                        try {
                            if (((Field) (game.getPlayer2().getPlayerBard().fields.get(posId[0]).get(posId[1]))).getWasHit() != true && game.getState() < 2) {
                                hitingProcedure(new Move(posId[0], posId[1], 0), 1);
                                drawBoardGameLoopEnemy(rootView);


                                Log.i("ai", "czy ai styrzeli?");
                                if (game.getType() == 0) {
                                    Log.i("ai", "ai strzeli");
                                    ArrayList<Integer> AImove = ((PlayerAi) (game.getPlayer2())).getAImove(game.getPlayer1().getPlayerBard());
                                    Log.i("ai", "pozyskano koordynaty");
                                    if (((Field) (game.getPlayer1().getPlayerBard().fields.get(AImove.get(0)).get(AImove.get(1)))).getWasHit() != true && game.getState() < 2) {
                                        hitingProcedure(new Move(AImove.get(0), AImove.get(1), 0), 2);
                                    } else {
                                        Log.i("aha", "ai oddalo strzal w to smao miejsce");
                                    }
                                }
                            } else {
                                if(game.getState() ==2) {
                                    Log.i("", "game alredy ended");
                                    turn.setText("game ended");
                                }

                            }
                        } catch (Exception e) {
                            Log.i("hiting", "field not hit");
                            throw new RuntimeException(e);
                        }

                        //  }//part of turn based shinanigans
                        drawBoardGameLoopEnemy(rootView);


                        // Snackbar.make(tableLayout, "Clicked on field " + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]), Snackbar.LENGTH_SHORT).show();
                    }
                });

                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
        // Dodawanie TableLayout do FrameLayout
        frameLayout.addView(tableLayout);


        //pierwsze wyrysowanie
        drawBoardGameLoopEnemy(rootView);

        return rootView;
    }


}
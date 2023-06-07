package com.battleships;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.GameStateFromServer;
import com.battleships.model.client.Move;
import com.battleships.model.client.board.Field;
import com.battleships.model.client.players.PlayerAi;
import com.battleships.model.client.ship.Ship;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OurBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OurBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Game game;

//    public void setGame(Game game_)
//    {
//        game=game_;
//    }


    public OurBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OurBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OurBoardFragment newInstance(String param1, String param2) {
        OurBoardFragment fragment = new OurBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        if(game.getType()==0) {
            if (pom1 == 0) {
                game.winner = game.getPlayer2().getId();
                GameEndProcedure(game.winner);
            }
            if (pom2 == 0) {
                game.winner = game.getPlayer1().getId();
                GameEndProcedure(game.winner);
            }
        } else if (game.getType()==1) {
            Log.i("hpPoliczoneWmulti", "countHP: "+"p1:"+pom1+" p2:"+pom2+ "czy gra skonczona?: "+game.gameStateFromServer.isFinished());


          //  if (game.gameStateFromServer.isFinished()==true)
            {

                Log.i("gameIsFinishedOnSerwe", "countHP: "+"p1:"+pom1+" p2:"+pom2);
                if (pom1 ==0) {
                    game.winner = game.getPlayer2().getId();
                    GameEndProcedure(game.winner);
                }
                if (pom2 ==0) {
                    game.winner = game.getPlayer1().getId();
                    GameEndProcedure(game.winner);
                }
            }

        }
    }

    private void GameEndProcedure(int winner) {
        game.setState(2);
        if (game.winner==game.getPlayer1().getId())
            Log.i("koniec", "gre wygrał gracz: " + 1 + "zajeło mu to " + game.getTurnFull());

        if (game.winner==game.getPlayer2().getId())
            Log.i("koniec", "gre wygrał gracz: " + 2 + "zajeło mu to " + game.getTurnFull());


        // Log.i("koniec", "gre wygrał gracz: " + game.winner + "zajeło mu to " + game.getTurnFull());

    }
    private void hitingProcedure(Move move, int player) {


        if (game.getType() == 1) {
            if (player == 1)
                game.getPlayer1().setMoove_token(false);
            if (player == 2)
                game.getPlayer1().setMoove_token(true);
        }
        countHP();
        TextView turn = getActivity().findViewById(R.id.textViewTurn);
        game.hitField(move, player);
        Log.i("hiting", "field hit" + String.valueOf(move.positionX) + ", " + String.valueOf(move.positionY));
        game.nextTurn();
        turn.setText("Turn:" + game.getTurnFull()+ ((game.getTurn()==0) ? "your turn":" enemy turn"));
        countHP();


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }




}
        private  void drawBoardGameLoopYour(View rootView)
        {
            TextView turn = getActivity().findViewById(R.id.textViewTurn);
            if (game.getState() != 2)
                turn.setText("Turn:" + game.getTurnFull() + ((game.getTurn() == 0) ? " your turn" : " enemy turn"));
            else {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_game_won);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                Button buttonExitToMainMenu = dialog.findViewById(R.id.buttonExitToMainMenu);
                Button buttonPlayAgain = dialog.findViewById(R.id.buttonPlayAgain);
                TextView winner_textView = dialog.findViewById(R.id.textViewAlert);
                if (game.winner == game.getPlayer2().getId())
                    winner_textView.setText("You lose");
                if (game.winner == game.getPlayer1().getId())
                    winner_textView.setText("You win");
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
            for(int x=0;x<10;x++)
            {
                for(int y=0;y<10;y++)
                {
                    if (((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()&&((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).getWasHit())
                    {
                        pom=rootView.findViewById(10*x+y);
                        pom.setImageResource(R.drawable.field_without_ship);
                    }
                    if (((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()&&((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).getWasHit()==false)
                    {
                        pom=rootView.findViewById(10*x+y);
                        pom.setImageResource(R.drawable.allied_ship);
                    }
                    if (((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()==false&&((Field)(game.getPlayer1().getPlayerBard().fields.get(y).get(x))).getWasHit()==true)
                    {
                        pom=rootView.findViewById(10*x+y);
                        pom.setImageResource(R.drawable.enemy_ship_hit);
                    }




                }
            }
            ArrayList<Integer> histogramp1 = Game.histogramInGame(game.getPlayer1().ships);
            ArrayList<Integer> histogramp2 = Game.histogramInGame(game.getPlayer2().ships);
            Log.i("histogramGame", "gracz1:" + histogramp1.get(0) + "," + histogramp1.get(1) + "," + histogramp1.get(2) + "," + histogramp1.get(3) + ",");
            Log.i("histogramGame", "gracz2:" + histogramp2.get(0) + "," + histogramp2.get(1) + "," + histogramp2.get(2) + "," + histogramp2.get(3) + ",");

            {
                TextView your4 = getActivity().findViewById(R.id.textViewYour4xShips);
                TextView your3 = getActivity().findViewById(R.id.textViewYour3xShips);
                TextView your2 = getActivity().findViewById(R.id.textViewYour2xShips);
                TextView your1 = getActivity().findViewById(R.id.textViewYour1xShips);

                TextView enemy4 = getActivity().findViewById(R.id.textViewEnemy4xShip);
                TextView enemy3 = getActivity().findViewById(R.id.textViewEnemy3xShip);
                TextView enemy2 = getActivity().findViewById(R.id.textViewEnemy2xShip);
                TextView enemy1 = getActivity().findViewById(R.id.textViewEnemy1xShip);

                your1.setText(histogramp1.get(0) + "x");
                your2.setText(histogramp1.get(1) + "x");
                your3.setText(histogramp1.get(2) + "x");
                your4.setText(histogramp1.get(3) + "x");

                enemy1.setText(histogramp2.get(0) + "x");
                enemy2.setText(histogramp2.get(1) + "x");
                enemy3.setText(histogramp2.get(2) + "x");
                enemy4.setText(histogramp2.get(3) + "x");
            }

        }
    private void setStateFromSever()
    {
        Connection conn =new Connection();

        Map<String,String> map= new HashMap<String,String>(){{
            put("uid",String.valueOf(game.getPlayer1().getId()));
        }};


        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = conn.get(Endpoints.GAME_STATE.getEndpoint(),map);
                Log.i("TheResponseOfStateFetch", response);
                JSONObject json = Connection.stringToJson(response);


                Log.i("the response", response);
                if (json.has("status")) {
                    Log.i("status", response);
                } else {

                    try {
                        game.gameStateFromServer= GameStateFromServer.getState(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            } finally {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent=getActivity().getIntent();
        game=(Game)(intent.getSerializableExtra("game"));
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
        textViewYourBoard.setText("Your Board");
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



                if(game.getTurn()%2==1) {
                    try {


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
                    } catch (Exception e) {

                    }
                }
                TextView ButtonChangeBoardGetAiMove=getActivity().findViewById(R.id.buttonChangeBoard);
                ButtonChangeBoardGetAiMove.setText("CHANGE BOARD AND TAKE YOUR SHOT");



                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                           ImageView clickedImageView = (ImageView) v;
                      //  clickedImageView.setImageResource(imageResourceFieldWithoutShip);
                        int pos = clickedImageView.getId();
                        Integer[] posId = new Integer[0];
                        if (getActivity() instanceof GameActivity) {
                            posId = ((GameActivity) getActivity()).getFieldId(pos);

                        }

                        drawBoardGameLoopYour(rootView);
                       // Snackbar.make(tableLayout, "Clicked on field " + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]), Snackbar.LENGTH_SHORT).show();
                    }
                });

                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);


        }

        // Dodawanie TableLayout do FrameLayout
        frameLayout.addView(tableLayout);
        if(game.getType()==1)
        {
            //gra jest w trybi muliplayer
            setStateFromSever();

            if (game.gameStateFromServer.getLastx()!=null&&game.gameStateFromServer.getTurnid()==game.getPlayer1().getId()&&game.gameStateFromServer.getLastx()!=null)
            {
                int pom1,pom2;
                pom1=game.gameStateFromServer.getLastx();
                pom2=game.gameStateFromServer.getLasty();
                if(!((Field)game.getPlayer1().getPlayerBard().fields.get(pom1).get(pom2)).getWasHit()) {
                    try {

                        hitingProcedure(new Move(game.gameStateFromServer.getLastx(), game.gameStateFromServer.getLasty(), 0), 2);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            Log.i("GameStateFromServer", game.gameStateFromServer.toString());
        }


        //pierwsze wyrysowanie
        drawBoardGameLoopYour(rootView);
        countHP();
        if(game.getTurn()==0)
        {
            if (game.getPlayer1().getId()==game.gameStateFromServer.getTurnid());
            {
                game.getPlayer1().setMoove_token(true);
            }
        }


        return rootView;
    }






}
package com.battleships;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.Move;
import com.battleships.model.client.board.Field;
import com.google.android.material.snackbar.Snackbar;

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
                            if (((Field) (game.getPlayer2().getPlayerBard().fields.get(posId[0]).get(posId[1]))).getWasHit() != true) {
                                game.hitField(new Move(posId[0], posId[1], 0), 2);
                                Log.i("hiting", "field hit" + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]));
                                game.nextTurn();
                                turn.setText("Turn:" + game.getTurn());
                            }
                        } catch (Exception e) {
                            Log.i("hiting", "field not hit");
                            throw new RuntimeException(e);
                        }

                        //  }//part of turn based shinanigans


                        for (int x = 0; x < 10; x++) {
                            for (int y = 0; y < 10; y++) {
                                if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).getWasHit() == true) {
                                    pom = rootView.findViewById(10 * x + y);
                                    pom.setImageResource(imageResourceFieldWithoutShip);
                                }
                            }
                        }
                        // Snackbar.make(tableLayout, "Clicked on field " + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]), Snackbar.LENGTH_SHORT).show();
                    }
                });

                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
        // Dodawanie TableLayout do FrameLayout
        frameLayout.addView(tableLayout);

        ImageView pom;
        TextView turn = getActivity().findViewById(R.id.textViewTurn);
        turn.setText("Turn:" + game.getTurn());
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).getWasHit() == true) {
                    pom = rootView.findViewById(10 * x + y);
                    pom.setImageResource(imageResourceFieldWithoutShip);
                }
            }
        }

        return rootView;
    }


}
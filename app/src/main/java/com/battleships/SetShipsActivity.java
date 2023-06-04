package com.battleships;

import static com.battleships.GameActivity.getFieldId;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.Move;
import com.battleships.model.client.board.Field;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.RequestBody;

public class SetShipsActivity extends AppCompatActivity {
    Game game_this;
    int align_selected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        createBoard(context);

        Button exitButton = findViewById(R.id.buttonExit);
        Button readyButton = findViewById(R.id.buttonReady);
        Button turnShips = findViewById(R.id.buttonTurnTheShip);
        Button clearBoardButton = findViewById(R.id.buttonClearBoard);
        ImageView ship1x = findViewById(R.id.imageViewShip1x);
        ImageView ship2x = findViewById(R.id.imageViewShip2x);
        ImageView ship3x = findViewById(R.id.imageViewShip3x);
        ImageView ship4x = findViewById(R.id.imageViewShip4x);

        int type_form_intent = getIntent().getIntExtra("newGameType", 0);
        Log.i("gamemode", "selectedGameType:" + type_form_intent);
        //game creation

        try {
            Game game = new Game(1, type_form_intent);
            game_this = game;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (game_this.getType() == 0) {
            try {
                vsAiProcedure();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            setLocalPlayerID();
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainMenu();
            }
        });

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game_this.getPlayer1().shipsSizes.isEmpty()) {

                    if (game_this.getType() == 1) {

                      if( joinGame())
                        Log.i("loby joined", "onClick: ");
                      else
                          Log.i("not joined", "onClick: ");
                        while(true) {
                           boolean gameFound=gameFound();
                           if (gameFound==true)
                               break;
                        }
                    }

                   goToGameActivity(game_this);
                }
            }
        });


        turnShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                align_selected = (align_selected + 1) % 2;
            }
        });
        clearBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("czyszczenie", "uruchomiono cyszczenie planszy ");
                //to będzie skomplikowane
                for (int i = 0; i < 10; i++) {
                    for (int x = 0; x < 10; x++) {
                        Log.i("czyszczenie", "usuwanie referencji");
                        ((Field) (game_this.getPlayer1().getPlayerBard().fields.get(i).get(x))).oocupyingShip = null;
                        Log.i("efekt suawaniareferencji", "wynik:" + ((Field) (game_this.getPlayer1().getPlayerBard().fields.get(i).get(x))).isOccupied());


                    }
                }

                game_this.getPlayer1().shipsSizes.clear();
                game_this.getPlayer1().ships.clear();
                for (int i = 1; i < 5; i++) {
                    for (int x = i - 1; x < 4; x++) {
                        Log.i("czyszczenie", "dodano rozmiar" + i);
                        game_this.getPlayer1().shipsSizes.add(i);
                    }
                }

                //czyszczenie planszy
                drawBoardPlacing();
                updateCountShips();
            }

        });
    }

    private boolean joinGame() {
        Connection conn = new Connection();

        RequestBody body = conn.searchingGameBody(game_this.getPlayer1().getId());
        AtomicBoolean lobbyJoined = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = conn.post(Endpoints.GAME_JOIN.getEndpoint(), body);
                Log.i("the response", response);
                if (!response.equals(true)) {
                    String status = response;
                    Log.i("status", status);
                } else {
                    lobbyJoined.set(true);
                }


            } catch (IOException e) {
                e.printStackTrace();
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

        return lobbyJoined.get();
    }
    private boolean gameFound() {
        Connection conn2 = new Connection();

        RequestBody body = conn2.searchingGameBody(game_this.getPlayer1().getId());
        AtomicBoolean gameFound = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                String response = conn2.get(Endpoints.GAME_QUEUE.getEndpoint());
                JSONObject json = Connection.stringToJson(response);
                Log.i("queue response", response);
                if (json.has("status")) {
                    String status = json.getString("status");
                    Log.i("no user of this id logged in", status);
                } else {
                    if (response.equals("true")){
                        gameFound.set(true);
                    }
                    else {
                        Log.i("game not found yet", "");
                    }
                }


            } catch ( JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

        return gameFound.get();
    }

    private void setLocalPlayerID() {
        int id;
        SharedPreferences sharedPred = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        id = Integer.valueOf(sharedPred.getString("uid", null));
        game_this.getPlayer1().setId(id);
    }

    private void goBackToMainMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void goToGameActivity(Game game) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", game_this);
        startActivity(intent);
    }

    private void updateCountShips() {
        ArrayList<Integer> a = Game.histogram(game_this.getPlayer1().shipsSizes);
        a = Game.histogram(game_this.getPlayer1().shipsSizes);
        TextView x4 = findViewById(R.id.textView4xShipsLeft);
        TextView x3 = findViewById(R.id.textView3xShipsLeft);
        TextView x2 = findViewById(R.id.textView2xShipsLeft);
        TextView x1 = findViewById(R.id.textView1xShipsLeft);

        x1.setText(String.valueOf(a.get(0)) + "x");
        x2.setText(String.valueOf(a.get(1)) + "x");
        x3.setText(String.valueOf(a.get(2)) + "x");
        x4.setText(String.valueOf(a.get(3)) + "x");

    }

    private void vsAiProcedure() throws Exception {
//        game_this.place_ship(new Move(0, 0, 0), 2, 1);
//        game_this.place_ship(new Move(2, 0, 0), 2, 1);
//        game_this.place_ship(new Move(4, 0, 0), 2, 1);
//        game_this.place_ship(new Move(6, 0, 0), 2, 1);
//        game_this.place_ship(new Move(9, 0, 0), 2, 1);
//        game_this.place_ship(new Move(0, 6, 0), 2, 1);
//        game_this.place_ship(new Move(2, 6, 0), 2, 1);
//        game_this.place_ship(new Move(4, 6, 0), 2, 1);
//        game_this.place_ship(new Move(0, 9, 0), 2, 1);
//        game_this.place_ship(new Move(9, 9, 0), 2, 1);
//        game_this.place_ship(new Move(1, 9, 0), 2, 1);//ponad limit
//        game_this.place_ship(new Move(9, 9, 0), 2, 1);//ponad limit
//        game_this.place_ship(new Move(8, 6, 0), 2, 1);//ponad limit
        Random random = new Random();
        int x, y, align;
        while (game_this.getPlayer2().shipsSizes.size() > 0) {
            x = random.nextInt(10);
            y = random.nextInt(10);
            align = random.nextInt(1);
            game_this.place_ship(new Move(x, y, 0), 2, align);
        }
    }

    private void drawBoardPlacing() {
        ImageView pom;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                pom = findViewById(10 * x + y);
                if (((Field) (game_this.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()) {

                    pom.setImageResource(R.drawable.field_without_ship);
                } else {
                    pom.setImageResource(R.drawable.field);
                }
            }
        }
    }


    public void createBoard(Context context) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        int imageResourceField = R.drawable.field;
        int imageResourceFieldWithoutShip = R.drawable.field_without_ship;

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            for (int j = 0; j < 10; j++) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));

                imageView.setImageResource(imageResourceField);

                int marginInPixels = 2; // W pikselach
                float density = getResources().getDisplayMetrics().density;
                int marginInDp = (int) (marginInPixels / density);

                TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) imageView.getLayoutParams();
                layoutParams.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
                imageView.setLayoutParams(layoutParams);

                // Nadanie unikalnego id dla każdego ImageView
                imageView.setId(i * 10 + j);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ImageView pom;
                        ImageView clickedImageView = (ImageView) v;
                        // clickedImageView.setImageResource(imageResourceFieldWithoutShip);
                        int pos = clickedImageView.getId();
                        Integer[] posId = new Integer[0];
                        posId = getFieldId(pos);
                        //ilosc statków
                        updateCountShips();

                        // Snackbar.make(tableLayout, "Clicked on field " + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]), Snackbar.LENGTH_SHORT).show();
                        try {
                            game_this.place_ship(new Move(posId[0], posId[1], 0), 1, align_selected);
                            drawBoardPlacing();
                            updateCountShips();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
    }
}
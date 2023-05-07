package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.Move;
import com.battleships.model.client.ship.Ship;
import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       Game game_this;
        try {
            Game game=new Game(1,0);
            game_this=game;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Context context = getApplicationContext();

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

                // Nadanie unikalnego id dla kaÅ¼dego ImageView
                imageView.setId(i * 10 + j);
                Integer[] pos2 = new Integer[2];
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView clickedImageView = (ImageView) v;
                        clickedImageView.setImageResource(imageResourceFieldWithoutShip);
                        int pos = clickedImageView.getId();
                        if (pos < 9) {
                            pos2[0] = pos;
                            pos2[1] = 0;
                        }
                        else
                        {
                            pos2[0]=pos%10;
                            pos2[1]=pos/10;
                        }
                        Snackbar.make(tableLayout, "test1 " + String.valueOf(pos2[0])+", "+String.valueOf(pos2[1]), Snackbar.LENGTH_SHORT).show();

                        try {
                            game_this.getMove(new Move(pos2[0],pos2[1],1),1);
                        } catch (Exception e) {
                           //throw new RuntimeException(e);
                        }


                    }
                });

                tableRow.addView(imageView);
            }

            tableLayout.addView(tableRow);
        }
    }
}
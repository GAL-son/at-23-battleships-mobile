package com.battleships;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SetShipsActivity extends AppCompatActivity implements View.OnTouchListener {
    private TableLayout tableLayout = findViewById(R.id.tableLayout);
    private ImageView ship1x = findViewById(R.id.imageViewShip1x);
    private ImageView ship2x = findViewById(R.id.imageViewShip2x);
    private ImageView ship3x = findViewById(R.id.imageViewShip3x);
    private ImageView ship4x = findViewById(R.id.imageViewShip4x);


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        createBoard(context);

        ship1x.setOnTouchListener(this);
        ship2x.setOnTouchListener(this);
        ship3x.setOnTouchListener(this);
        ship4x.setOnTouchListener(this);

    }

    public void createBoard(Context context){

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

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView clickedImageView = (ImageView) v;
                        clickedImageView.setImageResource(imageResourceFieldWithoutShip);
                        int pos = clickedImageView.getId();
                    }
                });
                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

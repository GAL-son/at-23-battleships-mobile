package com.battleships.model.client.players;

import android.util.Log;

import com.battleships.model.client.board.Board;
import com.battleships.model.client.board.Field;
import com.battleships.model.client.players.difficulty.Difficulty;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class PlayerAi extends Player implements Serializable {
    Difficulty difficulty;

    public ArrayList<ArrayList<Integer>> pojectionOfBoard = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> targeted_ship= new ArrayList<>();
    //this will be next version // to jest chyba wykonalne

    public PlayerAi() {
        super();
        for (int m = 0; m < 10; m++) {
            pojectionOfBoard.add(new ArrayList<Integer>());
            for (int n = 0; n < 10; n++) {
                pojectionOfBoard.get(m).add(1);
            }

        }
    }

    public ArrayList<Integer> getAImove(Board board) {
        //  pojectionOfBoard.get(5).set(5,99);
        // pojectionOfBoard.get(5).set(6,98);
        // pojectionOfBoard.get(5).set(7,97);
        Log.i("ai", "error catch 1");
        pojectionOfBoard.get(5).set(8, 3);
        Boolean stop = false;

        int greatest_value = pojectionOfBoard.get(0).get(0);
        if (greatest_value == 0) {
            for (int m = 0; m < 10; m++) {
                for (int n = 0; n < 10; n++) {

                    if (pojectionOfBoard.get(n).get(m) != 0 && stop != true) {
                        greatest_value = pojectionOfBoard.get(n).get(m);
                        stop = true;
                    }
                }

            }
        }


        for (
                int m = 0;
                m < 10; m++) {
            Log.i("ai", "error catch 2.1");
            for (int n = 0; n < 10; n++) {
                Log.i("ai", "error catch 2.2");
                if (((Field) board.fields.get(n).get(m)).getWasHit()) {
                    pojectionOfBoard.get(n).set(m, 0);
                }
                if (pojectionOfBoard.get(n).get(m) > greatest_value) {
                    greatest_value = pojectionOfBoard.get(n).get(m);
                    Log.i("gv", "greatest value=" + greatest_value);
                }
            }
        }

        Log.i("ai", "error catch 2");


        int x, y;
        Random random = new Random();
        ArrayList<Integer> AImove = new ArrayList<>();
        x = random.nextInt(10);
        y = random.nextInt(10);
        while (((Field) board.fields.get(x).

                get(y)).

                getWasHit() || pojectionOfBoard.get(x).

                get(y) < greatest_value) {
            x = random.nextInt(10);
            y = random.nextInt(10);
        }

        Log.i("ai", "error catch 3");
        AImove.add(x);
        AImove.add(y);
        Log.i("ai", "error catch 4");
        if (((((Field) board.fields.get(x).

                get(y)).

                isOccupied()))) {
            Log.i("targeting", "trafiono");
            for (int y_i = -1; y_i < 2; y_i++) {
                for (int x_i = -1; x_i < 2; x_i++) {
                    if ((x_i == 0 && y_i == 0) || (x_i == -1 && y_i == -1) || (x_i == 1 && y_i == 1) || (x_i == -1 && y_i == 1) || (x_i == 1 && y_i == -1)) {
                        Log.i("targeting", "srodek");

                    } else {
                        Log.i("targeting", "nie srodek");
                        if (x + x_i < 10 && x + x_i >= 0 && y + y_i < 10 && y + y_i >= 0) {

                            Log.i("targeting", "wykonywanie targeting dla" + (x + x_i) + " , " + (y + y_i));
                            pojectionOfBoard.get(x + x_i).set(y + y_i, pojectionOfBoard.get(x + x_i).get(y + y_i) * 9);
                        }
                    }
                }
            }

        }
        Log.i("ai", "error catch 5");

        String pom = "\n\n";
        for (
                int m = 0;
                m < 10; m++) {
            for (int n = 0; n < 10; n++) {
                pom = pom + pojectionOfBoard.get(n).get(m) + ',';
            }

            pom = pom + '\n';
        }
        Log.i("pojection", pom);

        Log.i("ai", "error catch 6");


        return AImove;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}

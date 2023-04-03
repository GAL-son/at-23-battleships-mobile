package client.board;

import java.util.ArrayList;

public class Board {

    private int size=10;
    ArrayList<ArrayList> fields = new ArrayList<>();

    Board() {

        for (int n = 0; n < this.size; n++) {
            fields.add(new ArrayList());
        }

        for (int m = 0; m <this.size; m++) {
            for (int n = 0; n < this.size; n++) {
                fields.get(n).add(new Field());
            }
        }

    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

}

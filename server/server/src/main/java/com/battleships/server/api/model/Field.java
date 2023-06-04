package com.battleships.server.api.model;

public class Field {
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public final int x, y;
}
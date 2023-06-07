package com.battleships.server.api.model;

import org.json.JSONArray;

public class Field {
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public JSONArray toJsonArray()
    {
        JSONArray xy = new JSONArray();
        xy.put(x);
        xy.put(y);
        return xy; 
    }

    public final int x, y;
}
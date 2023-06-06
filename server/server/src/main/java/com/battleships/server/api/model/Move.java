package com.battleships.server.api.model;

import org.json.JSONObject;

public class Move {
    
    public Move(int uid, int x, int y)
    {
        this.move = new Field(x, y);
        this.uid = uid;
    }

    public Move(int uid, Field move) {
        this.uid = uid;
        this.move = move;
    }

    public Field getMove() {
        return move;
    }

    public int getUid() {
        return uid;
    }

    public int getX() {
        return this.move.x;
    }

    public int getY() {
        return this.move.y;
    }

    public JSONObject toJsonObject()
    {
        JSONObject obj = new JSONObject();
        obj.put("uid", uid);
        obj.put("field", move.toJsonArray());
        return obj;
    }

    private Field move;
    private int uid;
}

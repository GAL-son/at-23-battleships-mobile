package com.battleships.model.client;

import android.util.Log;

import com.battleships.Connection;
import com.battleships.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GameStateFromServer {
    private int turnid;
    private int gid;

    private int oponentScore;
    private String oponentLogin;
    private int playerScore;
    private String playerLogin;
    private Integer lastx=null;
    private Integer lasty=null;

    private int lastShootingUserID;

    public int getLastShootingUserID() {
        return lastShootingUserID;
    }



    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public void setLastx(int lastx) {
        this.lastx = lastx;
    }

    public void setLasty(int lasty) {
        this.lasty = lasty;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    public Integer getLastx() {
        return lastx;
    }

    public Integer getLasty() {
        return lasty;
    }

    private boolean isStarted;
    private boolean isFinished;

    public int getTurnid() {
        return turnid;
    }

    public int getGid() {
        return gid;
    }

    public int getOponentScore() {
        return oponentScore;
    }

    public String getOponentLogin() {
        return oponentLogin;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String playerLogin() {
        return playerLogin;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setTurnid(int turnid) {
        this.turnid = turnid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setOponentScore(int oponentScore) {
        this.oponentScore = oponentScore;
    }

    public void setOponentLogin(String oponentLogin) {
        this.oponentLogin = oponentLogin;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void playerLogin(String login) {
        this.playerLogin = login;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
    public void setLastShootingUserID(int lastShootingUserID) {
        this.lastShootingUserID = lastShootingUserID;
    }

    @Override
    public String toString() {
        return "GameStateFromServer{" +
                "turnid=" + turnid +
                ", gid=" + gid +
                ", oponentScore=" + oponentScore +
                ", oponentLogin='" + oponentLogin + '\'' +
                ", playerScore=" + playerScore +
                ", playerLogin='" + playerLogin + '\'' +
                ", lastx=" + lastx +
                ", lasty=" + lasty +
                ", lastShootingUserID=" + lastShootingUserID +
                ", isStarted=" + isStarted +
                ", isFinished=" + isFinished +
                '}';
    }

    public GameStateFromServer() {

    }

    static public GameStateFromServer getState(JSONObject state) throws JSONException {

        GameStateFromServer tmp1 = new GameStateFromServer();

        tmp1.turnid = state.getInt("turnId");
        tmp1.gid = state.getInt("gid");
        JSONObject enemy = (JSONObject) state.get("opponent");
        JSONObject you = (JSONObject) state.get("opponent");

        tmp1.oponentLogin = enemy.getString("login");
        tmp1.oponentScore = enemy.getInt("score");

        tmp1.playerLogin = you.getString("login");
        tmp1.playerScore = you.getInt("score");

        if(state.has("lastMove")) {

            JSONObject jsoonInner = state.getJSONObject("lastMove");
            JSONArray movexy = jsoonInner.getJSONArray("field");

            tmp1.lastx = movexy.getInt(0);
            tmp1.lasty = movexy.getInt(1);
            tmp1.lastShootingUserID = jsoonInner.getInt("uid");
        }


        return tmp1;
    }



}

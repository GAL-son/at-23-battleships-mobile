package com.battleships.model.client.players;

import java.io.Serializable;

public class PlayerRemote extends Player implements Serializable {


    private int ConnectedServerId;


    public int getConnectedServerId() {
        return ConnectedServerId;
    }

    public void setConnectedServerId(int connectedServerId) {
        ConnectedServerId = connectedServerId;
    }
}

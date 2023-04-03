package client.players;

public class PlayerRemote extends Player {


    private int ConnectedServerId;


    public int getConnectedServerId() {
        return ConnectedServerId;
    }

    public void setConnectedServerId(int connectedServerId) {
        ConnectedServerId = connectedServerId;
    }
}

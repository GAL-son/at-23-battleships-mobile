import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import org.json.*;;


public class GameServer {
    // Server config
    public final int ID;
    private int serverPort;

    //Server Runtime States
    static enum ServerState {
        SERVER_NOT_READY,
        SERVER_READY,
        SERVER_RUNNING,
        SERVER_INIT_FAILED
    };

    private ServerState state;
    private boolean isActive;

    // Server Runtime Variables
    private LinkedList<Session> sessions;
    private LinkedList<Socket> openConnections;
    private LinkedList<Request> requestQueue;
    
    // Server Active Members
    private HttpRequestHandler httpRequestHandler;
    private DataBaseServerConnection dataBaseServerConnection;

    int testCycles;

    public GameServer(int ID, int serverPort, int testCycles) throws IOException
    {
        state = ServerState.SERVER_NOT_READY;
        this.ID = ID;
        this.serverPort = serverPort;

        sessions = new LinkedList<>();
        openConnections = new LinkedList<>();
        requestQueue = new LinkedList<>();

        try {
            httpRequestHandler = new HttpRequestHandler(serverPort);
        } finally {
            state = ServerState.SERVER_INIT_FAILED;
        }

        state = ServerState.SERVER_READY;    
        this.testCycles = testCycles;
    }

    public void startServer() throws RuntimeException {
        if(state.equals(ServerState.SERVER_INIT_FAILED)) {
            throw new RuntimeException("Start impossible failed to initialize server");
        }

        state = ServerState.SERVER_RUNNING;
        isActive = true;

        // TEST
        int i = 0;

        // Server run loop
        while(isActive && i < testCycles)
        {
            // Get new Connections
            try {
                openConnections.push(httpRequestHandler.getClientSocket());
            } catch (IOException e) {}      

            // Get new Request from openConnections
            for (Socket socket : openConnections) {
                try {
                    requestQueue.push(new Request(httpRequestHandler.getHTTPRequest(socket)));
                } catch (IOException e) {}
            }            
            
            // Remove Expired Sessions
            removeExpiredSessions();
        }
    }

    public void createSession(int sesssionID, int userID)
    {
        sessions.add(new Session(userID));
    }

    private void removeExpiredSessions()
    {
        for (Session session : sessions) {
            if(session.isSessionExpired()) sessions.remove(session);
        }
    }
}


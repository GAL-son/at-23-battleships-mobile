import java.io.IOException;
import java.net.Socket;
import java.security.DrbgParameters.Reseed;
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
        while(isActive)
        {
            try {
                Socket s = httpRequestHandler.getClientSocket();
                
                Request r = new Request(httpRequestHandler.getHTTPRequest(s), s);
                System.out.println(r.getRequestJson());
                String response = "";
                   
                response += "<h1>TwojaStara<h1>";

                System.out.println(response);

                httpRequestHandler.sendResponse(s, response);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // // Get new Connections
            // try {
            //     Socket s = httpRequestHandler.getClientSocket();
            //     requestQueue.push(new Request(httpRequestHandler.getHTTPRequest(s), s));
            // } catch (IOException e) {}                 

            // for (Request request : requestQueue) {
            //     System.out.println(request.getRequestJson());
            // }

            // System.out.println(i + "\n");
            // if(i >= 3)
            // {
            //     for (Request request : requestQueue) {
            //         String response = "";
            //         response += "HTTP/1.1 200 OK\r\n";
            //         response += "<p>TwojaStara<p>";

            //         try {
            //             httpRequestHandler.sendResponse(request.responseSocket, response);
            //             requestQueue.remove(request);
            //         } catch (IOException e) {
            //             // TODO Auto-generated catch block
            //             e.printStackTrace();
            //         }
            //     }
            // }
            
            // // Remove Expired Sessions
            // i++;
            // removeExpiredSessions();
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


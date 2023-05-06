import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import org.json.*;

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

    static class Actions {
        public static final String LOGIN = "login";
        public static final String REGISTER = "register";    
        public static final String GET_STATS = "getStats";

    }

    private ServerState state;
    private boolean isActive;

    // Server Runtime Variables
    private LinkedList<Session> sessions;
    //private LinkedList<Socket> openConnections;
    //private LinkedList<Request> requestQueue;
    
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
        //openConnections = new LinkedList<>();
        //requestQueue = new LinkedList<>();

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
                JSONObject rObj = r.getRequestBody();

                System.out.println(r.getRequestJson());
                String response = "";

                if(!rObj.has("Action")){
                    response = "REQUEST HAS NO ACTION FIELD";
                    httpRequestHandler.sendResponse(s, response, HttpRequestHandler.ResponseType.NOT_FOUND);
                    continue;
                }                      

                response = respond(rObj);
                System.out.println(response);

                httpRequestHandler.sendResponse(s, response, HttpRequestHandler.ResponseType.OK);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private String respond(JSONObject body) throws IOException{
        String action = body.getString("Action");

        switch (action) {
            case Actions.REGISTER:
                // Send Email and Add user to database
                return "REGISTER";
            case Actions.LOGIN:
                // Check if User Exists in database and check his password

                createSession(Integer.parseInt(body.getString("UserID")));
                return "SESSION CEARTED";
            case Actions.GET_STATS:
                //Fetch stats from Database

                return "GET_STATS";         
            default:
                break;
        }

        return "";
    }

    public void createSession(int userID)
    {
        Session s = new Session();
        s.addUser(new User(userID));
        sessions.push(s);
    }

    private void removeExpiredSessions()
    {
        for (Session session : sessions) {
            if(session.isSessionExpired()) sessions.remove(session);
        }
    }
}


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
        public static final String SEARCH_GAME = "searchGame";
        public static final String GAME_UPDATE = "gameUpdate";
        public static final String GAME_SET_SHIPS = "gameSetShips";
    }

    private ServerState state;
    private boolean isActive;

    // Server Runtime Variables
    private LinkedList<Session> sessions;
    private LinkedList<GameState> runningGames;
    private LinkedList<User> playerQueue;
    
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
        runningGames = new LinkedList<>();
        playerQueue = new LinkedList<>();

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
        int PID = Integer.parseInt(body.getString("UserID"));

        switch (action) {
            case Actions.REGISTER:
                // Send Email and Add user to database
                return "REGISTER";
            case Actions.LOGIN:
                // Check if User Exists in database and check his password

                createSession(PID);
                return "SESSION CEARTED";
            case Actions.GET_STATS:
                //Fetch stats from Database

                return "GET_STATS";         
            case Actions.SEARCH_GAME:
                boolean playerExist = false;
                Session playerSession = null;
                int searchID = PID;
                for(Session session : sessions) {
                    if(session.user.ID == searchID) { 
                        playerExist = true;
                        playerSession = session;
                        break;
                    }
                }

                if(!playerExist) {
                    return "LOGOUT";
                }

                boolean isPlayerInQueue = false;
                User searchUser = null;
                for(User user : playerQueue) {
                    if(user.ID == searchID) {
                        isPlayerInQueue = true;
                        searchUser = user;
                        break;
                    }
                }

                if(!isPlayerInQueue) {
                    playerQueue.push(playerSession.user);
                    return "JOINED QUEUE";
                }

                if(playerQueue.size() > 1)
                {
                    int foundPlayer = playerQueue.get((int)Math.random()%playerQueue.size()).ID;
                    GameState gamestate = new GameState();
                    gamestate.addPlayers(foundPlayer, searchID);
                    runningGames.push(gamestate);
                    return "PLAYER FOUND" + foundPlayer + " " + gamestate.gameID;
                } else {
                    return "SEARCHING FOR PLAYERS";
                }  
            case Actions.GAME_UPDATE:
                int gameID = Integer.valueOf(body.getString("GameID"));
                GameState currentGame = null;

                for(GameState game : runningGames) {
                    if(gameID == game.gameID)
                    {
                        currentGame = game;
                    }
                }

                String subAction = body.getString("GameAction");

                resolveGameAction(currentGame, subAction, PID);
                
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

    private String resolveGameAction(GameState game, String action, int PID) {
        switch (action) {
            case Actions.GAME_SET_SHIPS:
                
                game.setPlayerShips(PID, null);
                break;
        
            default:
                break;
        }

        return "";
    }

    private void removeExpiredSessions() {
        for (Session session : sessions) {
            if(session.isSessionExpired()) sessions.remove(session);
        }
    }
}


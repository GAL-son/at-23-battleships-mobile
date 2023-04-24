import java.util.LinkedList;

public class GameServer {
    public final int ID;
    
    private LinkedList<Session> sessions;
    private boolean isActive;


    public GameServer(int ID)
    {
        this.ID = ID;
        sessions = new LinkedList<>();
        isActive = false;
    }

    public void startServer()
    {
        isActive = true;

        while(isActive)
        {
            // Handle sessions and requests
        }
    }

    public void createSession(int sesssionID, int userID)
    {
        sessions.add(new Session(userID));
    }
}

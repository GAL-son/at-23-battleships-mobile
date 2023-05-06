// import java.util.ArrayList;

public class Session {
    public final int ID;
    static int freeID = 0;
    
    User user;
    int lifetime;
    long lastUpdateTime;
    
    public Session()
    {
        this.ID = freeID;
        freeID++;
        lifetime = 300;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public Session(int lifetimeSeconds)
    {
        this.ID = freeID;
        freeID++;
        lifetime = lifetimeSeconds;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void addUser(User user)
    {
        this.user = user;
        this.update();
    }

    public boolean isSessionExpired()
    {
        return System.currentTimeMillis() >= lastUpdateTime + lifetime * 1000;
    } 

    public void update()
    {
        this.lastUpdateTime = System.currentTimeMillis();
    }


}

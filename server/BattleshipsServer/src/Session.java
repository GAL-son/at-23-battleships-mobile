// import java.util.ArrayList;

public class Session {
    public final int ID;
    
    User user;
    int lifetime;
    long lastUpdateTime;
    
    public Session(int ID)
    {
        this.ID = ID;
        lifetime = 300;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public Session(int ID, int lifetimeSeconds)
    {
        this(ID);
        this.update();
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

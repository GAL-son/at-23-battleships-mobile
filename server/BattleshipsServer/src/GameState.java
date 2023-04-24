public class GameState {
    public int p1ID;
    public int p2ID;

    public int turn;
    public State state;

    public enum State
    {
        STARTING,
        SHIP_PLACEMENT,
        RUNNING,
        FINISHED
    }
}

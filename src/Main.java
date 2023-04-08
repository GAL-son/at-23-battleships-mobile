import client.Game;
public class Main {
    public static void main(String[] args) {

        try {
            Game game = new Game(1, 4);
        }catch( Exception e)
        {
            e.printStackTrace();
        }


    }
}
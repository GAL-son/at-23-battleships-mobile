public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        GameServer server = new GameServer(1, 8081, 10000);

        server.startServer();
    }
}

import java.net.*;
import java.io.*;

public class HttpRequestHandler {
    //private int port;
    private ServerSocket serverSocket;

    HttpRequestHandler(int port) throws IOException {
        //this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public Socket getClientSocket() throws IOException
    {
        return  serverSocket.accept();
    }

    public String getHTTPRequest( Socket clientSocket) throws IOException {        

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message = "";
       
        String s;
        while((s = in.readLine())!= null) {
            message += s;
            if(s.isEmpty()){
                break;
            }
        }

        return message;
    }

    public void sendResponse(Socket clientSocket, String response) throws IOException {
        OutputStream client = clientSocket.getOutputStream();
        client.write(response.getBytes());

        client.flush();
    }  

}

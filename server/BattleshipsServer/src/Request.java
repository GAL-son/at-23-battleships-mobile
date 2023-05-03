import java.io.IOException;
import java.net.Socket;

import org.json.*;

public class Request {
    JSONObject request;
    Socket responseSocket;

    String requestPlainText;


    public Request(String s, Socket socket) throws IOException{
        this.requestPlainText = s; 
        this.responseSocket = socket;
        this.request = new JSONObject();   
        int currentIndex = 0;

        if(s.isEmpty()) throw new IOException("Empty message");
        
        // Request Method
        {
            int methodEnd = s.indexOf(" ");
            request.append("Method", s.substring(currentIndex, methodEnd));
            currentIndex = methodEnd;
        }

        // Request Path and search Querry
        {
            int pathAndQuerryEnd = s.indexOf(" ", currentIndex + 1);
            String pathAndQuerry = s.substring(currentIndex, pathAndQuerryEnd);


            String path = "";
            String queryString = "";
            JSONObject query = new JSONObject();

            if(pathAndQuerry.contains("?")) {
                int questionMarkIndex = pathAndQuerry.indexOf("?");
                path = pathAndQuerry.substring(0, questionMarkIndex);
                queryString = pathAndQuerry.substring(questionMarkIndex);

                String queryItems[] = queryString.split("&");
                
                for (String string : queryItems) {
                    String item[] = string.split("=");
                    query.append(item[0], item[1]);
                }
            } else {
                path = pathAndQuerry;
            }
            
            request.append("Path", path);
           
            request.append("Query", query);
            currentIndex = pathAndQuerryEnd;
        }

        // HTTP version
        {

        }

        // request Body        
        int bodyStart = (s.contains("{")) ? s.indexOf("{") : s.length()-1;

        String requestBody = (bodyStart == s.length()-1) ? "" : s.substring(bodyStart, s.length()-1);

        System.out.println( requestBody);
        if(!requestBody.isBlank())
            request.append("Body", new JSONObject(requestBody));
    }

    public JSONObject getRequestJson()
    {
        return request;
    }

    public JSONObject getRequestBody()
    {
        if (request.has("Body")){
            return request.getJSONObject("Body");
        } else {
            return new JSONObject();
        }
    }

}



import org.json.*;

public class Request {
    String requestPlainText;
    String requestHeader;
    String requestBody;

    public Request(String s) {
        this.requestPlainText = s;

        String headerMarker = "HTTP/1.1";
        if(!requestPlainText.contains(headerMarker)){
            requestHeader =  "";
        } else {
            int headerStart = requestPlainText.indexOf(headerMarker);
            requestHeader = requestPlainText.substring(headerStart);

            if(!requestPlainText.contains("GET /")) {
                this.requestBody = "";
            } else {
                int bodyStart = requestPlainText.indexOf("GET /");
                this.requestBody = requestPlainText.substring(bodyStart, headerStart);
            }
        }
    }

    public String getRequestHeader() {
       return this.requestHeader;
    }

    public String getRequestBody()
    {
        return requestBody;
    }
}



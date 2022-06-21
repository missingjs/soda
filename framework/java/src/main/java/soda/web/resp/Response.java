package soda.web.resp;

import com.sun.net.httpserver.Headers;

public interface Response {
    
    int getHttpCode();

    Headers getHeaders();

    String getContentType();

    byte[] getBody();

}

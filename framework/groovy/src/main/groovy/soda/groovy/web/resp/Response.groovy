package soda.groovy.web.resp

import com.sun.net.httpserver.Headers

interface Response {

    int getHttpCode()

    Headers getHeaders()

    String getContentType()

    byte[] getBody()

}